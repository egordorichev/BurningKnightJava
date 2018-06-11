package org.rexellentgames.dungeon.entity.item.weapon.modifier;

import com.badlogic.gdx.graphics.Color;
import org.rexellentgames.dungeon.entity.item.weapon.WeaponBase;

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
		weapon.setCritChance(20);
	}

	@Override
	public void remove(WeaponBase weapon) {
		super.remove(weapon);
		weapon.resetCritChance();
	}
}