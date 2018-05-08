package org.rexellentgames.dungeon.entity.item.weapon.rocketlauncher;

import org.rexellentgames.dungeon.assets.Locale;

public class RocketLauncherC extends RocketLauncher {
	{
		sprite = "item (cannon C)";
		useTime = 1f;
		damage = 5;
		name = Locale.get("launcher_c");
		description = Locale.get("launcher_c_desc");
	}
}