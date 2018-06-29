package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

public class Ring extends Equipable {
	{
		sprite = "item-sonic_ring";
	}

	@Override
	public void onEquip() {
		super.onEquip();
		this.owner.modifyStat("get_damage_from_money", 1f);
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		this.owner.modifyStat("get_damage_from_money", -1f);
	}
}