package org.rexcellentgames.burningknight.entity.item.accessory.equippable;

public class Scissors extends Equippable {
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