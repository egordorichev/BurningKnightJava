package org.rexellentgames.dungeon.entity.level;

import org.rexellentgames.dungeon.entity.Entity;

public class SolidLevel extends Entity {
	private Level level;

	public void setLevel(Level level) {
		this.level = level;
		this.depth = 5;
		this.alwaysActive = true;
	}

	@Override
	public void render() {
		this.level.renderSolid();
	}
}