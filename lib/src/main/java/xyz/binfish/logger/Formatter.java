package xyz.binfish.logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import xyz.binfish.logger.Level;

class Formatter {

	private static String timePattern;

	public static String format(Level level, String message) {
		return new String("[" + level.getName() + "] " + DateTimeFormatter.ofPattern(timePattern).format(LocalDateTime.now()) + " : " + message);
	}

	public static void setTimePattern(String pattern) {
		timePattern = pattern;
	}
}
