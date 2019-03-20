package org.rexcellentgames.burningknight.debug;

import org.rexcellentgames.burningknight.physics.World;

public class DebugCommand extends ConsoleCommand {
	{
		name = "/debug";
		shortName = "/d";
	}

	@Override
	public void run(Console console, String[] args) {
		World.DRAW_DEBUG = !World.DRAW_DEBUG;
	}
}