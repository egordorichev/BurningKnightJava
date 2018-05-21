package org.rexellentgames.dungeon.entity.level;

import org.rexellentgames.dungeon.entity.Entity;

public class LightLevel extends Entity {
	private Level level;
	public static boolean LIGHT = false;


	public void setLevel(Level level) {
		this.level = level;
		this.depth = 14;
		this.alwaysRender = true;
	}

	@Override
	public void render() {
		if (LIGHT) {
			this.level.renderLight();
		}
	}
}