package org.rexcellentgames.burningknight.entity.level;

import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.level.entities.Door;

public class SignsLevel extends Entity {
	{
		depth = 6;
		alwaysRender = true;
	}

	@Override
	public void render() {
		for (Mob mob : Mob.all) {
			if (mob.onScreen) {
				mob.renderSigns();
			}
		}

		for (Door door : Door.all) {
			if (door.onScreen) {
				door.renderSigns();
			}
		}
	}
}