package org.rexcellentgames.burningknight.entity.creature.mob.desert;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Skeleton extends Mob {
	public static Animation animations = Animation.make("actor-skeleton", "-white");
	private AnimationData idle;
	private AnimationData run;
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData revive;
	private AnimationData animation;
	public float distance = 48;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 13;
		w = 13;

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

		if (this.prefix != null) {
			this.prefix.onDeath(this);
		}
	}

	@Override
	public void render() {
		if (Math.abs(this.vel.x) > 1f) {
			this.flipped = this.vel.x < 0;
		}

		float v = Math.abs(this.vel.x) + Math.abs(this.vel.y);

		if (this.state.equals("revive")) {
			this.animation = revive;
		} else if (this.state.equals("dead") || this.state.equals("kindadead")) {
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

	public class SkeletonState extends Mob.State<Skeleton> {

	}

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "dead": return new DeadState();
			case "kindadead": return new KindaDeadState();
			case "revive": return new ReviveState();
			case "idle": case "roam": return new IdleState();
			case "alerted": case "chase": return new ChaseState();
			case "attack": return new AttackState();
			case "preattack": return new PreattackState();
			case "todead": return new ToDeadState();
		}

		return super.getAi(state);
	}

	public void mod(Point vel, Point ivel, float a, float d, float time) {
		float v = (float) Math.cos(time * 2f);

		vel.x = ivel.x * v;
		vel.y = ivel.y * v;
	}

	public int side;
	public boolean eight;
	public float boneSpeed = 120f;
	public int bonesMissing;

	public class AttackState extends SkeletonState {
		@Override
		public void onEnter() {
			super.onEnter();
			side = Random.chance(50) ? -1 : 1;


			float t = 0.3f;

			Tween.to(new Tween.Task(32, t, Tween.Type.SINE_OUT) {
				@Override
				public float getValue() {
					return self.z;
				}

				@Override
				public void setValue(float value) {
					self.z = value;
					self.depth = (int) value;
				}

				@Override
				public void onEnd() {
					Tween.to(new Tween.Task(0, t, Tween.Type.SINE_IN) {
						@Override
						public float getValue() {
							return self.z;
						}

						@Override
						public void setValue(float value) {
							self.z = value;
							self.depth = (int) value;
						}

						@Override
						public void onEnd() {
							attack();
						}
					});
				}
			});
		}

		public void attack() {
			bonesMissing = 4;
			float add = Random.chance(50) ? (float) (Math.PI / 4) : 0;

			for (int i = 0; i < (eight ? 8 : 4); i++) {
				BulletProjectile ball = new BulletProjectile() {
					@Override
					public void control() {
						mod(vel, ivel, angle, dist, t);
					}

					@Override
					public void onCollision(Entity entity) {
						super.onCollision(entity);

						if (entity == this.owner && t >= 0.1f) {
							bonesMissing -= 1;
							remove = true;
						}
					}

					@Override
					public void countRemove() {
						super.countRemove();
						bonesMissing -= 1;
					}
				};

				float a = (float) (i * Math.PI / (eight ? 4 : 2)) + add;
				ball.vel = new Point((float) Math.cos(a) / 2f, (float) Math.sin(a) / 2f).mul(boneSpeed * shotSpeedMod);

				ball.x = (float) (self.x + self.w / 2 + Math.cos(a) * 8);
				ball.damage = 2;
				ball.canBeRemoved = false;
				ball.owner = self;
				ball.circleShape = true;
				ball.rotates = true;
				ball.y = (float) (self.y + Math.sin(a) * 8 + 6);

				ball.letter = "bone";
				Dungeon.area.add(ball);
			}

			Camera.shake(4);
			self.become("todead");
		}
	}

	public class ToDeadState extends SkeletonState {
		@Override
		public void update(float dt) {
			super.update(dt);
			if (this.t >= 0.3f) {
				self.become("kindadead");
			}
		}
	}

	public float okm;

	public class DeadState extends SkeletonState {
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();
			self.ignoreRooms = true;
			self.killed.setFrame(0);
			self.killed.setPaused(false);
			self.okm = self.knockbackMod;
			self.knockbackMod = 0;
			delay = Random.newFloat(20f, 30f);
			self.setUnhittable(true);
		}

		@Override
		public void onExit() {
			super.onExit();
			self.knockbackMod = self.okm;
			self.ignoreRooms = false;
			self.setUnhittable(false);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (t >= delay) {
				self.become("revive");
			}
		}
	}

	public class KindaDeadState extends SkeletonState {
		@Override
		public void onEnter() {
			super.onEnter();
			self.killed.setFrame(0);
			self.killed.setPaused(false);
			self.okm = self.knockbackMod;
			self.knockbackMod = 0;
			self.setUnhittable(true);
		}

		@Override
		public void onExit() {
			super.onExit();
			self.knockbackMod = self.okm;
			self.setUnhittable(false);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (bonesMissing <= 0) {
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

			if (this.moveTo(self.lastSeen, 4f, distance)) {
				self.become("preattack");
			}
		}
	}

	public class PreattackState extends SkeletonState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= 1f) {
				self.become("attack");
			}
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