package org.rexellentgames.dungeon.entity.creature.buff;

public class SpeedBuff extends Buff {
	{
		name = "Supersonic Speed";
		description = "You are super fast!";
		duration = 60f;
		sprite = "ui (speed buff)";
	}

	@Override
	public void onStart() {
		this.owner.modifySpeed(20);
	}

	@Override
	protected void onEnd() {
		this.owner.modifySpeed(-20);
	}
}