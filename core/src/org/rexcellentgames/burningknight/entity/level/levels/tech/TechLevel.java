package org.rexcellentgames.burningknight.entity.level.levels.tech;

import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.level.RegularLevel;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.HallPainter;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;

public class TechLevel extends RegularLevel {
	public TechLevel() {
		Terrain.loadTextures(3);
		this.uid = 3;
	}

	@Override
	public String getName() {
		return Locale.get("secret_laboratory");
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