package org.rexellentgames.dungeon.entity.creature.mob;

import org.rexellentgames.dungeon.entity.item.weapon.gun.BadGun;
import org.rexellentgames.dungeon.util.Animation;

public class RangedKnight extends Knight {
	public static Animation animations = Animation.make("actor-knight-v2", "-red");

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 5;
	}

	@Override
	public void init() {
		super.init();

		this.sword = new BadGun();
		this.sword.setOwner(this);
	}

	@Override
	public void destroy() {
		super.destroy();

		this.sword.destroy();
	}
}