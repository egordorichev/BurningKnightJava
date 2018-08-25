package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

public class IceSkates extends Equipable {
	@Override
	public void onEquip() {
		super.onEquip();
		this.owner.iceResitant = true;
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		this.owner.iceResitant = false;
	}
}