package org.rexcellentgames.burningknight.entity.level.levels;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.RegularLevel;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.HallPainter;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;

public class BossLevel extends RegularLevel {
	public BossLevel() {
		Terrain.loadTextures(0);
		this.uid = 0;
	}

	@Override
	public boolean same(Level level) {
		return super.same(level) || level instanceof BossLevel;
	}

	@Override
	public String getName() {
		return Locale.get("desert_ruins");
	}

	@Override
	public String getMusic() {
		return "Gobbeon";
	}

	@Override
	protected Painter getPainter() {
		return new HallPainter().setGrass(0.35f).setWater(0.35f).setDirt(0.35f).setCobweb(0.001f);
	}
}