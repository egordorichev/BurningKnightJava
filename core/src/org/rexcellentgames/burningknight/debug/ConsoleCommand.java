package org.rexcellentgames.burningknight.debug;

public abstract class ConsoleCommand {
	public String name;
	public String shortName;

	public abstract void run(Console console, String[] args);
}