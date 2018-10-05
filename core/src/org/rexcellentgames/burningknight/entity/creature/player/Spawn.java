package org.rexcellentgames.burningknight.entity.creature.player;

import org.rexcellentgames.burningknight.entity.level.SaveableEntity;

public class Spawn extends SaveableEntity {
	public static Spawn instance;

	@Override
	public void init() {
		super.init();
		instance = this;

		if (Player.instance != null) {
			Player.instance.tp(Spawn.instance.x, Spawn.instance.y);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		instance = null;
	}
}