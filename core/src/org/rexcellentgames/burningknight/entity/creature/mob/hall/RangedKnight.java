package org.rexcellentgames.burningknight.entity.creature.mob.hall;

import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.accessory.equippable.Aim;
import org.rexcellentgames.burningknight.entity.item.accessory.equippable.AmmoHolder;
import org.rexcellentgames.burningknight.entity.item.pet.orbital.AmmoOrbital;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.BadGun;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Revolver;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.ArrayList;

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
	protected ArrayList<Item> getDrops() {
		ArrayList<Item> items = new ArrayList<>();

		if (Random.chance(5)) {
			items.add(new Revolver());
		}

		if (Random.chance(2)) {
			items.add(new Aim());
		}

		if (Random.chance(2)) {
			items.add(new AmmoHolder());
		}

		if (Random.chance(2)) {
			items.add(new Aim());
		}

		if (Random.chance(1)) {
			AmmoOrbital item = new AmmoOrbital();
			item.generate();

			items.add(item);
		}

		return items;
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

		if (this.target == null) {
			return;
		}

		float d = this.getDistanceTo(this.target.x + this.target.w / 2, this.target.y + this.target.h / 2);

		if (d < 64f) {
			this.become("runaway");
		}
	}

	public class RunAwayState extends KnightState {
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

				self.sword.use();
				self.become("preattack");
				checkForRun();
			}
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