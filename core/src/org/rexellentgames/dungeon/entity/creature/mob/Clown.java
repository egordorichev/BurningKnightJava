package org.rexellentgames.dungeon.entity.creature.mob;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.item.entity.BombEntity;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.Log;

public class Clown extends Mob {
	private static Animation animations = Animation.make("actor-clown");
	private AnimationData idle;
	private AnimationData run;
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData laugh;
	private AnimationData animation;
	private boolean toLaugh;

	{
		hpMax = 10;
		hide = true;

		idle = animations.get("idle");
		run = animations.get("run");
		hurt = animations.get("hurt");
		killed = animations.get("dead");
		laugh = animations.get("laugh");
		animation = this.idle;
	}

	@Override
	public void init() {
		super.init();

		this.body = this.createBody(1, 2, 12, 12, BodyDef.BodyType.DynamicBody, false);
		this.body.setTransform(this.x, this.y, 0);

		speed = 100;
		maxSpeed = 100;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.dead) {
			super.common();
			return;
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

		float v = Math.abs(this.vel.x) + Math.abs(this.vel.y);

		if (this.dead) {
			this.animation = killed;
		} else if (this.invt > 0) {
			this.animation = hurt;
		} else if (v > 9.9) {
			this.animation = run;
		} else if (this.state.equals("laugh")) {
			this.animation = this.laugh;
		} else {
			this.animation = idle;
		}

		this.animation.render(this.x, this.y, this.flipped);
	}

	@Override
	protected State getAi(String state) {
		if (state.equals("idle")) {
			return new IdleState();
		} else if (state.equals("alerted")) {
			return new AlertedState();
		} else if (state.equals("chase")) {
			return new ChasingState();
		} else if (state.equals("attack")) {
			return new AttackState();
		} else if (state.equals("fleeing")) {
			return new FleeingState();
		} else if (state.equals("roam")) {
			return new RoamState();
		} else if (state.equals("laugh")) {
			return new LaughState();
		}

		return super.getAi(state);
	}

	public class ClownState extends State<Clown> {

	}

	public class IdleState extends ClownState {
		@Override
		public void update(float dt) {
			super.update(dt);
			this.checkForPlayer();
		}
	}

	public class LaughState extends ClownState {
		@Override
		public void update(float dt) {
			super.update(dt);
			this.checkForPlayer();
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
		public static final float ATTACK_DISTANCE = 24f;

		@Override
		public void update(float dt) {
			this.checkForPlayer();

			if (self.lastSeen == null) {
				return;
			} else {
				float d = 32f;

				if (this.target != null) {
					d = self.getDistanceTo((int) (self.target.x + self.target.w / 2),
						(int) (self.target.y + self.target.h / 2));
				}

				if (this.moveTo(self.lastSeen, d < 48f ? 20f : 10f, ATTACK_DISTANCE)) {
					if (self.target != null) {

						self.become("attack");
					} else if (self.target == null) {
						self.noticeSignT = 0f;
						self.hideSignT = 2f;
						self.become("idle");
					}

					return;
				}
			}

			super.update(dt);
		}
	}

	public class AttackState extends ClownState {
		@Override
		public void update(float dt) {
			self.become("fleeing");
			self.toLaugh = true;

			Dungeon.area.add(new BombEntity(self.x, self.y).velTo(self.lastSeen.x + 8, self.lastSeen.y + 8));

			for (Entity entity : Dungeon.area.getEntities()) {
				if (entity instanceof Mob) {
					Mob mob = (Mob) entity;

					if (mob instanceof BurningKnight) {

					} else {
						if (self.getDistanceTo(mob.x + mob.w / 2, mob.y + mob.h / 2) < 100f) {
							mob.become("fleeing");
						}
					}
				}
			}
		}
	}

	public class FleeingState extends ClownState {
		@Override
		public void update(float dt) {
			this.findNearbyPoint();
			self.flee = Math.max(0, self.flee - (self.mind == Mind.ATTACKER ? 0.1f : 0.05f));

			if (this.targetPoint != null && this.moveTo(this.targetPoint, 16f, 8f)) {
				self.flee = 0;
				self.become(self.toLaugh ? "laugh" : "idle");
				self.toLaugh = false;

				return;
			}

			super.update(dt);
		}
	}

	public class RoamState extends ClownState {
		@Override
		public void onEnter() {
			super.onEnter();
			this.findNearbyPoint();
		}

		@Override
		public void update(float dt) {
			if (this.targetPoint != null && this.moveTo(this.targetPoint, 6f, 8f)) {
				self.become("idle");
				return;
			}

			this.checkForPlayer();
			super.update(dt);
		}
	}
}