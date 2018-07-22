package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

public class SmartBullet extends Equipable {
	@Override
	public void onEquip() {
		super.onEquip();
		this.owner.modifyStat("ammo_restore_chance_on_lost", 0.2f);
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		this.owner.modifyStat("ammo_restore_chance_on_lost", -0.2f);
	}
}