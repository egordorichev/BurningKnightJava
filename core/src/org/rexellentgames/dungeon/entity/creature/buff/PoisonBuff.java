package org.rexellentgames.dungeon.entity.creature.buff;

public class PoisonBuff extends Buff {
	{
		name = "Poisoned";
		description = "You are slowly losing your life";
		duration = 60f;
		sprite = "ui (poison buff)";
		bad = true;
	}

	private float last;

	@Override
	public void update(float dt) {
		super.update(dt);
		this.last += dt;

		if (this.last >= 1f) {
			this.last = 0;
			this.owner.modifyHp(-1, true);
		}
	}
}