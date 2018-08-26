package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

public class RngBullet extends Equipable {
	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.modifyStat("restore_ammo_chance", 0.1f);
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.modifyStat("restore_ammo_chance", -0.1f);
	}
}