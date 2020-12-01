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

import xyz.binfish.logger.Level;
import xyz.binfish.logger.Formatter;

public class Logger {

	private static Logger instance;
	private static PrintStream writer;

	private static Config config;

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

	public static Logger createLogger(Config modifiConfig) {
		if(isInitialized) {
			throw new ExceptionInInitializerError("The logger has already been initialized");
		}

		if(modifiConfig == null) {
			config = new Config();
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
			if(item.getName() == newLevel.toUpperCase()) {
				break;
			}
			index++;
		}

		this.globalLevel = levels.get(index);
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

				if((int) (diff / (60 * 1000)) > config.getCleaningInterval()) {
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
		// FIXME i don't know гыг
		if(globalLevel.getOrder() != 0) {
			message = "[" + globalLevel.getName() + "] " + message;
		}

		System.out.printf("%s%n", message);
		if(canWrite) write(message);
	}

	public void log(String message, int logLevel) {
		String formattedMessage = Formatter.format(levels.get(logLevel), message);
		this.log(formattedMessage);
	}

	public void error(String message) {
		log(message, 1);
	}

	public void warn(String message) {
		log(message, 2);
	}

	public void info(String message) {
		log(message, 3);
	}

	public void debug(String message) {
		log(message, 4);
	}
}
