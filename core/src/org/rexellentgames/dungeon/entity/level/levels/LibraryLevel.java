package org.rexellentgames.dungeon.entity.level.levels;

import org.rexellentgames.dungeon.entity.level.RegularLevel;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.builders.Builder;
import org.rexellentgames.dungeon.entity.level.builders.LoopBuilder;
import org.rexellentgames.dungeon.entity.level.painters.LibraryPainter;
import org.rexellentgames.dungeon.entity.level.painters.Painter;

public class LibraryLevel extends RegularLevel {
	public LibraryLevel() {
		Terrain.loadTextures(3);
	}

	@Override
	protected Painter getPainter() {
		return new LibraryPainter();
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