package org.rexellentgames.dungeon.entity.creature.buff;

import org.rexellentgames.dungeon.UiLog;

public class HungryBuff extends Buff {
	{
		name = "Hungry";
		description = "Your stomach is empty";
		bad = true;
		infinite = true;
		sprite = "ui (hungry buff)";
	}

	@Override
	public void onStart() {
		if (UiLog.instance != null) {
			UiLog.instance.print("[orange]You are hungry!");
		}
	}
}