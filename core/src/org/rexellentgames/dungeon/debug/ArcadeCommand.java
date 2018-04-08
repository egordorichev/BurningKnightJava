package org.rexellentgames.dungeon.debug;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.Level;

public class ArcadeCommand extends ConsoleCommand {
	{
		shortName = "/ar";
		name = "/arcade";
		description = "Toggles arcade mode";
	}

	@Override
	public void run(Console console, String[] args) {
		Level.ARCADE = !Level.ARCADE;
		Dungeon.newGame();
	}
}