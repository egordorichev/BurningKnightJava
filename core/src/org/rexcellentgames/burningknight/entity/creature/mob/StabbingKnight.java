package org.rexcellentgames.burningknight.entity.creature.mob;

import org.rexcellentgames.burningknight.entity.item.weapon.throwing.ThrowingDaggerA;
import org.rexcellentgames.burningknight.util.Animation;

public class StabbingKnight extends Knight {
	public static Animation animations = Animation.make("actor-knight-v2", "-green");

	public Animation getAnimation() {
		return animations;
	}

	@Override
	public void init() {
		super.init();

		this.sword = new ThrowingDaggerA();
		this.sword.setOwner(this);
	}

}