package org.rexcellentgames.burningknight.entity.level.levels.hall;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.RegularLevel;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.HallPainter;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;

public class HallLevel extends RegularLevel {
	public HallLevel() {
		Terrain.loadTextures(0);
		this.uid = 0;
	}

	@Override
	public boolean same(Level level) {
		return super.same(level) || level instanceof HallBossLevel;
	}

	@Override
	public String getName() {
		return Locale.get("old_castle");
	}

	@Override
	public String getMusic() {
		return Dungeon.depth == -2 ? "Shopkeeper" : (Dungeon.depth == 0 ? "Gobbeon" : (Dungeon.depth < 0 ? "Outsider" : "Born to do rogueries"));
	}

	@Override
	protected Painter getPainter() {
		return new HallPainter().setGrass(0.35f).setWater(0.35f).setDirt(0.35f).setCobweb(0.001f);
	}
}