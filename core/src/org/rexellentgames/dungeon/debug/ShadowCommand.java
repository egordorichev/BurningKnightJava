package org.rexellentgames.dungeon.debug;

import org.rexellentgames.dungeon.entity.level.Level;

public class ShadowCommand extends ConsoleCommand {
	{
		shortName = "/sd";
		name = "/shadow";
		description = "Toggles shadows";
	}

	@Override
	public void run(Console console, String[] args) {
		super.run(console, args);

		Level.SHADOWS = !Level.SHADOWS;
	}
}