package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

public class IceSkates extends Equipable {
	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.iceResitant += 1;
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.iceResitant -= 1;
	}
}