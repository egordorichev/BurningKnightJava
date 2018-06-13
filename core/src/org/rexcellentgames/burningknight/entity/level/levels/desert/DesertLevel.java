package org.rexcellentgames.burningknight.entity.level.levels.desert;

import org.rexcellentgames.burningknight.entity.level.RegularLevel;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.HallPainter;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.RegularLevel;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.HallPainter;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;

public class DesertLevel extends RegularLevel {
	public DesertLevel() {
		Terrain.loadTextures(1);
		this.uid = 1;
	}

	@Override
	public String getName() {
		return "Desert ruins";
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