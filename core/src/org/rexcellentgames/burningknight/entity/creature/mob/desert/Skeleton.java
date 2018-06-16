package org.rexcellentgames.burningknight.entity.creature.mob.desert;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;

public class Skeleton extends Mob {
	public static Animation animations = Animation.make("actor-skeleton", "-white");
	private AnimationData idle;
	private AnimationData run;
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData revive;
	private AnimationData animation;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 13;

		idle = getAnimation().get("idle").randomize();
		run = getAnimation().get("run").randomize();
		hurt = getAnimation().get("hurt").randomize();
		killed = getAnimation().get("death");
		killed.setAutoPause(true);

		revive = getAnimation().get("revive");
		revive.setListener(new AnimationData.Listener() {
			@Override
			public void onEnd() {
				become("idle");
			}
		});

		animation = this.idle;
	}

	@Override
	public void init() {
		super.init();

		this.body = this.createSimpleBody(2, 1, 12, 12, BodyDef.BodyType.DynamicBody, false);
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	protected void die(boolean force) {
		this.hp = this.hpMax;
		this.done = false;
		this.become("dead");
	}

	@Override
	public void render() {
		if (Math.abs(this.vel.x) > 1f) {
			this.flipped = this.vel.x < 0;
		}

		if (this.falling) {
			this.renderFalling(this.animation);
			return;
		}

		float v = Math.abs(this.vel.x) + Math.abs(this.vel.y);

		if (this.state.equals("revive")) {
			this.animation = revive;
		} else if (this.state.equals("dead")) {
			this.animation = killed;
		} else if (this.invt > 0) {
			this.animation = hurt;
		} else if (v > 9.9) {
			this.animation = run;
		} else {
			this.animation = idle;
		}

		this.renderWithOutline(this.animation);
		Graphics.print(this.state, Graphics.small, this.x, this.y);
	}

	public class SkeletonState extends Mob.State<Skeleton> {

	}

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "dead": return new DeadState();
			case "revive": return new ReviveState();
			case "idle": case "roam": return new IdleState();
			case "alerted": case "chase": return new ChaseState();
		}

		return super.getAi(state);
	}

	public class DeadState extends SkeletonState {
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();
			self.killed.setFrame(0);
			self.killed.setPaused(false);
			delay = Random.newFloat(5f, 10f);
			self.setUnhittable(true);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (t >= delay) {
				self.setUnhittable(false);
				self.become("revive");
			}
		}
	}

	public class ReviveState extends SkeletonState {
		@Override
		public void onEnter() {
			super.onEnter();
			revive.setFrame(0);
		}

		@Override
		public void update(float dt) {
			super.update(dt);
		}
	}

	public class IdleState extends SkeletonState {
		@Override
		public void update(float dt) {
			super.update(dt);
			checkForPlayer();
		}
	}

	public class ChaseState extends SkeletonState {
		@Override
		public void update(float dt) {
			super.update(dt);
			checkForPlayer();
		}
	}

	@Override
	public void update(float dt) {
		if (this.animation != null) {
			this.animation.update(dt * speedMod);
		}

		super.update(dt);

		if (this.freezed) {
			return;
		}

		if (this.dead) {
			super.common();
			return;
		}

		super.common();
	}
}