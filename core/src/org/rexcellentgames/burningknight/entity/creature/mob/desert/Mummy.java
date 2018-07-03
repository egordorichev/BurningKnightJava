package org.rexcellentgames.burningknight.entity.creature.mob.desert;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Mummy extends Mob {
	public static Animation animations = Animation.make("actor-mummy", "-brown");
	private AnimationData idle;
	private AnimationData run;
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData animation;

	public Animation getAnimation() {
		return animations;
	}

	{
		w = 12;
		hpMax = 10;
		idle = getAnimation().get("idle").randomize();
		run = getAnimation().get("run").randomize();
		hurt = getAnimation().get("hurt").randomize();
		killed = getAnimation().get("death").randomize();
		animation = this.idle;
	}

	/*
	 * Close range enemy:
	 * Slowly runs to you, collides with you, doesn't make it possible to run through it
	 * Deals contact damage after some time after contact
	 */

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "alerted": case "chase": case "roam": case "idle": return new ChaseState();
			case "tired": return new TiredState();
		}

		return super.getAi(state);
	}

	public class MummyState extends Mob.State<Mummy> {

	}

	public class ChaseState extends MummyState {
		private float delay;
		private Point to;

		@Override
		public void onEnter() {
			super.onEnter();

			this.delay = Random.newFloat(7, 10);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			this.checkForPlayer();

			if (self.target != null) {
				this.moveTo(self.lastSeen, 4f, 8f);
			} else {
				if (to == null) {
					to = self.room.getRandomFreeCell();
				}

				this.moveTo(this.to, 3f, 16f);
			}

			if (this.t >= this.delay) {
				self.become("tired");
			}
		}
	}

	public class TiredState extends MummyState {
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();

			this.delay = Random.newFloat(3, 5);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= this.delay) {
				self.become("chase");
			}
		}
	}

	@Override
	public void init() {
		super.init();

		this.body = this.createSimpleBody(2, 1, 12, 12, BodyDef.BodyType.DynamicBody, false);
		this.body.setTransform(this.x, this.y, 0);

		speed = 100;
		maxSpeed = 100;
	}

	@Override
	public void render() {
		float v = Math.abs(this.vel.x) + Math.abs(this.vel.y);

		if (this.dead) {
			this.animation = killed;
		} else if (this.invt > 0) {
			this.animation = hurt;
		} else if (v > 9.9) {
			this.animation = run;
		} else {
			this.animation = idle;
		}

		this.renderWithOutline(this.animation);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.freezed) {
			return;
		}

		if (Math.abs(this.vel.x) > 1f) {
			this.flipped = this.vel.x < 0;
		}

		if (this.dead) {
			super.common();
			return;
		}

		if (this.animation != null) {
			this.animation.update(dt * speedMod);
		}

		super.common();
	}
}