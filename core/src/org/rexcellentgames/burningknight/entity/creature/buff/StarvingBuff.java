package org.rexcellentgames.burningknight.entity.creature.buff;

public class StarvingBuff extends Buff {
	{
		name = "Starving";
		description = "You want to eat so badly!";
		bad = true;
		infinite = true;
		sprite = "ui (starving buff)";
	}

	@Override
	protected void onUpdate(float dt) {
		super.onUpdate(dt);

		if (this.time % 15f <= 0.017f) {
			this.owner.modifyHp(-1,  null, true);
		}
	}
}