package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

public class Scissors extends Equipable {
	@Override
	public void onEquip() {
		super.onEquip();
		this.owner.cutCobweb = true;
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		this.owner.cutCobweb = false;
	}
}