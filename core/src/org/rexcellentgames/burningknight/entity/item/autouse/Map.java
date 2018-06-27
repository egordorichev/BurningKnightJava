package org.rexcellentgames.burningknight.entity.item.autouse;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Locale;

public class Map extends Autouse {
	{
	}

	@Override
	public void use() {
		super.use();
		this.setCount(count - 1);
		Dungeon.level.exploreAll();
	}
}