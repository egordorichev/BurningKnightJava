package org.rexellentgames.dungeon.entity.creature.mob.prefix;

import com.badlogic.gdx.graphics.Color;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.item.Explosion;
import org.rexellentgames.dungeon.entity.plant.Plant;
import org.rexellentgames.dungeon.util.Random;

public class ExplosiveDeathPrefix extends Prefix {
	private static Color color = Color.valueOf("#8f563b");

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void onDeath() {
		super.onDeath();

		this.mob.playSfx("explosion");
		float xx = this.mob.x + this.mob.w / 2;
		float yy = this.mob.y + this.mob.h / 2;

		Dungeon.area.add(new Explosion(xx, yy));

		for (int i = 0; i < Dungeon.area.getEntities().size(); i++) {
			Entity entity = Dungeon.area.getEntities().get(i);

			if (entity instanceof Creature && !(entity instanceof Mob)) {
				Creature creature = (Creature) entity;

				if (creature.getDistanceTo(xx, yy) < 32f) {
					creature.modifyHp(-Math.round(Random.newFloatDice(2, 4)), this.mob, true);

					float a = (float) Math.atan2(creature.y + creature.h / 2 - yy, creature.x + creature.w / 2 - xx);

					creature.vel.x += Math.cos(a) * 5000f;
					creature.vel.y += Math.sin(a) * 5000f;
				}
			} else if (entity instanceof Plant) {
				Plant creature = (Plant) entity;

				float dx = creature.x + creature.w / 2 - xx;
				float dy = creature.y + creature.h / 2 - yy;

				if (Math.sqrt(dx * dx + dy * dy) < 24f) {
					creature.startBurning();
				}
			}
		}
	}
}