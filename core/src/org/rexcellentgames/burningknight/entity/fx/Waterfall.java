package org.rexcellentgames.burningknight.entity.fx;

import com.badlogic.gdx.Gdx;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.level.Terrain;

public class Waterfall extends Entity {
	{
		depth = -1;
	}

	private float al = 1;
	private boolean gone;
	public int i;

	@Override
	public void render() {
		Graphics.startAlphaShape();

		for (int i = 0; i < 16; i++) {
			Graphics.shape.setColor(0, 0.6f, 1, 0.4f * al);

			// fixme: better color/sin function
			float h = (float) (7 + Math.sin((x + i) * 0.25f + Dungeon.time * 2f) * 8 * Math.cos(Dungeon.time * 2f));
			Graphics.shape.rect(x + i, y - h + 2, 1, h);
		}

		Graphics.endAlphaShape();

		if (gone) {
			al -= Gdx.graphics.getDeltaTime() * 0.5f;
			if (al <= 0) {
				done = true;
			}
		}

		if (!gone && Dungeon.level.liquidData[i] != Terrain.WATER) {
			this.gone = true;
		}
	}
}