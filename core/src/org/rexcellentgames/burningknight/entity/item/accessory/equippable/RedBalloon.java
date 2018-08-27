package org.rexcellentgames.burningknight.entity.item.accessory.equippable;

public class RedBalloon extends Equippable {
	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.flight += 1;
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.flight -= 1;
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}
}