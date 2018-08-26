package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

public class TimeBullet extends Equipable {
	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.modifyStat("slow_down_on_hit", 1f);
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.modifyStat("slow_down_on_hit", -1f);
	}
}