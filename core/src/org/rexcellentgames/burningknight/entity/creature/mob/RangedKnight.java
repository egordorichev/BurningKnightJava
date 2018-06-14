package org.rexcellentgames.burningknight.entity.creature.mob;

import org.rexcellentgames.burningknight.entity.item.weapon.gun.BadGun;
import org.rexcellentgames.burningknight.util.Animation;

public class RangedKnight extends Knight {
	public static Animation animations = Animation.make("actor-knight-v2", "-red");

	public Animation getAnimation() {
		return animations;
	}

	// todo: run away if too close

	{
		hpMax = 5;
	}

	@Override
	public void init() {
		super.init();

		this.sword = new BadGun();
		this.sword.setOwner(this);
	}
}