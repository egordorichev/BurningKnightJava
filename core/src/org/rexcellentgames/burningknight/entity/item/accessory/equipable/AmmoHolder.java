package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

public class AmmoHolder extends Equipable {
	@Override
	public void onEquip() {
		super.onEquip();
		this.owner.modifyStat("ammo_capacity", 0.5f);
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		this.owner.modifyStat("ammo_capacity", -0.5f);
	}
}