package org.rexellentgames.dungeon.entity.level.levels;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.level.BetterLevel;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.builders.Builder;
import org.rexellentgames.dungeon.entity.level.builders.LoopBuilder;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.entity.level.painters.PrisonPainter;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.util.Random;

public class PrisonLevel extends BetterLevel {
	public PrisonLevel() {
		if (!Network.SERVER) {
			Terrain.loadTextures(2);
		}
	}

	@Override
	protected Builder getBuilder() {
		return new LoopBuilder().setShape(2,
			Random.newFloat(0.4f, 0.7f),
			Random.newFloat(0f, 0.5f)).setPathLength(0.3f, new float[]{1,1,1});
	}

	@Override
	protected int getNumRegularRooms() {
		return 10;
	}

	@Override
	protected Painter getPainter() {
		return new PrisonPainter();
	}
}