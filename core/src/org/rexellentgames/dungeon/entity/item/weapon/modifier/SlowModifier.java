package org.rexellentgames.dungeon.entity.item.weapon.modifier;

import org.rexellentgames.dungeon.entity.item.weapon.WeaponBase;

public class SlowModifier extends Modifier {
	public SlowModifier() {
		super("Slow");
	}

	@Override
	public void apply(WeaponBase weapon) {
		super.apply(weapon);
		weapon.modifyUseTime(0.1f);
	}

	@Override
	public void remove(WeaponBase weapon) {
		super.remove(weapon);
		weapon.modifyUseTime(-0.1f);
	}
}