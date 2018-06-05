package org.rexellentgames.dungeon.entity.level.levels.desert;

import org.rexellentgames.dungeon.entity.level.RegularLevel;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.painters.HallPainter;
import org.rexellentgames.dungeon.entity.level.painters.Painter;

public class DesertLevel extends RegularLevel {
	public DesertLevel() {
		Terrain.loadTextures(1);
		this.uid = 1;
	}

	@Override
	public String getMusic() {
		return "Believer";
	}

	@Override
	protected Painter getPainter() {
		return new HallPainter().setGrass(0.45f).setWater(0.3f);
	}

	@Override
	protected int getNumConnectionRooms() {
		return 0;
	}
}