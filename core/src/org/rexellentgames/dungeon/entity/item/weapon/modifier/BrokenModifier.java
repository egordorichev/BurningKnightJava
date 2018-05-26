package org.rexellentgames.dungeon.entity.item.weapon.modifier;

import org.rexellentgames.dungeon.entity.item.weapon.WeaponBase;

public class BrokenModifier extends Modifier {
	public BrokenModifier() {
		super("Broken");
	}

	@Override
	public void apply(WeaponBase weapon) {
		super.apply(weapon);
		weapon.modifyDamage(-1);
	}

	@Override
	public void remove(WeaponBase weapon) {
		super.remove(weapon);
		weapon.modifyDamage(1);
	}
}