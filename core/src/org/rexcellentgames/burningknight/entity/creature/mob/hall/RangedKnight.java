package org.rexcellentgames.burningknight.entity.creature.mob.hall;

import org.rexcellentgames.burningknight.entity.item.weapon.gun.BadGun;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class RangedKnight extends Knight {
	public static Animation animations = Animation.make("actor-knight", "-red");

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 8;
	}

	@Override
	public void initStats() {
		super.initStats();
		setStat("reload_time", 1);
		setStat("ammo_capacity", 1);
	}

	@Override
	public void init() {
		super.init();

		this.sword = new BadGun();
		this.sword.setOwner(this);
	}

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

	public class IdleState extends KnightState {

	}

	@Override
	public boolean rollBlock() {
		return false;
	}

	public class ChaseState extends KnightState {
		@Override
		public void update(float dt) {
			this.checkForPlayer();

			if (self.lastSeen == null) {
				self.become("idle");
				return;
			} else {
				float att = 180;

				if (this.moveTo(self.lastSeen, 18f, att)) {
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
		if (((Gun)this.sword).isReloading()) {
			return;
		}

		if (this.ai != null) {
			this.ai.checkForPlayer();
		}

		if (this.target == null) {
			return;
		}

		float d = this.getDistanceTo(this.target.x + this.target.w / 2, this.target.y + this.target.h / 2);

		if (d < 64f) {
			this.become("runaway");
		}
	}

	public class RunAwayState extends KnightState {
		private float last;
		private Point lsat = new Point();

		@Override
		public void onEnter() {
			super.onEnter();
			lsat.x = self.target.x;
			lsat.y = self.target.y;
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			last += dt;

			if (this.last >= 0.25f) {
				last = 0;
				this.nextPathPoint = null;
			}

			this.checkForPlayer();

			this.moveFrom(lsat, 25f, 10f);

			float d = self.getDistanceTo(lsat.x, lsat.y);

			if (d >= self.minAttack) {
				self.become("preattack");
			}
		}
	}

	public class AttackingState extends KnightState {
		@Override
		public void onEnter() {
			super.onEnter();
		}

		@Override
		public void update(float dt) {
			if (!((Gun) self.sword).isReloading()) {
				if (!canSee(self.target) || self.getDistanceTo(self.target.x + self.target.w / 2, self.target.y + self.target.h / 2) > 220f) {
					self.become("chase");
					return;
				}
			}

			self.sword.use();
			checkForRun();
			self.become("preattack");
		}
	}

	public class PreAttackState extends KnightState {
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
}