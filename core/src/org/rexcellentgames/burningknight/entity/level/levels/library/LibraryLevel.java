package org.rexcellentgames.burningknight.entity.level.levels.library;

import org.rexcellentgames.burningknight.entity.level.RegularLevel;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.HallPainter;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;

public class LibraryLevel extends RegularLevel {
	public LibraryLevel() {
		Terrain.loadTextures(2);
		this.uid = 2;
	}

	@Override
	public String getName() {
		return "Ancient library";
	}

	@Override
	public String getMusic() {
		return "Believer";
	}

	@Override
	protected Painter getPainter() {
		return new HallPainter().setGrass(0.35f).setWater(0);
	}

	@Override
	protected int getNumConnectionRooms() {
		return 0;
	}
}