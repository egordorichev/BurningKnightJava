package org.rexcellentgames.burningknight.entity.item.accessory.equippable;

public class SmartBullet extends Equippable {
	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.modifyStat("ammo_restore_chance_on_lost", 0.2f);
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.modifyStat("ammo_restore_chance_on_lost", -0.2f);
	}
}