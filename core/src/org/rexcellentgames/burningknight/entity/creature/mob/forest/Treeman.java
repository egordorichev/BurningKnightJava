package org.rexcellentgames.burningknight.entity.creature.mob.forest;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.entities.SolidProp;
import org.rexcellentgames.burningknight.entity.trap.RollingSpike;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;

public class Treeman extends Mob {
	public static Animation animations = Animation.make("actor-treeman", "-green");
	private AnimationData idle;
	private AnimationData killed;
	private AnimationData hurt;
	private AnimationData run;
	private AnimationData up;
	private AnimationData down;
	private AnimationData animation;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 10;

		idle = getAnimation().get("blink");
		hurt = getAnimation().get("hurt");
		killed = getAnimation().get("dead");
		up = getAnimation().get("up");
		down = getAnimation().get("down");
		run = getAnimation().get("run");
		animation = idle;
		w = 19;
	}

	@Override
	public void init() {
		super.init();

		flying = true;

		this.body = this.createSimpleBody(2, 3, 12, 9, BodyDef.BodyType.DynamicBody, false);
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
		} else if (this.state.equals("run")) {
			this.animation = run;
		} else if (this.state.equals("up")) {
			this.animation = up;
		} else if (this.state.equals("down")) {
			this.animation = down;
		} else {
			this.animation = idle;
		}

		this.renderWithOutline(this.animation);
		Graphics.batch.setColor(1, 1, 1, 1);
		super.renderStats();
	}

	@Override
	public boolean shouldCollide(Object entity, Contact contact, Fixture fixture) {
		if (entity instanceof SolidProp || entity instanceof RollingSpike) {
			return false;
		}

		return super.shouldCollide(entity, contact, fixture);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (!freezed && !(state.equals("up") || state.equals("down") || state.equals("idle"))) {
			animation.update(dt);
		}

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
			case "up": return new UpState();
			case "down": return new DownState();
			case "run": return new RunState();
			case "idle": case "roam": case "alerted": return new IdleState();
		}

		return super.getAi(state);
	}

	public class TreeState extends Mob.State<Treeman> {

	}

	private boolean noLeft;

	public class IdleState extends TreeState {
		@Override
		public void onEnter() {
			super.onEnter();

			t = Random.newFloat(10);
			delay = Random.newFloat(20, 40);
			dl = Random.newFloat(7f, 20f);
		}

		private float delay;
		private float dl;

		@Override
		public void update(float dt) {
			super.update(dt);
			delay -= dt;

			if (self.target != null && self.getDistanceTo(self.target.x + 8, self.target.y + 8) < 64) {
				self.become("up");
				return;
			}

			boolean found = false;

			for (Mob mob : Mob.all) {
				if (mob.room == self.room && !(mob instanceof Treeman)) {
					found = true;
					break;
				}
			}

			if (!found) {
				self.become("up");
				self.noLeft = true;
				return;
			}

			if (t < dl) {
				self.idle.setFrame(0);

				if (delay <= 0) {
					self.become("up");
				}
			} else if (t < dl + 1.2f) {
				self.idle.setFrame((int) ((t - dl) * 5));
			} else {
				t = 0;
				dl = Random.newFloat(7f, 20f);
			}
		}
	}

	public class RunState extends TreeState {
		private float s;
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();
			delay = Random.newFloat(7, 16f);
			s = Random.newFloat(16, 40);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (self.room != null && Player.instance.room == self.room) {
				for (Mob mob : Mob.all) {
					if (mob != self && mob.room == self.room && mob instanceof Treeman) {
						float x = mob.x + mob.w / 2 + mob.velocity.x * dt * 10;
						float y = mob.y + mob.h / 2 + mob.velocity.y * dt * 10;
						float d = self.getDistanceTo(x, y);

						if (d < 16) {
							float a = d <= 1 ? Random.newFloat((float) (Math.PI * 2)) : self.getAngleTo(x, y);
							float f = 600 * dt;

							self.velocity.x -= Math.cos(a) * f;
							self.velocity.y -= Math.sin(a) * f;
						}
					}
				}
			}

			if (!noLeft) {
				if (self.target == null || !self.onScreen || self.getDistanceTo(self.target.x + 8, self.target.y + 8) > 140f) {
					self.become("down");
					return;
				}

				if (t >= delay) {
					self.become("down");
					return;
				}
			}

			moveTo(Player.instance, s, 4f);
		}
	}

	public class UpState extends TreeState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (t <= 0.5f) {
				self.up.setFrame((int) (t * 8f));
			} else {
				self.become("run");
			}
		}
	}

	public class DownState extends TreeState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (t <= 0.5f) {
				self.down.setFrame((int) (t * 8f));
			} else {
				self.become("idle");
			}
		}
	}
}