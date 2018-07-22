package org.rexcellentgames.burningknight.entity.creature.mob.desert;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Explosion;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.Random;

public class BrownMummy extends Mummy {
	public static Animation animations = Animation.make("actor-mummy", "-brown");

	{
		speedModifer = 0.5f;
		mod = 4f;
	}

	@Override
	public void initStats() {
		super.initStats();

		modifyStat("knockback", 10);
	}

	@Override
	public Animation getAnimation() {
		return animations;
	}

	@Override
	protected void collide(Player player) {
		super.collide(player);

		this.playSfx("explosion");
		Explosion.make(this.x + 8, this.y + 8);

		for (int i = 0; i < Dungeon.area.getEntities().size(); i++) {
			Entity entity = Dungeon.area.getEntities().get(i);

			if (entity instanceof Creature) {
				Creature creature = (Creature) entity;

				if (creature.getDistanceTo(this.x + 8, this.y + 8) < 24f) {
					if (!creature.explosionBlock) {
						creature.modifyHp(-Math.round(Random.newFloatDice(10 / 3 * 2, 10)), this, true);
					}

					float a = (float) Math.atan2(creature.y + creature.h / 2 - this.y - 8, creature.x + creature.w / 2 - this.x - 8);

					float knockbackMod = creature.getStat("knockback");
					creature.vel.x += Math.cos(a) * 5000f * knockbackMod;
					creature.vel.y += Math.sin(a) * 5000f * knockbackMod;
				}
			}
		}
	}
}