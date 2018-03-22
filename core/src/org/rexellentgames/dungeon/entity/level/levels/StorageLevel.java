package org.rexellentgames.dungeon.entity.level.levels;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.level.BetterLevel;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.builders.Builder;
import org.rexellentgames.dungeon.entity.level.builders.CastleBuilder;
import org.rexellentgames.dungeon.entity.level.builders.LoopBuilder;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.entity.level.painters.StoragePainter;
import org.rexellentgames.dungeon.net.Network;

public class StorageLevel extends BetterLevel {
	public StorageLevel() {
		if (!Network.SERVER) {
			Terrain.loadTextures(1);
		}
	}

	@Override
	protected Painter getPainter() {
		return new StoragePainter().setGrass(0.4f).setWater(0.3f);
	}

	@Override
	protected Builder getBuilder() {
		return new CastleBuilder();
	}

	@Override
	protected int getNumRegularRooms() {
		return 10;
	}
}