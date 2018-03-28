package org.rexellentgames.dungeon.entity.creature.mob;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.geometry.Point;

public class Clown extends Mob {
	private static Animation animations = Animation.make("actor-clown");
	private Point point;
	private AnimationData idle;
	private AnimationData run;
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData animation;

	{
		hpMax = 3;
		speed = 15;

		idle = animations.get("idle");
		run = animations.get("run");
		hurt = animations.get("hurt");
		killed = animations.get("dead");
		animation = this.idle;
	}

	@Override
	public void init() {
		super.init();

		this.body = this.createBody(1, 2, 12, 14, BodyDef.BodyType.DynamicBody, false);
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.dead) {
			super.common();
			return;
		}

		float v = Math.abs(this.vel.x) + Math.abs(this.vel.y);

		if (v > 9.9) {
			this.become("run");
		} else {
			this.become("idle");

			this.vel.x = 0;
			this.vel.y = 0;
		}

		if (this.animation != null) {
			this.animation.update(dt);
		}

		super.common();
	}

	@Override
	public void render() {
		Graphics.batch.setColor(1, 1, 1, this.a);

		if (this.falling) {
			this.renderFalling(this.animation);
			return;
		}

		if (this.dead) {
			this.animation = killed;
		} else if (this.invt > 0) {
			this.animation = hurt;
		} else if (this.state.equals("run")) {
			this.animation = run;
		} else {
			this.animation = idle;
		}

		this.animation.render(this.x, this.y, this.flipped);
	}

	public Point lastSeen;

	@Override
	protected State getAi(String state) {
		if (state.equals("alerted")) {
			return new AlertedState();
		}

		return new IdleState();
	}

	public class ClownState extends State<Clown> {
		public void checkForPlayer() {
			if (self.target != null) {
				self.lastSeen = new Point(self.target.x, self.target.y);

				if (!self.canSee(self.target)) {
					self.target = null;
				}
			}

			if (self.target == null) {
				for (Player player : Player.all) {
					if (self.canSee(player)) {
						self.target = player;
						self.become("alerted");

						break;
					}
				}
			}
		}
	}

	public class IdleState extends ClownState {
		@Override
		public void update(float dt) {
			super.update(dt);
		}
	}

	public class AlertedState extends ClownState {
		private static final float DELAY = 1f;

		@Override
		public void update(float dt) {
			super.update(dt);

			if (self.t >= DELAY) {
				self.become("chase");
			}
		}
	}

	public class ChasingState extends ClownState {
		// todo
	}
}