package org.rexcellentgames.burningknight.entity.item.autouse;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.item.consumable.Consumable;
import org.rexcellentgames.burningknight.ui.UiBanner;

public class Autouse extends Consumable {
	{
		useOnPickup = true;
	}

	@Override
	public void use() {
		super.use();

		UiBanner banner = new UiBanner();
		banner.text = this.name;
		banner.extra = this.description;
		Dungeon.area.add(banner);
	}
}