package org.rexellentgames.dungeon.debug;

import org.rexellentgames.dungeon.game.state.InGameState;

public class LightCommand extends ConsoleCommand {
	{
		name = "/light";
		shortName = "/lt";
		description = "toggles light";
	}

	@Override
	public void run(Console console, String[] args) {
		InGameState.LIGHT = !InGameState.LIGHT;
	}
}