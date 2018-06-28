package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

public class LuckyBullet extends Equipable {
	@Override
	public void onEquip() {
		super.onEquip();
		this.owner.modifyStat("ammo_save_chance", 0.1f);
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		this.owner.modifyStat("ammo_save_chance", -0.1f);
	}
}