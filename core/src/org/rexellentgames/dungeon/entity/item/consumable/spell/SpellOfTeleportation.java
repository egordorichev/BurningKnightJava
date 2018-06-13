package org.rexellentgames.dungeon.entity.item.consumable.spell;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Locale;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.util.Random;

public class SpellOfTeleportation extends Spell {
	{
		name = Locale.get("teleport_spell");
		description = Locale.get("teleport_spell_desc");
	}

	@Override
	public void use() {
		super.use();

		int x;
		int y;

		do {
			x = Random.newInt(1, Level.getWidth() - 2);
			y = Random.newInt(1, Level.getHeight() - 2);
		} while (!check(x, y));

		Player.instance.tp(x * 16, y * 16);
		Camera.follow(Player.instance);
	}

	private static boolean check(int x, int y) {
		return Dungeon.level.checkFor(x, y, Terrain.PASSABLE);
	}
}