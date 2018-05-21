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

	private float lastFlame = 0;

	@Override
	protected void onUpdate(float dt) {
		Dungeon.level.addLightInRadius(this.owner.x + this.owner.w / 2, this.owner.y + this.owner.h / 2, 1f, 0.9f, 0f, 0.9f, 3f, false);
		this.lastFlame += dt;

		if (this.lastFlame >= 0.1f) {
			this.addFlame();
			this.lastFlame = 0;

			if (Random.chance(30)) {
				this.owner.modifyHp(-1, null, true);
			}
		}
	}

	public void addFlame() {
		Dungeon.area.add(new FlameFx(this.owner));
	}
}