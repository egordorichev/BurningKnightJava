package org.rexcellentgames.burningknight.entity.creature.buff;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class LightBuff extends Buff {
	{
		duration = 160.0f;
		name = "Lighted";
		description = "Your body emits so much light, that you almost can't see anything";
		sprite = "ui (sun buff)";
	}

	@Override
	public void onStart() {
		if (this.owner instanceof Player) {
			((Player) this.owner).lightModifier = 5;
		}
	}

	public void onEnd() {
		if (this.owner instanceof Player) {
			((Player) this.owner).lightModifier = 0;
		}
	}
}