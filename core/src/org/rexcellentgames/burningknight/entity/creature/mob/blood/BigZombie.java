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

public class BigZombie extends Mob {
	public static Animation animations = Animation.make("actor-zombie", "-normal");
	private AnimationData idle;
	private AnimationData run;
	private AnimationData killed;
	private AnimationData hurt;
	private AnimationData hurtDead;
	private AnimationData appear;
	private AnimationData dissappear;
	private AnimationData animation;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 16;

		idle = getAnimation().get("idle");
		run = getAnimation().get("run");
		hurt = getAnimation().get("hurt");
		hurtDead = getAnimation().get("hurt_dead");
		killed = getAnimation().get("dead");
		appear = getAnimation().get("respawn");
		dissappear = getAnimation().get("die");
		animation = idle;
	}

	@Override
	public void init() {
		super.init();

		this.body = this.createSimpleBody(0, 0, 16, 16, BodyDef.BodyType.DynamicBody, false);
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
			this.animation = this.state.equals("dissappear") ? hurtDead : hurt;
		} else if (this.state.equals("dissappear")) {
			this.animation = dissappear;
		} else if (this.state.equals("appear")) {
			this.animation = appear;
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

		lastHit += dt;
		animation.update(dt);
		super.common();
	}

	@Override
	protected void deathEffects() {
		super.deathEffects();

		this.playSfx("death_clown");
		deathEffect(killed);
	}

	private float lastHit;

	@Override
	protected void onHurt(int a, Entity creature) {
		super.onHurt(a, creature);
		lastHit = 0;
		this.playSfx("damage_clown");
	}

	@Override
	protected Mob.State getAi(String state) {
		switch (state) {
			case "idle": case "roam": case "alerted": return new IdleState();
			case "tired": return new TiredState();
			case "dissappear": return new DissappearState();
			case "appear": return new AppearState();
		}

		return super.getAi(state);
	}

	public class IdleState extends Mob.State<BigZombie> {
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();
			delay = Random.newFloat(4f, 12f);
		}

		@Override
		public void update(float dt) {
			if (Player.instance.room == self.room) {
				t += dt;

				if (self.canSee(Player.instance)) {
					moveRightTo(Player.instance, 10f, 4f);
				} else {
					moveTo(Player.instance, 10f, 4f);
				}

				if (self.room != null && Player.instance.room == self.room) {
					for (Mob mob : Mob.all) {
						if (mob != self && mob.room == self.room && mob instanceof BigZombie) {
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

	@Override
	public void die() {
		if (state.equals("dissappear")) {
			super.die();
		} else {
			become("dissappear");
			hp = hpMax / 2;
		}
	}

	public class AppearState extends Mob.State<BigZombie> {
		@Override
		public void onEnter() {
			super.onEnter();

			self.appear.setFrame(0);
			self.appear.setPaused(false);
			self.appear.setAutoPause(true);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (self.t >= 0.4f) {
				self.become("idle");
			}
		}
	}

	public class DissappearState extends Mob.State<BigZombie> {
		@Override
		public void onEnter() {
			super.onEnter();

			lastHit = 0;

			self.dissappear.setFrame(0);
			self.dissappear.setPaused(false);
			self.dissappear.setAutoPause(true);
		}

		private float tt;

		@Override
		public void update(float dt) {
			super.update(dt);

			tt += dt;

			if (tt >= 0.5f) {
				tt = 0;
				self.modifyHp(+1, null);
			}

			if (t >= 5f && lastHit >= 3f) {
				self.become("appear");
			}
		}
	}

	public class TiredState extends Mob.State<BigZombie> {
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