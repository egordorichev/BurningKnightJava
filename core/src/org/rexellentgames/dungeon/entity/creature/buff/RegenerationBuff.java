package org.rexellentgames.dungeon.entity.creature.buff;

public class RegenerationBuff extends Buff {
	{
		name = "Regeneration";
		description = "You are full of powerful energy";
		duration = 30f;
		sprite = "ui (regen buff)";
	}

	private float last;

	@Override
	public void update(float dt) {
		super.update(dt);
		this.last += dt;

		if (this.last >= 0.5f) {
			this.last = 0;
			this.owner.modifyHp(1, null);
		}
	}
}