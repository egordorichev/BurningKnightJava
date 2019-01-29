package org.rexcellentgames.burningknight.entity.creature.mob.ice;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Snowflake extends Mob {
	public static Animation animations = Animation.make("actor-snowflake", "-white");
	private AnimationData idle;
	private AnimationData killed;
	private AnimationData hurt;
	private AnimationData animation;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 12;

		idle = getAnimation().get("idle");
		hurt = getAnimation().get("hurt");
		killed = getAnimation().get("dead");
		animation = idle;
	}

	@Override
	public void init() {
		super.init();

		flying = true;
		this.body = World.createCircleBody(this, 2, 2, 6, BodyDef.BodyType.DynamicBody, false);
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	@Override
	public void render() {
		if (this.target != null) {
			this.flipped = this.target.x < this.x;
		} else {
			if (Math.abs(this.velocity.x) > 1f) {
				this.flipped = this.velocity.x < 0;
			}
		}

		if (this.dead) {
			this.animation = killed;
		} else if (this.invt > 0) {
			this.animation = hurt;
		} else {
			this.animation = idle;
		}

		this.renderWithOutline(this.animation);
		Graphics.batch.setColor(1, 1, 1, 1);
		super.renderStats();
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		animation.update(dt);
		super.common();
	}

	@Override
	protected void deathEffects() {
		super.deathEffects();

		this.playSfx("death_clown");
		deathEffect(killed);
	}

	@Override
	protected void onHurt(int a, Entity creature) {
		super.onHurt(a, creature);
		this.playSfx("damage_clown");
	}

	@Override
	protected State getAi(String state) {
		return new IdleState();
	}

	public class IdleState extends Mob.State<Snowflake> {
		private float init;

		@Override
		public void onEnter() {
			super.onEnter();

			init = Random.newFloat((float) (Math.PI * 2));
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (self.target != null && self.target.room == self.room) {
				if (self.canSee(self.target)) {
					float a = t * 1.5f + init;
					float d = 32f;

					flyTo(new Point(self.target.x + 8 + (float) Math.cos(a) * d, self.target.y + 8 + (float) Math.sin(a) * d), 20f, 4f);

					if (self.room != null && Player.instance.room == self.room) {
						for (Mob mob : Mob.all) {
							if (mob != self && mob.room == self.room && mob instanceof Snowflake) {
								float x = mob.x + mob.w / 2 + mob.velocity.x * dt * 10;
								float y = mob.y + mob.h / 2 + mob.velocity.y * dt * 10;
								d = self.getDistanceTo(x, y);

								if (d < 16) {
									a = d <= 1 ? Random.newFloat((float) (Math.PI * 2)) : self.getAngleTo(x, y);
									float f = 600 * dt;

									self.velocity.x -= Math.cos(a) * f;
									self.velocity.y -= Math.sin(a) * f;
								}
							}
						}
					}

				} else {
					moveTo(self.target, 20f, 4f);
				}
			}
		}
	}
}