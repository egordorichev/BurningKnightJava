package org.rexellentgames.dungeon.entity.creature.mob.prefix;

import com.badlogic.gdx.graphics.Color;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;

public class MoreDefensePrefix extends Prefix {
	private static Color color = Color.valueOf("#444b7e");

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void apply(Mob mob) {
		super.apply(mob);
		mob.modifyDefense(+2);
	}
}