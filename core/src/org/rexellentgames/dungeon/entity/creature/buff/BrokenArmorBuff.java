package org.rexellentgames.dungeon.entity.creature.buff;

public class BrokenArmorBuff extends Buff {
	{
		sprite = "ui (defense buff)"; // Todo: replace
		name = "Broken armor";
		description = "Your defense is lower!";
		duration = 10f;
	}

	@Override
	public void onStart() {
		this.owner.modifyDefense(-5);
	}

	@Override
	public void onEnd() {
		this.owner.modifyDefense(5);
	}
}