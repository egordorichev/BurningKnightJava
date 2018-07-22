package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

public class RedBalloon extends Equipable {
	@Override
	public void onEquip() {
		super.onEquip();
		this.owner.flying = true;
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		this.owner.flying = false;
	}
}