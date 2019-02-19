package org.rexcellentgames.burningknight.entity.creature;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Explosion;
import org.rexcellentgames.burningknight.entity.item.entity.BombEntity;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Chest;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.file.FileReader;

import java.io.IOException;

public class Mine extends SaveableEntity {
	public static Animation animations = Animation.make("actor-mine", "-normal");
	private AnimationData idle;
	private Body body;

	@Override
	public void init() {
		super.init();
		idle = animations.get("idle");
		h = 9;
		w = 14;
	}

	@Override
	public void destroy() {
		super.destroy();
		body = World.removeBody(body);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
	}

	@Override
	public void onCollision(Entity e) {
		super.onCollision(e);

		if (e instanceof Player) {
			playSfx("explosion");
			done = true;
			Explosion.make(x + w / 2, y + h / 2, true);
			for (int i = 0; i < Dungeon.area.getEntities().size(); i++) {
				Entity entity = Dungeon.area.getEntities().get(i);

				if (entity instanceof Creature) {
					Creature creature = (Creature) entity;

					if (creature.getDistanceTo(this.x + w / 2, this.y + h / 2) < 24f) {
						if (!creature.explosionBlock) {
							if (creature instanceof Player) {
								creature.modifyHp(-1000, this, true);
							} else {
								creature.modifyHp(-Math.round(Random.newFloatDice(20 / 3 * 2, 20)), this, true);
							}
						}

						float a = (float) Math.atan2(creature.y + creature.h / 2 - this.y - 8, creature.x + creature.w / 2 - this.x - 8);

						float knockbackMod = creature.knockbackMod;
						creature.knockback.x += Math.cos(a) * 10f * knockbackMod;
						creature.knockback.y += Math.sin(a) * 10f * knockbackMod;

					}
				} else if (entity instanceof Chest) {
					if (entity.getDistanceTo(this.x + w / 2, this.y + h / 2) < 24f) {
						((Chest) entity).explode();
					}
				} else if (entity instanceof BombEntity) {
					BombEntity b = (BombEntity) entity;

					float a = (float) Math.atan2(b.y - this.y, b.x - this.x) + Random.newFloat(-0.5f, 0.5f);

					b.vel.x += Math.cos(a) * 200f;
					b.vel.y += Math.sin(a) * 200f;
				}
			}
		}
	}

	@Override
	public void update(float dt) {
		if (body == null) {
			body = World.createSimpleBody(this, 0, 0, w, h, BodyDef.BodyType.DynamicBody, true);
			body.setTransform(x, y, 0);
		}

		super.update(dt);
		idle.update(dt);
	}

	@Override
	public void render() {
		super.render();
		idle.render(this.x, this.y, false);
	}
}