package org.rexcellentgames.burningknight.entity.item.weapon.magic;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import org.rexcellentgames.burningknight.Collisions;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.creature.fx.ManaFx;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.fx.RectFx;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.entity.level.entities.Tree;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Chest;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.physics.World;

public class Firebolt extends Wand {
	{
		name = Locale.get("firebolt");
		description = Locale.get("firebolt_desc");
		sprite = "item-wand_h";
		damage = 2;
		penetrates = true;
		useTime = 0.2f;
	}

	public static TextureRegion region = Graphics.getTexture("particle-long");

	@Override
	public void spawnProjectile(float x, float y, float a) {
		float s = 120f;
		final int mana = getManaUsage();

		BulletProjectile missile = new BulletProjectile() {
			@Override
			protected void onDeath() {
				super.onDeath();

				int weight = mana;

				while (weight > 0) {
					ManaFx fx = new ManaFx();
fx.x = x; 					fx.y = y;
					fx.half = weight == 1;
					fx.poof();

					weight -= fx.half ? 1 : 2;
					Dungeon.area.add(fx);
					LevelSave.add(fx);fx.body.setLinearVelocity(new Vector2(-this.velocity.x * 0.5f, -this.velocity.y * 0.5f));
				}
			}

			private int num;

			{
				ignoreArmor = true;
			}

			@Override
			public void render() {
				Graphics.batch.setColor(1f, 0.6f, 0.1f, 0.2f);
				Graphics.render(region, this.x, this.y, this.a, this.w / 2, this.h / 2, false, false, 2, 2);
				Graphics.batch.setColor(1f, 0.6f, 0.1f, 0.9f);
				Graphics.render(region, this.x, this.y, this.a, this.w / 2, this.h / 2, false, false);
				Graphics.shape.setColor(1, 1, 1, 1);
			}

			private PointLight light;

			@Override
			public void init() {
				super.init();
				light = World.newLight(32, new Color(1f, 0.6f, 0.1f, 1f), 64, x, y);
			}

			@Override
			public void destroy() {
				super.destroy();
				World.removeLight(light);
			}

			@Override
			public void logic(float dt) {
				super.logic(dt);

				light.setPosition(x, y);

				if (this.last > 0.03f) {
					this.last = 0;
					RectFx fx = new RectFx();

					fx.depth = this.depth;
					fx.x = this.x - this.h / 2;
					fx.y = this.y - this.h / 2;
					fx.w = 4;
					fx.h = 4;
					fx.b = 0.3f;
					fx.g = 0.6f;

					Dungeon.area.add(fx);

					Dungeon.level.setOnFire(Level.toIndex((int) Math.floor(this.x / 16), (int) Math.floor((this.y + 8) / 16)), true);
				}

				World.checkLocked(this.body).setTransform(this.x, this.y, (float) Math.toRadians(this.a));
			}

			@Override
			protected boolean breaksFrom(Entity entity) {
				return false;
			}

			@Override
			public void onCollision(Entity entity) {
				super.onCollision(entity);

				if (entity instanceof Door) {
					((Door) entity).burning = true;
				} else if (entity instanceof Chest) {
					((Chest) entity).burning = true;
				} else if (this.t >= 0.2f && entity instanceof Creature) {
					((Creature) entity).addBuff(new BurningBuff());
				} else if (entity instanceof Tree) {
					((Tree) entity).burning = true;
				}

				if (entity == null || entity instanceof SolidProp || entity instanceof Door) {
					num++;

					if (num >= 5) {
						broke = true;
						return;
					}

					PolygonShape poly = ((PolygonShape) Collisions.last.getShape());
					Vector2 sum = new Vector2();

					for (int i = 0; i < poly.getVertexCount(); i++) {
						Vector2 pos = new Vector2();
						poly.getVertex(i, pos);
						sum.add(pos);
					}

					sum.x /= poly.getVertexCount();
					sum.y /= poly.getVertexCount();

					float x = this.x;
					float y = this.y;

					double a = Math.atan2(y - sum.y, x - sum.x);

					if (a < 0) {
						a += Math.PI * 2;
					}

					if (a > Math.PI * 1.75 || a < Math.PI * 0.25 || (a > Math.PI * 0.75 && a < Math.PI * 1.25)) {
						this.velocity.x *= -1;
					} else {
						this.velocity.y *= -1;
					}
				}
			}

			@Override
			protected void onHit(Entity entity) {
				super.onHit(entity);

				if (entity instanceof Creature) {
					((Creature) entity).addBuff(new BurningBuff());
				}
			}
		};

		missile.depth = 1;
		missile.damage = this.rollDamage();

		missile.crit = this.lastCrit;
		missile.owner = this.owner;
		missile.x = x;
		missile.y = y - 3;
		missile.penetrates = true;
		missile.canBeRemoved = false;
		missile.rectShape = true;
		missile.w = 13;
		missile.h = 3;

		double ra = Math.toRadians(a);

		missile.velocity.x = (float) Math.cos(ra) * s;
		missile.velocity.y = (float) Math.sin(ra) * s;

		Dungeon.area.add(missile);
	}
}