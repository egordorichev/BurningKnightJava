package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

public class OldManual extends Equipable {
	@Override
	public void onEquip() {
		super.onEquip();
		this.owner.modifyStat("fire_on_reload", 1f);
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		this.owner.modifyStat("fire_on_reload", -1f);
	}
}