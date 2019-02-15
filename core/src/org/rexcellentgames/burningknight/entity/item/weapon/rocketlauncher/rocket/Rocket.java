package org.rexcellentgames.burningknight.entity.item.weapon.rocketlauncher.rocket;

import org.rexcellentgames.burningknight.entity.item.Item;

public class Rocket extends Item {
	public int damage;
	public String rocketName;

	{
		stackable = true;
		useable = false;
		rocketName = "rocket";
	}
}