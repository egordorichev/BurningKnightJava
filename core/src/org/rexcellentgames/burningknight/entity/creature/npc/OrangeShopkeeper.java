package org.rexcellentgames.burningknight.entity.creature.npc;

import org.rexcellentgames.burningknight.util.Animation;

public class OrangeShopkeeper extends Shopkeeper {
	private static Animation animations = Animation.make("actor-trader", "-orange");

	@Override
	public Animation getAnimation() {
		return animations;
	}
}