package org.rexcellentgames.burningknight.entity.item.accessory.equippable;

public class OldManual extends Equippable {
	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.modifyStat("fire_on_reload", 1f);
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.modifyStat("fire_on_reload", -1f);
	}
}