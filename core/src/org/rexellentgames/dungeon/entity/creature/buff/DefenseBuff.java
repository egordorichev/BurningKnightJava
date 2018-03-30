package org.rexellentgames.dungeon.entity.creature.buff;

public class DefenseBuff extends Buff {
	{
		sprite = "ui (defense buff)";
		name = "Protected";
		description = "You feel protected";
		duration = 60f;
	}

	@Override
	public void onStart() {
		this.owner.modifyDefense(5);
	}

	@Override
	protected void onEnd() {
		this.owner.modifyDefense(-5);
	}
}