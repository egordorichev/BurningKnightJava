package org.rexellentgames.dungeon.entity.item.consumable.spell;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.util.Random;

public class SpellOfTeleportation extends Spell {
	{
		name = "Spell Of Teleportation";
		description = "Teleports you to a random location";
	}

	@Override
	public void use() {
		super.use();

		int x;
		int y;

		do {
			x = Random.newInt(1, Level.getWIDTH() - 2);
			y = Random.newInt(1, Level.getHEIGHT() - 2);
		} while (!check(x, y));

		Player.instance.tp(x * 16, y * 16);
		Camera.instance.follow(Player.instance);
	}

	private static boolean check(int x, int y) {
		short t = Dungeon.level.get(x, y);
		return t > 5 && t < 14;
	}
}