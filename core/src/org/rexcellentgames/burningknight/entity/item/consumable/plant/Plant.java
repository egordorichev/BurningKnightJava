package org.rexcellentgames.burningknight.entity.item.consumable.plant;

import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.item.Item;

public class Plant extends Item {
	protected float added;

	{
		stackable = true;
	}

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		if (this.added != 0) {
			float angle = (flipped ? this.added : -this.added);

			Graphics.render(this.getSprite(), x + (flipped ? -w / 4 : w / 4) + w / 2, y + h / 3, angle, 8, 10, false,
				false);
		}
	}
}