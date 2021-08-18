package xyz.binfish.logger;

public enum Level {

	OFF("off", 0),
	ERROR("error", 1),
	WARN("warn", 2),
	INFO("info", 3),
	DEBUG("debug", 4);

	private final String name;
	private final int order;

	Level(String name, int order) {
		this.name = name;
		this.order = order;
	}

	public String getName() {
		return name;
	}

	public int getOrder() {
		return order;
	}

	public static Level getLevel(int order) {
		for(Level lvl : values()) {
			if(lvl.order == order) {
				return lvl;
			}
		}

		return null;
	}
}
