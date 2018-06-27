package org.rexcellentgames.burningknight.entity.creature.mob.hall;

import org.rexcellentgames.burningknight.entity.item.weapon.throwing.ThrowingDaggerA;
import org.rexcellentgames.burningknight.util.Animation;

public class StabbingKnight extends RangedKnight {
	public static Animation animations = Animation.make("actor-knight-v2", "-green");

	public Animation getAnimation() {
		return animations;
	}

	@Override
	public void init() {
		super.init();

		this.minAttack = 64f;
		this.sword = new ThrowingDaggerA();
		this.sword.setOwner(this);
	}
}