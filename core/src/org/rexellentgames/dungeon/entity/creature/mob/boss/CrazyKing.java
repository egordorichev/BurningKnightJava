package org.rexellentgames.dungeon.entity.creature.mob.boss;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.Random;

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

	public class CKState extends State<CrazyKing> {

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