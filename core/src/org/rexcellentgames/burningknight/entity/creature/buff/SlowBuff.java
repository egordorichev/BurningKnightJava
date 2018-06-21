package org.rexcellentgames.burningknight.entity.creature.buff;

public class SlowBuff extends Buff {
	{
		name = "Speed = 0";
		description = "You are slow!";
		duration = 5f;
		sprite = "ui-slowed";
	}

	@Override
	public void onStart() {
		this.owner.modifySpeed(-5);
	}

	@Override
	public void onEnd() {
		this.owner.modifySpeed(5);
	}
}