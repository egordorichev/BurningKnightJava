package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

public class FireBoots extends Equipable {
	@Override
	public void onEquip() {
		super.onEquip();
		this.owner.burnLevel += this.level;

		if (this.level >= 2) {
			this.owner.fireResist += 1;
		}

		if (this.level >= 3) {
			this.owner.lavaResist += 1;
		}
	}

	@Override
	public int getMaxLevel() {
		return 3;
	}

	@Override
	public void onUnequip() {
		super.onUnequip();
		this.owner.burnLevel -= this.level;

		if (this.level >= 2) {
			this.owner.fireResist -= 1;
		}

		if (this.level >= 3) {
			this.owner.lavaResist -= 1;
		}
	}
}