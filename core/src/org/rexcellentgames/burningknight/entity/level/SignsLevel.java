package org.rexcellentgames.burningknight.entity.level;

import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.npc.Upgrade;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.entities.Door;

public class SignsLevel extends Entity {
	{
		depth = 15;
		alwaysRender = true;
	}

	@Override
	public void render() {
		for (Mob mob : Mob.all) {
			if (mob.onScreen) {
				mob.renderSigns();
			}
		}

		for (Upgrade upgrade : Upgrade.Companion.getAll()) {
			upgrade.renderSigns();
		}

		for (Door door : Door.all) {
			if (door.onScreen) {
				door.renderSigns();
			}
		}

		Player.instance.renderBuffs();
	}
}