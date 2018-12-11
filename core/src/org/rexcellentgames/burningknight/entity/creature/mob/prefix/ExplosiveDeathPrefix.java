package org.rexcellentgames.burningknight.entity.creature.mob.prefix;

import com.badlogic.gdx.graphics.Color;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Explosion;
import org.rexcellentgames.burningknight.entity.item.entity.BombEntity;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Chest;
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

		Explosion.make(xx, yy);

		for (int i = 0; i < Dungeon.area.getEntities().size(); i++) {
			Entity entity = Dungeon.area.getEntities().get(i);

			if (entity instanceof Creature) {
				Creature creature = (Creature) entity;

				if (creature.getDistanceTo(mob.x + 8, mob.y + 8) < 24f) {
					if (!creature.explosionBlock) {
						if (creature instanceof Player) {
							creature.modifyHp(-1000, mob, true);
						} else {
							creature.modifyHp(-Math.round(Random.newFloatDice(20 / 3 * 2, 20)), mob, true);
						}
					}

					float a = (float) Math.atan2(creature.y + creature.h / 2 - mob.y - 8, creature.x + creature.w / 2 - mob.x - 8);

					float knockbackMod = creature.getStat("knockback");
					creature.knockback.x += Math.cos(a) * 10f * knockbackMod;
					creature.knockback.y += Math.sin(a) * 10f * knockbackMod;
				}
			} else if (entity instanceof Chest) {
				if (entity.getDistanceTo(mob.x + 8, mob.y + 8) < 24f) {
					((Chest) entity).explode();
				}
			} else if (entity instanceof BombEntity) {
				BombEntity b = (BombEntity) entity;

				float a = (float) Math.atan2(b.y - mob.y, b.x - mob.x) + Random.newFloat(-0.5f, 0.5f);

				b.vel.x += Math.cos(a) * 200f;
				b.vel.y += Math.sin(a) * 200f;
			}
		}
	}
}