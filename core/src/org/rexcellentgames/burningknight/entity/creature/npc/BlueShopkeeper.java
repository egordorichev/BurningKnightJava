package org.rexcellentgames.burningknight.entity.creature.npc;

import org.rexcellentgames.burningknight.util.Animation;

public class BlueShopkeeper extends Shopkeeper {
	private static Animation animations = Animation.make("actor-trader", "-blue");

	@Override
	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 40;
	}
}