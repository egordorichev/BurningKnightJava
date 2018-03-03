package org.rexellentgames.dungeon.entity.creature.buff;

public class RegenerationBuff extends Buff {
	{
		name = "Regeneration";
		description = "You are full of powerful energy";
		duration = 30f;
		sprite = 4;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.time % 0.5f <= 0.0175f) {
			this.owner.modifyHp(1);
		}
	}
}