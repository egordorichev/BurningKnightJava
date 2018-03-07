package org.rexellentgames.dungeon.entity.level;

import org.rexellentgames.dungeon.entity.level.builders.Builder;
import org.rexellentgames.dungeon.entity.level.builders.LoopBuilder;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.entity.level.painters.StoragePainter;

public class StorageLevel extends BetterLevel {
	@Override
	protected Painter getPainter() {
		return new StoragePainter().setGrass(0.4f).setWater(0.3f);
	}

	@Override
	protected Builder getBuilder() {
		return new LoopBuilder().setPathLength(0.3f, new float[]{1,2,3});
	}

	@Override
	protected int getNumRegularRooms() {
		return 15;
	}
}