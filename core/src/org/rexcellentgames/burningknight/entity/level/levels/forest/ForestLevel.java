package org.rexcellentgames.burningknight.entity.level.levels.forest;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.level.RegularLevel;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.HallPainter;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;

public class ForestLevel extends RegularLevel {
	public ForestLevel() {
		Terrain.loadTextures(5);

		this.uid = 5;
	}

	@Override
	public String getName() {
		return Locale.get("forest_ruins");
	}

	@Override
	public String getMusic() {
		return Dungeon.depth == 0 ? "Gobbeon" : "Botanical Expedition";
	}

	@Override
	protected Painter getPainter() {
		return new HallPainter().setGrass(0.45f).setWater(0.45f);
	}
}