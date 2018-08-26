package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

public class IceBoots extends Equipable {
	{
		sprite = "item-frost_boots";
	}

	@Override
	public void onEquip() {
		super.onEquip();
		this.owner.frostLevel += this.level;
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		this.owner.frostLevel -= this.level;
	}
}