package org.rexellentgames.dungeon.debug;

import org.rexellentgames.dungeon.Dungeon;

public class GenerateCommand extends ConsoleCommand {
	{
		name = "/reset";
		shortName = "/rst";
		description = "Generates a new game and player (might freeze)";
	}

	@Override
	public void run(Console console, String[] args) {
		Dungeon.newGame();
	}
}