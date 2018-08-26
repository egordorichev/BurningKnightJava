package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

public class Scissors extends Equipable {
	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.cutCobweb = true;
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.cutCobweb = false;
	}
}