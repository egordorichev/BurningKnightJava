package org.rexcellentgames.burningknight.entity.item.weapon.modifier;

import com.badlogic.gdx.graphics.Color;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;

public class CriticalModifier extends Modifier {
	public CriticalModifier() {
		super("Critical");
	}

	private static Color color = Color.valueOf("#ffff00");

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void apply(WeaponBase weapon) {
		super.apply(weapon);
		weapon.setCritChance(weapon.critChance + 20);
	}

	@Override
	public void remove(WeaponBase weapon) {
		super.remove(weapon);
		weapon.resetCritChance();
	}

	@Override
	public void apply(StringBuilder builder) {
		super.apply(builder);

		builder.append("[green]+20% crit chance[gray]");
	}
}