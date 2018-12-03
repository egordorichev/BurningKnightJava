package org.rexcellentgames.burningknight.entity.creature.mob.desert;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.SnipperGun;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Archeologist extends Mob {
	public static Animation animations = Animation.make("actor-archeologist", "-green");
	private AnimationData idle;
	private AnimationData run;
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData animation;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 10;
		idle = getAnimation().get("idle").randomize();
		run = getAnimation().get("run").randomize();
		hurt = getAnimation().get("hurt").randomize();
		killed = getAnimation().get("death").randomize();
		animation = this.idle;
		speed = 100;
		maxSpeed = 100;
	}

	private Item weapon;

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "preattack": return new PreAttackState();
			case "attack": return new AttackingState();
			case "runaway": case "alerted": return new RunAwayState();
			case "roam": case "idle": return new IdleState();
			case "chase": return new ChaseState();
		}

		return super.getAi(state);
	}

	public class ArcheologistState extends Mob.State<Archeologist> {

	}

	public class IdleState extends ArcheologistState {
		@Override
		public void update(float dt) {
			super.update(dt);
			this.checkForPlayer();

			if (self.target != null) {
				self.become("alerted");
			}
		}
	}

	@Override
	public boolean rollBlock() {
		return false;
	}

	public class ChaseState extends ArcheologistState {
		@Override
		public void update(float dt) {
			this.checkForPlayer();

			if (self.lastSeen == null) {
				self.become("idle");
				return;
			} else {
				float att = 180;

				if (this.moveTo(self.lastSeen, 18f, att) && self.onScreen) {
					if (self.target != null && self.getDistanceTo((int) (self.target.x + self.target.w / 2),
						(int) (self.target.y + self.target.h / 2)) <= att) {

						if (self.canSee(self.target)) {
							self.become("preattack");
						}
					} else {
						self.noticeSignT = 0f;
						self.hideSignT = 2f;
						self.become("idle");
					}
				}
			}

			super.update(dt);
		}
	}

	public void checkForRun() {
		if (((Gun)this.weapon).isReloading()) {
			return;
		}

		if (this.target == null) {
			return;
		}

		float d = this.getDistanceTo(this.target.x + this.target.w / 2, this.target.y + this.target.h / 2);

		if (d < 64f) {
			this.become("runaway");
		}
	}

	public class RunAwayState extends ArcheologistState {
		@Override
		public void onEnter() {
			super.onEnter();
			self.lastSeen = new Point(self.target.x, self.target.y);
		}

		@Override
		public void update(float dt) {
			super.update(dt);
			this.checkForPlayer();

			if (self.lastSeen == null) {
				self.become("idle");
				return;
			}

			this.moveFrom(self.lastSeen, 20f, 6f);
			float d = self.getDistanceTo(self.target.x, self.target.y);

			if (d >= 128) {
				self.become("preattack");
			}
		}
	}

	{
		alwaysRender = true;
	}

	public class AttackingState extends ArcheologistState {
		@Override
		public void onEnter() {
			super.onEnter();
		}

		@Override
		public void update(float dt) {
			if (!((Gun) self.weapon).isReloading() && !(((Gun) self.weapon).showRedLine)) {
				if (!canSee(self.target) || self.getDistanceTo(self.target.x + self.target.w / 2, self.target.y + self.target.h / 2) > 220f) {
					self.become("chase");
					return;
				}

				self.weapon.use();
				self.become("preattack");
				checkForRun();
			}
		}
	}

	public class PreAttackState extends ArcheologistState {
		@Override
		public void update(float dt) {
			super.update(dt);

			float dx = self.target.x + self.target.w / 2 - lastAim.x;
			float dy = self.target.y + self.target.h / 2 - lastAim.y;
			// float d = (float) Math.sqrt(dx * dx + dy);
			float s = 0.04f;

			lastAim.x += dx * s;
			lastAim.y += dy * s;

			if (this.t > 1f) {
				self.become("attack");
			}

			checkForRun();
		}
	}
	private Point lastAim = new Point();

	@Override
	public void tp(float x, float y) {
		super.tp(x, y);

		lastAim.x = x + 10;
		lastAim.y = y;
	}

	@Override
	public Point getAim() {
		return lastAim;
	}

	public boolean bombs;

	@Override
	public void init() {
		super.init();

		this.body = this.createSimpleBody(2, 1, 12, 12, BodyDef.BodyType.DynamicBody, false);
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);

		this.weapon = new SnipperGun();
		this.weapon.setOwner(this);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.freezed) {
			return;
		}

		if (Math.abs(this.velocity.x) > 1f) {
			this.flipped = this.velocity.x < 0;
		}

		if (this.dead) {
			super.common();
			return;
		}

		if (this.animation != null) {
			this.animation.update(dt * speedMod);
		}

		this.weapon.update(dt * speedMod);

		super.common();

	}

	@Override
	public void render() {
		float v = Math.abs(this.acceleration.x) + Math.abs(this.acceleration.y);

		if (this.dead) {
			this.animation = killed;
		} else if (this.invt > 0) {
			this.animation = hurt;
		} else if (v > 1f) {
			this.animation = run;
		} else {
			this.animation = idle;
		}

		this.renderWithOutline(this.animation);
		this.weapon.render(this.x, this.y, this.w, this.h, this.flipped);
		super.renderStats();
	}

	@Override
	public void destroy() {
		super.destroy();

		if (this.weapon != null) {
			this.weapon.destroy();
		}
	}

	@Override
	protected void deathEffects() {
		super.deathEffects();
		this.playSfx("death_clown");

		this.done = true;
		deathEffect(killed);
	}
}