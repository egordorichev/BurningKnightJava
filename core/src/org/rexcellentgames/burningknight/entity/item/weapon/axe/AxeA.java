package org.rexcellentgames.burningknight.entity.item.weapon.axe;

import org.rexcellentgames.burningknight.assets.Locale;

public class AxeA extends Axe {
	{
		name = Locale.get("axe_a");
		description = Locale.get("axe_a_desc");
		damage = 3;
		penetrates = true;
		sprite = "item-axe_a";
		stackable = false;
	}

	@Override
	public void generate() {

	}

	@Override
	protected boolean canBeConsumed() {
		return false;
	}
}