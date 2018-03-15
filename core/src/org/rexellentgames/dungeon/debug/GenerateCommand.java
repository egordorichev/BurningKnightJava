package org.rexellentgames.dungeon.debug;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.creature.mob.BurningKnight;
import org.rexellentgames.dungeon.entity.creature.player.Player;

public class GenerateCommand extends ConsoleCommand {
	{
		name = "/reset";
		shortName = "/rst";
		description = "Generates a new game and player (might freeze)";
	}

	@Override
	public void run(Console console, String[] args) {
		Dungeon.reset = true;
		Player.instance = null;
		BurningKnight.instance = null;
		Dungeon.level = null;
		Dungeon.area.destroy();
		Dungeon.goToLevel(0);
	}
}