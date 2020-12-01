package xyz.binfish.logger;

class Level {

	private String name;
	private int order;

	public Level(String levelName, int levelOrder) {
		this.name = levelName.toUpperCase();
		this.order = levelOrder;
	}

	public String getName() {
		return this.name;
	}

	public int getOrder() {
		return this.order;
	}
}
