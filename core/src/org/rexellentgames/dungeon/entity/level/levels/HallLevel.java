package org.rexellentgames.dungeon.entity.level.levels;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.RegularLevel;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.builders.Builder;
import org.rexellentgames.dungeon.entity.level.builders.CastleBuilder;
import org.rexellentgames.dungeon.entity.level.builders.LineBuilder;
import org.rexellentgames.dungeon.entity.level.builders.LoopBuilder;
import org.rexellentgames.dungeon.entity.level.painters.HallPainter;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.Random;

public class HallLevel extends RegularLevel {
	public HallLevel() {
		Terrain.loadTextures(0);

		this.addLight = Dungeon.depth == 0;
	}

	@Override
	protected Painter getPainter() {
		return new HallPainter().setGrass(0.45f).setWater(0.45f);
	}

	@Override
	protected Builder getBuilder() {
		if (Dungeon.depth == 0) {
			return new LineBuilder();
		} else {
			switch (Random.newInt(5)) {
				case 0: case 3: default: return new CastleBuilder();
				case 1: return new LineBuilder();
				case 2: case 4: return new LoopBuilder().setShape(2,
					Random.newFloat(0.4f, 0.7f),
					Random.newFloat(0f, 0.5f)).setPathLength(0.3f, new float[]{1,1,1});
			}
		}
	}

	@Override
	protected int getNumRegularRooms() {
		return Dungeon.depth == 0 ? 0 : Random.newInt((int) (Dungeon.depth * 1.4f + 2f), (int) (Dungeon.depth * 2.5f + 3));
	}

	@Override
	protected int getNumConnectionRooms() {
		return 0;
	}
}