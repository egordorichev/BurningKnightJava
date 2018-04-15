package org.rexellentgames.dungeon.entity.creature.mob.boss;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.Random;

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
		mind = Mind.ATTACKER;
	}

	@Override
	public void render() {
		Graphics.batch.setColor(1, 1, 1, this.a);
		this.animation.render(this.x, this.y, this.flipped);
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		this.animation.update(dt);
	}

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "idle": return new IdleState();
			case "roam": return new RoamState();
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
		}
	}
}