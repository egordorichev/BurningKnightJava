package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

public class ElementalRing extends Equipable {
	@Override
	public void onEquip() {
		super.onEquip();
		this.owner.lavaResist += 1;
		this.owner.poisonResist += 1;
		this.owner.stunResist += 1;
		this.owner.fireResist += 1;
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		this.owner.lavaResist -= 1;
		this.owner.poisonResist -= 1;
		this.owner.stunResist -= 1;
		this.owner.fireResist -= 1;
	}
}