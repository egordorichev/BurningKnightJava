package org.rexellentgames.dungeon.entity.item.weapon.modifier;

import org.rexellentgames.dungeon.entity.item.weapon.WeaponBase;

public class FastModifier extends Modifier {
	public FastModifier() {
		super("Fast");
	}

	@Override
	public void apply(WeaponBase weapon) {
		super.apply(weapon);
		weapon.modifyUseTime(-0.1f);
	}

	@Override
	public void remove(WeaponBase weapon) {
		super.remove(weapon);
		weapon.modifyUseTime(0.1f);
	}
}