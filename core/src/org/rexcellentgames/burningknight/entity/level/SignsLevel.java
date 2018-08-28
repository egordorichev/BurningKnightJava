package org.rexcellentgames.burningknight.entity.level;

import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class SignsLevel extends Entity {
	{
		depth = 14;
		alwaysRender = true;
	}

	@Override
	public void render() {
		for (Mob mob : Mob.all) {
			if (mob.onScreen) {
				mob.renderSigns();
			}
		}

		Player.instance.renderBuffs();
	}
}