package org.rexellentgames.dungeon.entity.item.consumable.plant;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.item.Item;

public class Plant extends Item {
	protected float added;

	{
		stackable = true;
	}

	@Override
	public void render(float x, float y, boolean flipped) {
		if (this.added != 0) {
			float angle = (flipped ? this.added : -this.added);

			Graphics.render(this.region, x + (flipped ? -3 : 3), y - 4, angle, 8, 10, false,
				false);
		}
	}
}