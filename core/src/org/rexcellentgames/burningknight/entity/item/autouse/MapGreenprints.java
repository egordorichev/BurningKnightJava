package org.rexcellentgames.burningknight.entity.item.autouse;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Locale;

public class MapGreenprints extends Autouse {
	{
		name = Locale.get("map_greenprints");
		description = Locale.get("map_greenprints_desc");
		sprite = "item-greenprint";
	}

	@Override
	public void use() {
		super.use();
		this.setCount(count - 1);
		Dungeon.level.exploreRandom();
	}
}