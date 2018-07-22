package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

public class TimeBullet extends Equipable {
	@Override
	public void onEquip() {
		super.onEquip();
		this.owner.modifyStat("slow_down_on_hit", 1f);
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		this.owner.modifyStat("slow_down_on_hit", -1f);
	}
}