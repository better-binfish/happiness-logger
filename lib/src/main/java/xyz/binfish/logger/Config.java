package xyz.binfish.logger;

public class Config {

	public static String logDirectory;
	public static String formatTimePattern = "dd-MM-yyyy HH:mm:ss";

	public static boolean autoCleaning = false;
	public static boolean notifyDeleteFiles = false;
	public static int cleaningMinuteInterval = 0;

	public Config() { }

	public Config(String logDirectory) {
		this.logDirectory = logDirectory;
	}

	public boolean isAutoCleaning() {
		return this.autoCleaning;
	}

	public int getCleaningInterval() {
		return this.cleaningMinuteInterval;
	}
}
