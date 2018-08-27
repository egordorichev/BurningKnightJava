package org.rexcellentgames.burningknight.entity.item.accessory.equippable;

import org.rexcellentgames.burningknight.assets.Locale;

public class IceBoots extends Equippable {
	{
		sprite = "item-frost_boots";
	}

	private static String lvl2 = Locale.get("ice_immunity");
	private static String lvl3 = Locale.get("lava_immunity");
	private static String lvl4 = Locale.get("lava_to_ice");

	@Override
	public String getDescription() {
		String d = super.getDescription();

		if (this.level >= 2) {
			d += "\n" + lvl2;
		}

		if (this.level >= 3) {
			d += "\n" + lvl3;
		}

		if (this.level >= 4) {
			d += "\n" + lvl4;
		}

		return d;
	}

	@Override
	public int getMaxLevel() {
		return 4;
	}

	@Override
	public void onEquip(boolean load) {
		super.onEquip(load);
		this.owner.frostLevel += this.level;

		if (this.level >= 2) {
			this.owner.iceResitant += 1;
		}

		if (this.level >= 3) {
			this.owner.lavaResist += 1;
		}
	}

	@Override
	public void onUnequip(boolean load) {
		super.onUnequip(load);
		this.owner.frostLevel -= this.level;

		if (this.level >= 2) {
			this.owner.iceResitant -= 1;
		}

		if (this.level >= 3) {
			this.owner.lavaResist -= 1;
		}
	}
}