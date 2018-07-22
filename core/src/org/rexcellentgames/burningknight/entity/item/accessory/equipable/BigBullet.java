package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

public class BigBullet extends Equipable {
	{
		sprite = "item-large_bullet";
	}

	@Override
	public void onEquip() {
		super.onEquip();
		this.owner.modifyStat("gun_use_time", -0.5f);
		this.owner.modifyStat("damage", 0.3f);
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		this.owner.modifyStat("gun_use_time", 0.5f);
		this.owner.modifyStat("damage", -0.3f);
	}
}