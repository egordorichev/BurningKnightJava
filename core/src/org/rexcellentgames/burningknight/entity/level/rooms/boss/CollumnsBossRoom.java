package org.rexcellentgames.burningknight.entity.level.rooms.boss;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Rect;

public class CollumnsBossRoom extends BossRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		int minDim = Math.min(getWidth(), getHeight());
		boolean circ = Random.chance(50);
		byte tile = Random.chance(50) ? Terrain.WALL : (Random.chance(60) ? Terrain.CHASM : Terrain.LAVA);

		int pillarInset = minDim >= 12 ? 2 : 1;
		int pillarSize = (minDim - 6)/(pillarInset + 1);

		float xSpaces = getWidth() - 2*pillarInset - pillarSize - 2;
		float ySpaces = getHeight() - 2*pillarInset - pillarSize - 2;
		float minSpaces = Math.min(xSpaces, ySpaces);

		float percentSkew = Math.round(Random.newFloat() * minSpaces) / minSpaces;

		if (circ) {
			Painter.fillEllipse(level, left + 1 + pillarInset + Math.round(percentSkew*xSpaces), top + 1 + pillarInset, pillarSize, pillarSize, tile);
			Painter.fillEllipse(level, right - pillarSize - pillarInset, top + 1 + pillarInset + Math.round(percentSkew*ySpaces), pillarSize, pillarSize, tile);
			Painter.fillEllipse(level, right - pillarSize - pillarInset - Math.round(percentSkew*xSpaces), bottom - pillarSize - pillarInset, pillarSize, pillarSize, tile);
			Painter.fillEllipse(level, left + 1 + pillarInset, bottom - pillarSize - pillarInset - Math.round(percentSkew*ySpaces), pillarSize, pillarSize, tile);

			if (Random.chance(50)) {
				tile = Random.chance(50) ? Terrain.WALL : (Random.chance(60) ? Terrain.CHASM : Terrain.LAVA);

				Painter.fillEllipse(level, Rect.make(left + 1 + pillarInset + Math.round(percentSkew*xSpaces), top + 1 + pillarInset, pillarSize, pillarSize), 1, tile);
				Painter.fillEllipse(level, Rect.make(right - pillarSize - pillarInset, top + 1 + pillarInset + Math.round(percentSkew*ySpaces), pillarSize, pillarSize), 1, tile);
				Painter.fillEllipse(level, Rect.make(right - pillarSize - pillarInset - Math.round(percentSkew*xSpaces), bottom - pillarSize - pillarInset, pillarSize, pillarSize), 1, tile);
				Painter.fillEllipse(level, Rect.make(left + 1 + pillarInset, bottom - pillarSize - pillarInset - Math.round(percentSkew*ySpaces), pillarSize, pillarSize), 1, tile);
			}
		} else {
			Painter.fill(level, left + 1 + pillarInset + Math.round(percentSkew*xSpaces), top + 1 + pillarInset, pillarSize, pillarSize, tile);
			Painter.fill(level, right - pillarSize - pillarInset, top + 1 + pillarInset + Math.round(percentSkew*ySpaces), pillarSize, pillarSize, tile);
			Painter.fill(level, right - pillarSize - pillarInset - Math.round(percentSkew*xSpaces), bottom - pillarSize - pillarInset, pillarSize, pillarSize, tile);
			Painter.fill(level, left + 1 + pillarInset, bottom - pillarSize - pillarInset - Math.round(percentSkew*ySpaces), pillarSize, pillarSize, tile);

			if (Random.chance(50)) {
				tile = Random.chance(50) ? Terrain.WALL : (Random.chance(60) ? Terrain.CHASM : Terrain.LAVA);

				Painter.fill(level, Rect.make(left + 1 + pillarInset + Math.round(percentSkew*xSpaces), top + 1 + pillarInset, pillarSize, pillarSize), 1, tile);
				Painter.fill(level, Rect.make(right - pillarSize - pillarInset, top + 1 + pillarInset + Math.round(percentSkew*ySpaces), pillarSize, pillarSize), 1, tile);
				Painter.fill(level, Rect.make(right - pillarSize - pillarInset - Math.round(percentSkew*xSpaces), bottom - pillarSize - pillarInset, pillarSize, pillarSize), 1, tile);
				Painter.fill(level, Rect.make(left + 1 + pillarInset, bottom - pillarSize - pillarInset - Math.round(percentSkew*ySpaces), pillarSize, pillarSize), 1, tile);
			}
		}
	}
}