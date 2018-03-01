package org.rexellentgames.dungeon.debug;

public class ConsoleCommand {
	protected String name;
	protected String shortName;
	protected String description;

	public void run(Console console, String[] args) {

	}

	public String getName() {
		return this.name;
	}

	public String getShortName() {
		return this.shortName;
	}

	public String getDescription() {
		return this.description;
	}
}