package org.rexcellentgames.burningknight.entity.item.weapon.modifier;

import com.badlogic.gdx.graphics.Color;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;

public class StrongModifier extends Modifier {
	public StrongModifier() {
		super("Strong");
	}

	private static Color color = Color.valueOf("#389770");

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void apply(WeaponBase weapon) {
		super.apply(weapon);
		weapon.modifyDamage((int) Math.max(1, Math.ceil(((float) weapon.damage) / 4 * 3)));
	}

	@Override
	public void remove(WeaponBase weapon) {
		super.remove(weapon);
		weapon.restoreDamage();
	}

	@Override
	public void apply(StringBuilder builder) {
		super.apply(builder);

		builder.append("[green]+25% damage[gray]");
	}
}