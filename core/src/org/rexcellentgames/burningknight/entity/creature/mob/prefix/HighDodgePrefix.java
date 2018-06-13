package org.rexcellentgames.burningknight.entity.creature.mob.prefix;

import com.badlogic.gdx.graphics.Color;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;

public class HighDodgePrefix extends Prefix {
	private static Color color = Color.valueOf("#00ff00");

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void apply(Mob mob) {
		super.apply(mob);
		mob.blockChance += 25f;
	}
}