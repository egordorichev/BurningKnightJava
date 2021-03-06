package org.rexcellentgames.burningknight.entity.level;

import org.rexcellentgames.burningknight.entity.Entity;

public class LiquidLevel extends Entity {
	{
		depth = -8;
		alwaysRender = true;
	}

	private Level level;

	public void setLevel(Level level) {
		this.level = level;
	}

	@Override
	public void render() {
		level.renderLiquids();
	}
}