package org.rexcellentgames.burningknight.entity.creature.mob.blood;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;

public class Zombie extends Mob {
	public static Animation animations = Animation.make("actor-small-zombie", "-normal");
	private AnimationData idle;
	private AnimationData run;
	private AnimationData killed;
	private AnimationData hurt;
	private AnimationData animation;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 16;

		idle = getAnimation().get("idle");
		run = getAnimation().get("run");
		hurt = getAnimation().get("hurt");
		killed = getAnimation().get("dead");
		animation = idle;
	}

	@Override
	public void init() {
		super.init();

		w = 8;
		h = 8;

		this.body = this.createSimpleBody(0, 0, 8, 8, BodyDef.BodyType.DynamicBody, false);
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	@Override
	public void render() {
		if (Math.abs(this.velocity.x) > 1f) {
			this.flipped = this.velocity.x < 0;
		}

		if (this.dead) {
			this.animation = killed;
		} else if (this.invt > 0) {
			this.animation = hurt;
		} else if (this.acceleration.len2() > 1) {
			this.animation = run;
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
		switch (state) {
			case "idle": case "roam": case "alerted": return new IdleState();
			case "tired": return new TiredState();
		}

		return super.getAi(state);
	}

	public class IdleState extends Mob.State<Zombie> {
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();
			delay = Random.newFloat(2f, 6f);
		}

		@Override
		public void update(float dt) {
			if (Player.instance.room == self.room) {
				t += dt;

				if (self.canSee(Player.instance)) {
					moveRightTo(Player.instance, 25f, 4f);
				} else {
					moveTo(Player.instance, 25f, 4f);
				}

				if (self.room != null && Player.instance.room == self.room) {
					for (Mob mob : Mob.all) {
						if (mob != self && mob.room == self.room) {
							float x = mob.x + mob.w / 2 + mob.velocity.x * dt * 10;
							float y = mob.y + mob.h / 2 + mob.velocity.y * dt * 10;
							float d = self.getDistanceTo(x, y);

							if (d < 16) {
								float a = d <= 1 ? Random.newFloat((float) (Math.PI * 2)) : self.getAngleTo(x, y);
								float f = 500 * dt;

								self.velocity.x -= Math.cos(a) * f;
								self.velocity.y -= Math.sin(a) * f;
							}
						}
					}
				}

				if (t >= delay) {
					self.become("tired");
				}
			}
		}
	}

	public class TiredState extends Mob.State<Zombie> {
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();
			delay = Random.newFloat(2f, 5f);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (t >= delay) {
				self.become("idle");
			}
		}
	}
}