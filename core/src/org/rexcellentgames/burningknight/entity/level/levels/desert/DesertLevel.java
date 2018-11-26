package org.rexcellentgames.burningknight.entity.level.levels.desert;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.level.Level;
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
	public boolean same(Level level) {
		return super.same(level) || level instanceof DesertBossLevel;
	}

	@Override
	public String getName() {
		return Locale.get("desert_ruins");
	}

	@Override
	public String getMusic() {
		return Dungeon.depth == 0 ? "Gobbeon" : "Believer";
	}

	@Override
	protected Painter getPainter() {
		return new HallPainter().setGrass(0.45f).setWater(0f).setDirt(0.45f);
	}
}