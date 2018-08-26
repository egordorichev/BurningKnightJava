package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

public class FireBoots extends Equipable {
	@Override
	public void onEquip() {
		super.onEquip();
		this.owner.burnLevel += this.level;
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		this.owner.burnLevel -= this.level;
	}
}