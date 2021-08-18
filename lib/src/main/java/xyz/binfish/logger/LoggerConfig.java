package xyz.binfish.logger;

public class LoggerConfig {

	private String logDirectory = null;
	private String formatTimePattern = "dd-MM-yyyy HH:mm:ss";

	private boolean autoCleaning = false;
	private boolean notifyDeleteFiles = false;
	private boolean showThread = false;
	private boolean useColors = false;
	private int minimumAgeInMinutes = 0;

	public LoggerConfig() { }

	public LoggerConfig(String path) {
		this.logDirectory = path;
	}

	public LoggerConfig setDirectory(String path) {
		this.logDirectory = path;

		return this;
	}

	public String getDirectory() {
		return logDirectory;
	}

	public boolean hasDirectory() {
		return logDirectory != null;
	}

	public LoggerConfig setFormatTimePattern(String pattern) {
		this.formatTimePattern = pattern;

		return this;
	}

	public String getFormatTimePattern() {
		return formatTimePattern;
	}

	public LoggerConfig setAutoCleaning(boolean value) {
		this.autoCleaning = value;

		return this;
	}

	public boolean isAutoCleaning() {
		return autoCleaning;
	}

	public LoggerConfig setNotifyDeleteFiles(boolean value) {
		this.notifyDeleteFiles = value;

		return this;
	}

	public boolean notifyDeleteFiles() {
		return notifyDeleteFiles;
	}

	public LoggerConfig setShowThread(boolean value) {
		this.showThread = value;

		return this;
	}

	public boolean showThread() {
		return showThread;
	}

	public LoggerConfig setUseColors(boolean value) {
		this.useColors = value;

		return this;
	}

	public boolean useColors() {
		return useColors;
	}

	public LoggerConfig setMinimumAgeInMinutes(int min) {
		this.minimumAgeInMinutes = min;

		return this;
	}

	public int getMinimumAgeInMinutes() {
		return minimumAgeInMinutes;
	}
}
