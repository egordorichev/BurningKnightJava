package org.rexcellentgames.burningknight.entity.item.accessory.equippable;

public class TimeBullet extends Equippable {
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