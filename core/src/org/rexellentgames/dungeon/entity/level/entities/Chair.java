package org.rexellentgames.dungeon.entity.level.entities;

import com.badlogic.gdx.math.Rectangle;

public class Chair extends SolidProp {
	public boolean flipped;

	{
		collider = new Rectangle(1, 10, 12, 1);
	}

	@Override
	public void init() {
		this.sprite = flipped ? "biome-0 (chair A)" : "biome-0 (chair B)";

		super.init();
	}
}