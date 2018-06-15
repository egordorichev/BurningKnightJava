package org.rexcellentgames.burningknight.entity.item.weapon.modifier;

import com.badlogic.gdx.graphics.Color;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;

public class NoChanceModifier extends Modifier {
	public NoChanceModifier() {
		super("Boring");
	}

	private static Color color = Color.valueOf("#ff00ff");

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void apply(WeaponBase weapon) {
		super.apply(weapon);
		weapon.setCritChance(0);
	}

	@Override
	public void remove(WeaponBase weapon) {
		super.remove(weapon);
		weapon.resetCritChance();
	}
}