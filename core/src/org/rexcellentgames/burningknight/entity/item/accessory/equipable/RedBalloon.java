package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

public class RedBalloon extends Equipable {
	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.flying = true;
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.flying = false;
	}
}