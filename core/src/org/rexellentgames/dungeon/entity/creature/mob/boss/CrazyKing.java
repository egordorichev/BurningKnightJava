package org.rexellentgames.dungeon.entity.creature.mob.boss;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;

/*
 * Tests player skills, such as:
 *
 * - Quick movement, dodging
 * - Bullet dodging via sword block
 * - BK usage (ranged attacks)
 * - Navigating in full darkness
 * - Managing multiple enemies
 *
 *
 *
 * Attacks that do that:
 *
 * - Turns off the light on the level, teleports to a random location in the room
 * - Shoots a ring of fast moving fireballs, that can be dodged via distance or sword block
 * - Creates a shield from projectiles with only one small hole in it, that you can attack through
 * - Summons helping enemies, goes unhittable till you defeat them all
 * - Creates a fireball circle around the player, that slowly moves to him. Blocked via sword
 *
 *
 *
 * Ways to defend for the boss:
 *
 * - Summon enemies and go unhittable
 * - Break BK projectiles via sword
 */

public class CrazyKing extends Boss {
	private static Animation animations = Animation.make("actor_towel_king");
	private static AnimationData idle = animations.get("idle");
	private AnimationData animation = idle;

	{
		hpMax = 100;
		w = 20;
		h = 24;
		mind = Mind.ATTACKER;

		alwaysActive = true;
		depth = 1; // debug
	}

	@Override
	public void init() {
		super.init();
		this.body = this.createBody(0, 0, 20, 24, BodyDef.BodyType.DynamicBody, false);
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void render() {
		Graphics.batch.setColor(1, 1, 1, this.a);
		this.animation.render(this.x, this.y, this.flipped);
		Graphics.print(this.state + " " + this.vel.x + " : " + this.vel.y, Graphics.small, this.x, this.y);
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		this.animation.update(dt);
		super.common();

		Log.info(this.x + " " + this.y);
	}

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "idle": return new IdleState();
			case "roam": return new RoamState();
			case "alerted": return new AlertedState();
			case "chase": return new ChaseState();
			case "preattack": return new PreattackState();
			case "attack": return new AttackState();
		}

		return super.getAi(state);
	}

	public class CKState extends BossState<CrazyKing> {

	}

	public class IdleState extends CKState {
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();
			this.delay = Random.newFloat(1f, 3f);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= this.delay) {
				self.become("roam");
			}

			this.checkForTarget();
		}
	}

	public class RoamState extends CKState {
		@Override
		public void update(float dt) {
			super.update(dt);

			this.findNearbyPoint();

			if (this.targetPoint == null) {
				self.become("idle");
			} else if (this.moveTo(this.targetPoint, 10f, 8f)) {
				self.become("idle");
			}

			this.checkForTarget();
		}
	}

	public class AlertedState extends CKState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= 1f) {
				self.become("chase");
			}
		}
	}

	public class ChaseState extends CKState {
		@Override
		public void update(float dt) {
			if (self.target != null) {
				self.lastSeen = new Point(self.target.x, self.target.y);
			}

			if (this.moveTo(self.lastSeen, 10f, 64f)) {
				self.become("preattack");
				return;
			} else if ((self.lastSeen == null || (self.target != null && !self.canSee(self.target)) &&
				(Dungeon.depth > 0)) || (self.target != null && self.target.invisible)) {
				self.target = null;
				self.become("idle");
				self.noticeSignT = 0f;
				self.hideSignT = 2f;
				return;
			}

			super.update(dt);
		}
	}

	public class PreattackState extends CKState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= 1f) {
				self.become("attack");
			}
		}
	}

	public class AttackState extends CKState {
		@Override
		public void onEnter() {
			super.onEnter();

			// todo: attack
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= 1f) {
				self.become("chase");
			}
		}
	}
}