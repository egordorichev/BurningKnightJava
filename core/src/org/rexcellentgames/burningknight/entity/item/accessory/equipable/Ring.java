package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

public class Ring extends Equipable {
	{
		sprite = "item-sonic_ring";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.modifyStat("get_damage_from_money", 1f);
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.modifyStat("get_damage_from_money", -1f);
	}
}