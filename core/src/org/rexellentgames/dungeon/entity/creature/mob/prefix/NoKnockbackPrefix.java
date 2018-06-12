package org.rexellentgames.dungeon.entity.creature.mob.prefix;

import com.badlogic.gdx.graphics.Color;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;

public class NoKnockbackPrefix extends Prefix {
	private static Color color = Color.valueOf("#ffffff");

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void apply(Mob mob) {
		super.apply(mob);
		mob.knockbackMod = 0;
	}
}