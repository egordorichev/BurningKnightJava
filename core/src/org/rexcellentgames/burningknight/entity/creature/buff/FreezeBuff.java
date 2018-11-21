
package org.rexcellentgames.burningknight.entity.creature.buff;

public class FreezeBuff extends Buff {
	{
		name = "Speed = 0";
		description = "You are slow!";
		duration = 5f;
		sprite = "ui-freezed";
	}

	@Override
	public void onStart() {
		this.owner.freezed = true;
		this.owner.removeBuff(BurningBuff.class);
	}

	@Override
	public void onEnd() {
		this.owner.freezed = false;
	}
}