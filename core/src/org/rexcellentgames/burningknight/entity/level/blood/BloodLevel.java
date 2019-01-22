package org.rexcellentgames.burningknight.entity.level.blood;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.level.RegularLevel;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.HallPainter;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;

public class BloodLevel extends RegularLevel {
	public BloodLevel() {
		Terrain.loadTextures(6);
		this.uid = 6;
	}

	@Override
	public String getName() {
		return Locale.get("womb_ruins");
	}

	@Override
	public String getMusic() {
		return Dungeon.depth == 0 ? "Gobbeon" : "Born to do rogueries";
	}

	@Override
	protected Painter getPainter() {
		return new HallPainter().setGrass(0f).setWater(0.35f);
	}
}