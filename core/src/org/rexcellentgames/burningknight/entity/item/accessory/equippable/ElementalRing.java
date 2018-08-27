package org.rexcellentgames.burningknight.entity.item.accessory.equippable;

public class ElementalRing extends Equippable {
	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.lavaResist += 1;
		this.owner.poisonResist += 1;
		this.owner.stunResist += 1;
		this.owner.fireResist += 1;
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.lavaResist -= 1;
		this.owner.poisonResist -= 1;
		this.owner.stunResist -= 1;
		this.owner.fireResist -= 1;
	}
}