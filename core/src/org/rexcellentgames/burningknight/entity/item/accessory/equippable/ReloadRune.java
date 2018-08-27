package org.rexcellentgames.burningknight.entity.item.accessory.equippable;

public class ReloadRune extends Equippable {
	{
		sprite = "item-scroll_a";
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.modifyStat("reload_time", 1f);
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.modifyStat("reload_time", -1f);
	}
}