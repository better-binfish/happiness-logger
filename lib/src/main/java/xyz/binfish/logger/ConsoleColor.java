package xyz.binfish.logger;

import javax.annotation.Nonnull;
import java.util.regex.Matcher;

public enum ConsoleColor {

	RESET("\u001B[0m", "reset"),
	BLACK("\u001B[30m", "black"),
	RED("\u001B[31m", "red"),
	GREEN("\u001B[32m", "green"),
	YELLOW("\u001B[33m", "yellow"),
	BLUE("\u001B[34m", "blue"),
	PURPLE("\u001B[35m", "purple"),
	CYAN("\u001B[36m", "cyan"),
	WHITE("\u001B[37m", "white");

	private final String color;
	private final String format;

	ConsoleColor(String color, String format) {
		this.color = color;
		this.format = format;
	}

	public static String format(String string) {
		if(string == null) {
			return null;
		}

		boolean useColors = Logger.getConfig().useColors();

		for(ConsoleColor color : values()) {
			string = string.replaceAll(
					Matcher.quoteReplacement("%" + color.getFormat()), useColors ? color.getColor() : ""
			);
		}

		return string;
	}

	public String getColor() {
		return color;
	}

	public String getFormat() {
		return format;
	}

	@Override
	public String toString() {
		return getColor();
	}
}
