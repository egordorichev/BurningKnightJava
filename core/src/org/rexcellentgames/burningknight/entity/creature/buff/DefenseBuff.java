package org.rexcellentgames.burningknight.entity.creature.buff;

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
	public void onEnd() {
		this.owner.modifyDefense(-5);
	}
}