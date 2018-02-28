package org.rexellentgames.dungeon.entity.creature.buff;

import org.rexellentgames.dungeon.entity.creature.buff.fx.FlameFx;

public class BurningBuff extends Buff {
	{
		duration = 600.0f;
		name = "Burning";
		description = "You are on fire!";
		sprite = 0;
		bad = true;
	}

	@Override
	protected void onUpdate(float dt) {
		if (this.time % 0.1 <= 0.017) {
			this.addFlame();
		}
	}

	public void addFlame() {
		this.owner.getArea().add(new FlameFx(this.owner));
	}
}