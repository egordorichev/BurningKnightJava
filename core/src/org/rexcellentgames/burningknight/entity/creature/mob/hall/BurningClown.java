package org.rexcellentgames.burningknight.entity.creature.mob.hall;

import org.rexcellentgames.burningknight.entity.creature.buff.Buff;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.item.entity.BombEntity;
import org.rexcellentgames.burningknight.util.Animation;

public class BurningClown extends Clown {
	public static Animation animations = Animation.make("actor-clown-v2", "-green");

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 2;
	}

	@Override
	public void apply(BombEntity bomb) {
		super.apply(bomb);
		bomb.toApply.add(new BurningBuff());
	}

	@Override
	protected boolean canHaveBuff(Buff buff) {
		if (buff instanceof BurningBuff) {
			return false;
		}

		return super.canHaveBuff(buff);
	}
}