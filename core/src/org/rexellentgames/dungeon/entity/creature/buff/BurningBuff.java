package org.rexellentgames.dungeon.entity.creature.buff;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.creature.buff.fx.FlameFx;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;

public class BurningBuff extends Buff {
	{
		duration = 5.0f;
		name = "Burning";
		description = "You are on fire!";
		sprite = "ui (burning buff)";
		bad = true;
	}

	@Override
	protected void onUpdate(float dt) {
		Dungeon.level.addLightInRadius(this.owner.x + this.owner.w / 2, this.owner.y + this.owner.h / 2, 1f, 0.9f, 0f, 0.9f, 3f, false);

		if (this.time % 0.1 <= 0.017) {
			this.addFlame();

			if (Random.chance(30)) {
				this.owner.modifyHp(-1, true);
			}
		}
	}

	public void addFlame() {
		this.owner.getArea().add(new FlameFx(this.owner));
	}
}