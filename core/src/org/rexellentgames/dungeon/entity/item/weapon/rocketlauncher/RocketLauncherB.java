package org.rexellentgames.dungeon.entity.item.weapon.rocketlauncher;

import org.rexellentgames.dungeon.assets.Locale;

public class RocketLauncherB extends RocketLauncher {
	{
		sprite = "item (cannon B)";
		useTime = 1f;
		damage = 4;
		name = Locale.get("launcher_b");
		description = Locale.get("launcher_b_desc");
	}
}