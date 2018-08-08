package org.rexcellentgames.burningknight.entity.creature.mob.desert;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.entity.BombEntity;
import org.rexcellentgames.burningknight.util.Animation;

public class BrownMummy extends Mummy {
	public static Animation animations = Animation.make("actor-mummy", "-brown");

	{
		speedModifer = 0.5f;
		mod = 4f;
	}

	@Override
	public void initStats() {
		super.initStats();

		modifyStat("knockback", 10);
	}

	@Override
	public Animation getAnimation() {
		return animations;
	}

	@Override
	public void die() {
		super.die();

		BombEntity e = new BombEntity(this.x + (this.w - 16) / 2, this.y + (this.h - 16) / 2);
		e.owner = this;

		Dungeon.area.add(e);

		for (Mob mob : Mob.all) {
			if (mob != this && mob.room == this.room) {
				mob.become("getout");
			}
		}
	}
}