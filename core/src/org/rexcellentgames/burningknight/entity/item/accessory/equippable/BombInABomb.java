package org.rexcellentgames.burningknight.entity.item.accessory.equippable;

public class BombInABomb extends Equippable {
	{
		sprite = "item-multi_bomb";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.leaveSmall = true;
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.leaveSmall = false;
	}

	@Override
	public boolean canBeDegraded() {
		return false;
	}

	@Override
	public boolean canBeUpgraded() {
		return false;
	}
}