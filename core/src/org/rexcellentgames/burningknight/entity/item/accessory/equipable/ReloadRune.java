package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

public class ReloadRune extends Equipable {
	{
		sprite = "item-scroll_a";
	}

	@Override
	public void onEquip() {
		super.onEquip();
		this.owner.modifyStat("reload_time", 1f);
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		this.owner.modifyStat("reload_time", -1f);
	}
}