package org.rexellentgames.dungeon.debug;

import org.rexellentgames.dungeon.entity.level.LightLevel;

public class LightCommand extends ConsoleCommand {
	{
		name = "/light";
		shortName = "/lt";
		description = "toggles light";
	}

	@Override
	public void run(Console console, String[] args) {
		LightLevel.LIGHT = !LightLevel.LIGHT;
	}
}