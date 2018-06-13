package org.rexcellentgames.burningknight.entity.creature.buff;

public class FreezeBuff extends Buff {
	{
		name = "Speed = 0";
		description = "You are slow!";
		duration = 5f;
		// todo: replace
		sprite = "ui (speed buff)";
	}

	private int s;

		@Override
	public void onStart() {
		s = (int) this.owner.getSpeed();
		this.owner.freezed = true;
	}

	@Override
	public void onEnd() {
		this.owner.freezed = false;
	}
}