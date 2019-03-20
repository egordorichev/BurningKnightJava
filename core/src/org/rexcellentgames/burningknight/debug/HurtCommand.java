package org.rexcellentgames.burningknight.debug;

import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class HurtCommand extends ConsoleCommand {
	{
		name = "/hurt";
		shortName = "/o";
	}

	@Override
	public void run(Console console, String[] args) {
		Player.instance.modifyHp(-1, null, true);
	}
}