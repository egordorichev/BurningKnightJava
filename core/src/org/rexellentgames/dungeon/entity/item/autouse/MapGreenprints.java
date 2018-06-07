package org.rexellentgames.dungeon.entity.item.autouse;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Locale;

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