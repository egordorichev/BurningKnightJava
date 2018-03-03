package org.rexellentgames.dungeon.entity.creature.buff;

public class PoisonBuff extends Buff {
	{
		name = "Poisoned";
		description = "You are slowly losing your life";
		duration = 60f;
		sprite = 5;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.time % 1f <= 0.0175f) {
			this.owner.modifyHp(-1, true);
		}
	}
}