package org.rexellentgames.dungeon.debug;

import org.rexellentgames.dungeon.game.state.InGameState;

public class DebugCommand extends ConsoleCommand {
	{
		shortName = "/d";
		name = "/debug";
		description = "Toggles physical debug";
	}

	@Override
	public void run(Console console, String[] args) {
		super.run(console, args);

		InGameState.DRAW_DEBUG = !InGameState.DRAW_DEBUG;
	}
}