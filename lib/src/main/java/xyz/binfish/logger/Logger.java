package xyz.binfish.logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import xyz.binfish.logger.Level;
import xyz.binfish.logger.Formatter;
import xyz.binfish.logger.LoggerConfig;

public class Logger {

	private static Logger instance;
	private static PrintStream writer;

	private static LoggerConfig config;

	private File logDirectoryFile;
	private Level globalLevel;

	private static ArrayList<Level> levels = new ArrayList<Level>(Arrays.asList(
					new Level("OFF", 0),
					new Level("ERROR", 1),
					new Level("WARN", 2),
					new Level("INFO", 3),
					new Level("DEBUG", 4)
				));

	private static boolean canWrite = false;
	private static boolean isInitialized = false;

	private Logger() {
		this.globalLevel = levels.get(0);

		if(config.logDirectory != null) {
			try {
				this.logDirectoryFile = new File(config.logDirectory);

				if(logDirectoryFile.exists() && logDirectoryFile.isDirectory()) {
					if(config.autoCleaning) {
						scanDir(logDirectoryFile);
					}

					String absolutePathToFile = logDirectoryFile.getAbsolutePath() + "/" + java.time.LocalDate.now() + ".log";
					File logFile = new File(absolutePathToFile);

					if(!logFile.exists()) {
						logFile.createNewFile();
					}

					if(logFile.isFile() && logFile.canWrite()) {
						canWrite = true;
						writer = new PrintStream(new FileOutputStream(logFile, true));
					}
				} else {
					throw new IOException("The specified path is not a directory, or it does not exist");
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Logger createLogger(LoggerConfig modifiConfig) {
		if(isInitialized) {
			throw new ExceptionInInitializerError("The logger has already been initialized");
		}

		if(modifiConfig == null) {
			config = new LoggerConfig();
		} else {
			config = modifiConfig;
		}

		Formatter.setTimePattern(config.formatTimePattern);
		isInitialized = true;
		instance = new Logger();

		return instance;
	}

	public static Logger getLogger() {
		if(isInitialized) {
			return instance;
		} else {
			throw new NullPointerException("The logger was not initialized");
		}
	}

	public void closeLogger() {
		if(isInitialized) {
			instance = null;
			canWrite = false;
			isInitialized = false;

			writer.close();
		} else {
			throw new NullPointerException("The logger was not initialized");
		}
	}

	public static boolean isInit() {
		return isInitialized;
	}

	public Level getLevel() {
		return globalLevel;
	}

	public void setLevel(String newLevel) {
		int index = 0;

		for(Level item : levels) {
			if(item.getName().equals(newLevel.toUpperCase())) {
				break;
			}
			index++;
		}

		try {
			this.globalLevel = levels.get(index);
		} catch(IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
	}

	public void setLevel(int newLevel) {
		this.globalLevel = levels.get(newLevel);
	}

	private void write(String formattedMessage) {
		writer.printf("%s%n", formattedMessage);
	}

	private void scanDir(File dir) throws IOException {
		File[] directoryFilesList = dir.listFiles();

		if(directoryFilesList != null) {
			for(int i = 0; i < directoryFilesList.length; i++) {
				BasicFileAttributes attrs = Files.readAttributes(directoryFilesList[i].toPath(), BasicFileAttributes.class);
				FileTime time = attrs.creationTime();

				long diff = (new Date().getTime() - new Date(time.toMillis()).getTime());

				if((int) (diff / (60 * 1000)) > config.getCleaningMinAge()) {
					String filename = directoryFilesList[i].getName();

					directoryFilesList[i].delete();
					if(config.notifyDeleteFiles) {
						this.log("\t-> File `" + filename + "` has been deleted", 3);
					}
				}
			}
		}
	}

	public void log(String message) {
		if(message == null || message.length() == 0) {
			message = "NULL log message";
		}

		if(globalLevel.getOrder() != 0) {
			message = "[" + globalLevel.getName() + "] " + message;
		}

		System.out.printf("%s%n", message);
		if(canWrite) write(message);
	}

	public void log(@Nonnull String message, int logLevel) {
		String formattedMessage = Formatter.format(levels.get(logLevel), message);
		this.log(formattedMessage);
	}

	public void error(@Nonnull Throwable throwable) {	
		log("Cause of Exception: " + throwable.getCause(), 1);
	}

	public void error(@Nonnull String message) {
		log(message, 1);
	}

	public void warn(@Nonnull String message) {
		log(message, 2);
	}

	public void info(@Nonnull String message) {
		log(message, 3);
	}

	public void debug(@Nonnull String message, @Nullable Object... args) {
		if(args != null || args.length != 0) {
			message = String.format(message, args);
		}

		log(message, 4);
	}

	public void debug(@Nonnull Object object) {
		log(this.toString(object), 4);
	}

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

		return "Couldn't find a correct type for the object";
	}
}
