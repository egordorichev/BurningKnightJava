package org.rexellentgames.dungeon.entity.item.weapon.rocketlauncher;

import org.rexellentgames.dungeon.assets.Locale;

public class RocketLauncherA extends RocketLauncher {
	{
		sprite = "item (cannon A)";
		useTime = 1f;
		damage = 2;
		name = Locale.get("launcher_a");
		description = Locale.get("launcher_a_desc");
	}
}