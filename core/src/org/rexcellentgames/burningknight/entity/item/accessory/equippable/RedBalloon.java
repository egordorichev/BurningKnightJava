package org.rexcellentgames.burningknight.entity.item.accessory.equippable;

public class RedBalloon extends Equippable {
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