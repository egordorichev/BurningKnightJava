package org.rexellentgames.dungeon.entity.creature.buff;

import org.rexellentgames.dungeon.UiLog;

public class HungryBuff extends Buff {
	{
		name = "Hungry";
		description = "Your stomach is empty";
		bad = true;
		infinite = true;
		sprite = 7;
	}

	@Override
	public void onStart() {
		UiLog.instance.print("[orange]You are hungry!");
	}
}