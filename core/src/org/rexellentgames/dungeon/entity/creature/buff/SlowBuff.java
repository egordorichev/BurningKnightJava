package org.rexellentgames.dungeon.entity.creature.buff;

public class SlowBuff extends Buff {
	{
		name = "Speed = 0";
		description = "You are slow!";
		duration = 5f;
		// todo: replace
		sprite = "ui (speed buff)";
	}

	@Override
	public void onStart() {
		this.owner.modifySpeed(-15);
	}

	@Override
	protected void onEnd() {
		this.owner.modifySpeed(15);
	}
}