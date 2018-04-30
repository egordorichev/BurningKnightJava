package org.rexellentgames.dungeon.debug;

import org.rexellentgames.dungeon.game.state.InGameState;
import org.rexellentgames.dungeon.physics.World;

public class DebugCommand extends ConsoleCommand {
	{
		shortName = "/d";
		name = "/debug";
		description = "Toggles physical debug";
	}

	@Override
	public void run(Console console, String[] args) {
		super.run(console, args);

		World.DRAW_DEBUG = !World.DRAW_DEBUG;
	}
}