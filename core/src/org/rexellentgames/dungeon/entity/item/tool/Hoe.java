package org.rexellentgames.dungeon.entity.item.tool;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.Terrain;

public class Hoe extends Tool {
	{
		name = "Hoe";
		description = "Old and rusty hoe.";
		sprite = 7;
		damage = 2;
	}

	@Override
	public void endUse() {
		super.endUse();

		int x = Math.round(this.owner.x / 16) + (this.owner.isFlipped() ? -1 : 1);
		int y = Math.round(this.owner.y / 16);

		if (Dungeon.level.get(x, y) == Terrain.DIRT) {
			Dungeon.level.set(x, y, Terrain.GOOD_DIRT);
		}
	}
}