package org.rexellentgames.dungeon.entity.item.autouse;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Locale;

public class Map extends Autouse {
	{
		name = Locale.get("map");
		description = Locale.get("map_desc");
		sprite = "item-map";
	}

	@Override
	public void use() {
		super.use();
		this.setCount(count - 1);
		Dungeon.level.exploreAll();
	}
}