package xyz.binfish.logger;

public class LoggerConfig {

	public static String logDirectory;
	public static String formatTimePattern = "dd-MM-yyyy HH:mm:ss";

	public static boolean autoCleaning = false;
	public static boolean notifyDeleteFiles = false;
	public static int minAgeInMinutes = 0;

	public LoggerConfig() { }

	public LoggerConfig(String logDirectory) {
		this.logDirectory = logDirectory;
	}

	public boolean isAutoCleaning() {
		return this.autoCleaning;
	}

	public int getCleaningMinAge() {
		return this.minAgeInMinutes;
	}
}
