package xyz.binfish.logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Formatter {

	private static String timePattern;

	public static String format(Level level, String message) {
		StringBuilder formattedMessage = new StringBuilder();

		message = ConsoleColor.format(message);

		if(level.getOrder() != 0) {
			formattedMessage
				.append(String.format("[%s] ", level.getName().toUpperCase()));

			if(Logger.getConfig().showThread()) {
				String threadName = Logger.getThreadName();

				if(threadName != null) {
					formattedMessage.append(threadName + "/");
				}
			}

			formattedMessage
				.append(DateTimeFormatter.ofPattern(timePattern).format(LocalDateTime.now()))
				.append(String.format(" :  %s", message));
		} else {
			formattedMessage.append(message);
		}

		return formattedMessage.toString();
	}

	public static void setTimePattern(String pattern) {
		timePattern = pattern;
	}

	public static String getStackTraceString(Throwable t) {
		if(t == null) {
			return "";
		}

		StringBuilder str = new StringBuilder();
		StackTraceElement[] stktrace = t.getStackTrace();

		for(int i = 0; i < stktrace.length; i++) {
			str.append("\t" + stktrace[i].toString() + "\n");
		}

		return str.toString();
	}
}
