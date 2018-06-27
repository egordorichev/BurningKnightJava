package org.rexcellentgames.burningknight.entity.item.weapon.magic;

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
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.fx.RectFx;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.util.Random;

public class Firebolt extends Wand {
	{
		damage = 2;
	}

	public static TextureRegion region = Graphics.getTexture("particle-long");

	@Override
	public void spawnProjectile(float x, float y, float a) {
		float s = 80f;

		BulletProjectile missile = new BulletProjectile() {
			private int num;

			@Override
			public void render() {
				Graphics.batch.end();
				RectFx.shader.begin();
				RectFx.shader.setUniformf("r", 1f);
				RectFx.shader.setUniformf("g", 0.6f);
				RectFx.shader.setUniformf("b", 0.1f);
				RectFx.shader.setUniformf("a", 0.9f);
				RectFx.shader.end();
				Graphics.batch.setShader(RectFx.shader);
				Graphics.batch.begin();
				Graphics.render(region, this.x, this.y, this.a, this.w / 2, this.h / 2, false, false);
				Graphics.batch.end();
				Graphics.batch.setShader(null);
				Graphics.batch.begin();
			}

			@Override
			public void logic(float dt) {
				super.logic(dt);

				if (this.last > 0.03f) {
					this.last = 0;
					RectFx fx = new RectFx();

					fx.depth = this.depth;
					fx.x = this.x + Random.newFloat(this.w) - this.w / 2;
					fx.y = this.y + Random.newFloat(this.w) - this.h / 2;
					fx.w = 4;
					fx.h = 4;
					fx.b = 0.3f;
					fx.g = 0.6f;

					Dungeon.area.add(fx);
				}

				this.body.setTransform(this.x, this.y, (float) Math.toRadians(this.a));
			}

			@Override
			protected boolean breaksFrom(Entity entity) {
				return false;
			}

			@Override
			public void onCollision(Entity entity) {
				super.onCollision(entity);

				if (entity instanceof Door) {
					broke = true;
					return;
				}

				if (entity == null || entity instanceof SolidProp) {
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
						this.vel.x *= -1;
					} else {
						this.vel.y *= -1;
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
		missile.rectShape = true;
		missile.w = 13;
		missile.h = 3;

		double ra = Math.toRadians(a);

		missile.vel.x = (float) Math.cos(ra) * s;
		missile.vel.y = (float) Math.sin(ra) * s;

		Dungeon.area.add(missile);
	}
}