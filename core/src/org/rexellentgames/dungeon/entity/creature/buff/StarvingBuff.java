package org.rexellentgames.dungeon.entity.creature.buff;

import org.rexellentgames.dungeon.UiLog;

public class StarvingBuff extends Buff {
	{
		name = "Starving";
		description = "You want to eat so badly!";
		bad = true;
		infinite = true;
		sprite = 8;
	}

	@Override
	public void onStart() {
		if (UiLog.instance != null) {
			UiLog.instance.print("[red]You are starving!");
		}
	}

	@Override
	protected void onUpdate(float dt) {
		super.onUpdate(dt);

		if (this.time % 5f <= 0.017f) {
			this.owner.modifyHp(-1, true);
		}
	}
}