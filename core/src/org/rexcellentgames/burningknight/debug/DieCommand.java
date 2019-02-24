package org.rexcellentgames.burningknight.debug;

import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class DieCommand extends ConsoleCommand {
	{
		name = "/kill";
		shortName = "/k";
	}

	@Override
	public void run(Console console, String[] args) {
		if (Player.instance != null) {
			Player.instance.die();
		}
	}
}