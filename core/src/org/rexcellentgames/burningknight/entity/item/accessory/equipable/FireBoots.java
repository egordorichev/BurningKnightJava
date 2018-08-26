package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.assets.Locale;

public class FireBoots extends Equipable {
	private static String lvl2 = Locale.get("fire_immunity");
	private static String lvl3 = Locale.get("lava_immunity");

	@Override
	public String getDescription() {
		String d = super.getDescription();

		if (this.level >= 2) {
			d += "\n" + lvl2;
		}

		if (this.level >= 3) {
			d += "\n" + lvl3;
		}

		return d;
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
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
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.burnLevel -= this.level;

		if (this.level >= 2) {
			this.owner.fireResist -= 1;
		}

		if (this.level >= 3) {
			this.owner.lavaResist -= 1;
		}
	}
}