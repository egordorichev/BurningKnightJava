package org.rexcellentgames.burningknight.entity.fx;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;

public class Waterfall extends Entity {
	{
		depth = -1;
	}

	@Override
	public void render() {
		Graphics.startAlphaShape();

		for (int i = 0; i < 16; i++) {
			Graphics.shape.setColor(0, 0.6f, 1, 0.4f);

			float h = (float) (8 + Math.sin(i * 16f + Dungeon.time * 2f) * 4);
			Graphics.shape.rect(x + i, y - h, 1, h);
		}

		Graphics.endAlphaShape();
	}
}