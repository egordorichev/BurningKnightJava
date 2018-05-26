package org.rexellentgames.dungeon.entity.item.weapon.modifier;

import org.rexellentgames.dungeon.entity.item.weapon.WeaponBase;

public class StrongModifier extends Modifier {
	public StrongModifier() {
		super("Strong");
	}

	@Override
	public void apply(WeaponBase weapon) {
		super.apply(weapon);
		weapon.modifyDamage(2);
	}

	@Override
	public void remove(WeaponBase weapon) {
		super.remove(weapon);
		weapon.modifyDamage(-2);
	}
}