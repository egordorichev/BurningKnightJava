package org.rexcellentgames.burningknight.debug;

import org.rexcellentgames.burningknight.entity.level.Level;

public class PassableCommand extends ConsoleCommand {
	{
		name = "/pas";
		shortName = "/p";
	}

	@Override
	public void run(Console console, String[] args) {
		Level.RENDER_PASSABLE = !Level.RENDER_PASSABLE;
	}
}