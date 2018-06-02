package org.rexellentgames.dungeon.entity.level.levels.hall;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.RegularLevel;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.painters.HallPainter;
import org.rexellentgames.dungeon.entity.level.painters.Painter;

public class HallLevel extends RegularLevel {
	public HallLevel() {
		Terrain.loadTextures(0);

		this.addLight = Dungeon.depth == 0;
		this.uid = 0;
	}

	@Override
	protected Painter getPainter() {
		return new HallPainter().setGrass(0.45f).setWater(0.45f);
	}
}