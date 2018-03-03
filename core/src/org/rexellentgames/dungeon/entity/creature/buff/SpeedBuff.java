package org.rexellentgames.dungeon.entity.creature.buff;

import org.rexellentgames.dungeon.util.Log;

public class SpeedBuff extends Buff {
	{
		name = "Supersonic Speed";
		description = "You are super fast!";
		duration = 60f;
		sprite = 3;
	}

	@Override
	public void onStart() {
		Log.info("add");
		this.owner.modifySpeed(10);
	}

	@Override
	protected void onEnd() {
		this.owner.modifySpeed(-10);
	}
}