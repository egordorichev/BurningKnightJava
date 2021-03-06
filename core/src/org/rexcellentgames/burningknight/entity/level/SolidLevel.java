package org.rexcellentgames.burningknight.entity.level;

import org.rexcellentgames.burningknight.entity.Entity;

public class SolidLevel extends Entity {
	private Level level;

	public void setLevel(Level level) {
		this.level = level;
		this.depth = 5;
		this.alwaysRender = true;
	}

	@Override
	public void render() {
		this.level.renderSolid();
	}
}