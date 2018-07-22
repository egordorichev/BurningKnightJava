package org.rexcellentgames.burningknight.entity.item.weapon.rocketlauncher.rocket;

import org.rexcellentgames.burningknight.entity.item.Item;

public class Rocket extends Item {
	public int damage;
	public String rocketName;

	{
		identified = true;
		stackable = true;
		useable = false;
		rocketName = "rocket";
	}

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {

	}
}