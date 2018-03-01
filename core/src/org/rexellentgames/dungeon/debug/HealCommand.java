package org.rexellentgames.dungeon.debug;

import org.rexellentgames.dungeon.entity.creature.player.Player;

public class HealCommand extends ConsoleCommand {
	{
		name = "/heal";
		shortName = "/hl";
		description = "heals you";
	}

	@Override
	public void run(Console console, String[] args) {
		Player.instance.modifyHp(Player.instance.getHpMax() - Player.instance.getHp());
	}
}