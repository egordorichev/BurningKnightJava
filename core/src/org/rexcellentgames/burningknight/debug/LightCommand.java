package org.rexcellentgames.burningknight.debug;

import org.rexcellentgames.burningknight.entity.level.LightLevel;

public class LightCommand extends ConsoleCommand {
	{
		name = "/light";
		shortName = "/lt";
	}

	@Override
	public void run(Console console, String[] args) {
		LightLevel.LIGHT = !LightLevel.LIGHT;
	}
}