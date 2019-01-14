package org.rexcellentgames.burningknight.entity.level.levels.ice;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.level.RegularLevel;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.HallPainter;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;

public class IceLevel extends RegularLevel {
	public IceLevel() {
		Terrain.loadTextures(7);
		this.uid = 7;
	}

	@Override
	public String getName() {
		return Locale.get("frozen_ruins");
	}

	@Override
	public String getMusic() {
		return Dungeon.depth == 0 ? "Gobbeon" : "Frozen to the bones";
	}

	@Override
	protected Painter getPainter() {
		return new HallPainter().setGrass(0f).setWater(0.4f).setCobweb(0);
	}

	@Override
	protected int getNumConnectionRooms() {
		return 0;
	}
}