package org.rexcellentgames.burningknight.entity.item.consumable.spell;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.util.Random;

public class SpellOfTeleportation extends Spell {
	{
		
		
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