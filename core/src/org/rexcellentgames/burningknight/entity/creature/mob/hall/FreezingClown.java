package org.rexcellentgames.burningknight.entity.creature.mob.hall;

import org.rexcellentgames.burningknight.entity.creature.buff.Buff;
import org.rexcellentgames.burningknight.entity.creature.buff.FreezeBuff;
import org.rexcellentgames.burningknight.entity.item.entity.BombEntity;
import org.rexcellentgames.burningknight.util.Animation;

public class FreezingClown extends Clown {
	public static Animation animations = Animation.make("actor-clown-v2", "-black");

	public Animation getAnimation() {
		return animations;
	}

	@Override
	public void apply(BombEntity bomb) {
		super.apply(bomb);
		bomb.toApply.add(new FreezeBuff());
	}

	@Override
	protected boolean canHaveBuff(Buff buff) {
		if (buff instanceof FreezeBuff) {
			return false;
		}

		return super.canHaveBuff(buff);
	}
}