package org.rexcellentgames.burningknight.entity.creature.buff;

public class PoisonBuff extends Buff {
	{
		name = "Poisoned";
		description = "You are slowly losing your life";
		duration = 60f;
		sprite = "ui-poisoned";
		bad = true;
	}

	private float last;

	@Override
	public void onStart() {
		super.onStart();
		this.owner.poisoned = true;
	}

	@Override
	public void onEnd() {
		super.onEnd();
		this.owner.poisoned = false;
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		this.last += dt;

		if (this.last >= 0.5f) {
			this.last = 0;
			this.owner.modifyHp(-1, null, true);
		}
	}
}