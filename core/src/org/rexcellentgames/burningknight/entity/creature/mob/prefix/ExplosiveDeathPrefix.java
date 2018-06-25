package org.rexcellentgames.burningknight.entity.creature.mob.prefix;

import com.badlogic.gdx.graphics.Color;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.Explosion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.util.Random;

public class ExplosiveDeathPrefix extends Prefix {
	private static Color color = Color.valueOf("#8f563b");

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void onDeath(Mob mob) {
		super.onDeath(mob);

		mob.playSfx("explosion");
		float xx = mob.x + mob.w / 2;
		float yy = mob.y + mob.h / 2;

		Dungeon.area.add(new Explosion(xx, yy));

		for (int i = 0; i < Dungeon.area.getEntities().size(); i++) {
			Entity entity = Dungeon.area.getEntities().get(i);

			if (entity instanceof Creature && !(entity instanceof Mob)) {
				Creature creature = (Creature) entity;

				if (creature.getDistanceTo(xx, yy) < 32f) {
					creature.modifyHp(-Math.round(Random.newFloatDice(2, 4)), mob, true);

					float a = (float) Math.atan2(creature.y + creature.h / 2 - yy, creature.x + creature.w / 2 - xx);

					creature.vel.x += Math.cos(a) * 5000f * creature.knockbackMod;
					creature.vel.y += Math.sin(a) * 5000f * creature.knockbackMod;
				}
			}
		}
	}
}