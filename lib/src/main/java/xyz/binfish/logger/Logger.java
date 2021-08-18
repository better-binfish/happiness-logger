package xyz.binfish.logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import java.util.Arrays;
import java.util.Date;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Logger {

	private static Logger instance;
	private static PrintStream writer;

	private static LoggerConfig config;
	private static File logDirectoryFile;

	private static final ThreadLocal<String> localThread = new ThreadLocal<>();

	private static boolean canWrite = false;
	private static boolean isInitialized = false;


	private Logger() {
		if(config.hasDirectory()) {
			try {
				this.logDirectoryFile = new File(config.getDirectory());

				if(logDirectoryFile.exists() && logDirectoryFile.isDirectory()) {
					if(config.isAutoCleaning()) {
						this.scanDir(logDirectoryFile);
					}

					String absolutePathToFile = String.format(
							"%s/%s.log", logDirectoryFile.getAbsolutePath(), java.time.LocalDate.now()
					);

					File logFile = new File(absolutePathToFile);

					if(!logFile.exists()) {
						logFile.createNewFile();
					}

					if(logFile.isFile() && logFile.canWrite()) {
						canWrite = true;
						writer = new PrintStream(new FileOutputStream(logFile, true));
					}
				} else {
					throw new IOException("The specified path is not a directory, or it does not exist.");
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * Creates an instance of the logger and returns it.
	 *
	 * @param modifiConfig the modified logger configuration object.
	 * @return the created logger instance.
	 */
	public static Logger createLogger(LoggerConfig modifiConfig) {
		if(isInitialized) {
			throw new ExceptionInInitializerError("The logger has already been initialized.");
		}

		if(modifiConfig == null) {
			config = new LoggerConfig();
		} else {
			config = modifiConfig;
		}

		Formatter.setTimePattern(config.getFormatTimePattern());
		isInitialized = true;
		instance = new Logger();

		return instance;
	}

	public static Logger getLogger() {
		if(isInitialized) {
			return instance;
		} else {
			throw new NullPointerException("The logger was not initialized.");
		}
	}

	public void closeLogger() {
		if(isInitialized) {
			instance = null;
			canWrite = false;
			isInitialized = false;

			writer.close();
		} else {
			throw new NullPointerException("The logger was not initialized.");
		}
	}

	/*
	 * Get the configuration of the logger as a LoggerConfig object.
	 *
	 * @return the LoggerConfig object.
	 */
	public static LoggerConfig getConfig() {
		return config;
	}


	/*
	 * Get the name of the current thread.
	 *
	 * @return the name of the thread as String.
	 */
	public static String getThreadName() {
		String thread = localThread.get();

		if(thread != null) {
			return thread;
		}

		return null;
	}

	public static Logger setThread(String thread) {
		if(thread != null) {
			localThread.set(thread);
		}

		return new Logger();
	}

	/*
	 * Below are methods for printing to the console
	 * and, if possible, to a file of the messages passed
	 * as an argument. Messages are formatted using Formatter
	 * class in the #log(String, int) method.
	 */
	public void log(String message) {
		this.log(message, 0);
	}

	public void log(String message, int orderLevel) {
		if(message == null || message.length() == 0) {
			message = "NULL also known as nothing.";
		}

		message = Formatter.format(Level.getLevel(orderLevel), message);

		System.out.printf("%s%n", message);

		if(canWrite) this.write(message);
	}

	public void error(@Nonnull Throwable throwable) {
		this.log(throwable.getMessage() + ": " + throwable.getCause() + "\n"
				+ Formatter.getStackTraceString(throwable), 1);
	}

	public void error(@Nonnull String message) {
		this.log(message, 1);
	}

	public void error(@Nonnull String message, @Nonnull Throwable throwable) {
		this.log(message + "\n" + throwable.getMessage() + ": " + throwable.getCause() + "\n"
				+ Formatter.getStackTraceString(throwable), 1);
	}

	public void warn(@Nonnull String message) {
		this.log(message, 2);
	}

	public void info(@Nonnull String message) {
		this.log(message, 3);
	}

	public void debug(@Nonnull String message, @Nullable Object... args) {
		if(args != null || args.length != 0) {
			message = String.format(message, args);
		}

		this.log(message, 4);
	}

	public void debug(@Nonnull Object object) {
		this.log(toString(object), 4);
	}

	/*
	 * Converts the incoming object to a string, if this
	 * is not possible, a prepared message is returned.
	 *
	 * @param object the Object for converting.
	 * @return the converted object as String, or prepared message.
	 */
	private String toString(Object object) {
		if(object == null) {
			return "null";
		}

		if(!object.getClass().isArray()) {
			return object.toString();
		}

		if(object instanceof boolean[]) {
			return Arrays.toString((boolean[]) object);
		}
		if(object instanceof byte[]) {
			return Arrays.toString((byte[]) object);
		}
		if(object instanceof char[]) {
			return Arrays.toString((char[]) object);
		}
		if(object instanceof short[]) {
			return Arrays.toString((short[]) object);
		}
		if(object instanceof int[]) {
			return Arrays.toString((int[]) object);
		}
		if(object instanceof long[]) {
			return Arrays.toString((long[]) object);
		}
		if(object instanceof float[]) {
			return Arrays.toString((float[]) object);
		}
		if(object instanceof double[]) {
			return Arrays.toString((double[]) object);
		}
		if(object instanceof Object[]) {
			return Arrays.toString((Object[]) object);
		}

		return "Couldn't find a correct type for the object.";
	}

	/*
	 * Writes a formatted string from arguments using 
	 * PrintStream to a file, provided that it can write.
	 *
	 * @param formattedMessage the formatted string to write.
	 */
	private void write(String formattedMessage) {
		writer.printf("%s%n", formattedMessage);
	}

	/*
	 * Checks a directory for old files, and deletes if any.
	 *
	 * @param dir the directory as File object for getting the files.
	 */
	private void scanDir(File dir) throws IOException {
		File[] directoryFilesList = dir.listFiles();

		if(directoryFilesList != null) {
			for(int i = 0; i < directoryFilesList.length; i++) {
				BasicFileAttributes attrs = Files.readAttributes(directoryFilesList[i].toPath(), BasicFileAttributes.class);
				FileTime time = attrs.creationTime();

				long diff = (new Date().getTime() - new Date(time.toMillis()).getTime());

				if((int) (diff / (60 * 1000)) > config.getMinimumAgeInMinutes()) {
					String filename = directoryFilesList[i].getName();

					directoryFilesList[i].delete();
					if(config.notifyDeleteFiles()) {
						this.log("\t-> File `" + filename + "` has been deleted.", 3);
					}
				}
			}
		}
	}
}
