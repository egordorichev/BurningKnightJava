package org.rexcellentgames.burningknight.debug;

import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class HealCommand extends ConsoleCommand {
	{
		name = "/heal";
		shortName = "/h";
	}

	@Override
	public void run(Console console, String[] args) {
		Player.instance.modifyHp(Player.instance.getHpMax() - Player.instance.getHp(), null);
	}
}