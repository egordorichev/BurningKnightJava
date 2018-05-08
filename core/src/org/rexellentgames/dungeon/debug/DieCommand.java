package org.rexellentgames.dungeon.debug;

import org.rexellentgames.dungeon.entity.creature.player.Player;

public class DieCommand extends ConsoleCommand {
	{
		shortName = "/de";
		name = "/die";
		description = "Death";
	}

	@Override
	public void run(Console console, String[] args) {
		if (Player.instance != null) {
			Player.instance.die();
		}
	}
}