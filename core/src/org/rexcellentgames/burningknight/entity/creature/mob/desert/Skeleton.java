package org.rexcellentgames.burningknight.entity.creature.mob.desert;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.physics.World;
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
		hpMax = 3;
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
	public void initStats() {
		super.initStats();
		setStat("knockback", 0);
	}

	@Override
	public void init() {
		super.init();

		this.body = this.createSimpleBody(2, 1, 12, 12, BodyDef.BodyType.DynamicBody, false);
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
	}

	@Override
	protected void onHurt(int a, Creature from) {
		super.onHurt(a, from);

		if (this.state.equals("dead")) {
			this.triggerEvent("on_death");
			this.remove = true;
			this.dead = true;
			this.done = true;
			this.rem = true;

			for (int i = 0; i < 10; i++) {
				PoofFx fx = new PoofFx();

				fx.x = this.x + this.w / 2;
				fx.y = this.y + this.h / 2;

				Dungeon.area.add(fx);
			}
		}
	}

	private boolean rem;

	@Override
	protected void die(boolean force) {
		if (this.rem) {
			return;
		}

		this.hp = this.hpMax;
		this.done = false;
		this.become("dead");

		if (this.prefix != null) {
			this.prefix.onDeath(this);
		}
	}

	@Override
	public void render() {
		if (Math.abs(this.velocity.x) > 1f) {
			this.flipped = this.velocity.x < 0;
		}

		float v = Math.abs(this.acceleration.x) + Math.abs(this.acceleration.y);

		if (this.state.equals("revive")) {
			this.animation = revive;
		} else if (this.state.equals("dead") || this.state.equals("kindadead")) {
			this.animation = killed;
		} else if (this.invt > 0) {
			this.animation = hurt;
		} else if (v > 1f) {
			this.animation = run;
		} else {
			this.animation = idle;
		}

		this.renderWithOutline(this.animation);
		super.renderStats();
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
		private boolean attacked;

		@Override
		public void onEnter() {
			super.onEnter();
			side = Random.chance(50) ? -1 : 1;
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (!this.attacked && this.t >= 2f) {
				final float t = 0.3f;

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

				attacked = true;
				self.unhittable = true;
			}
		}

		@Override
		public void onExit() {
			self.unhittable = false;
			super.onExit();
		}

		public void attack() {
			bonesMissing = 4;
			float add = Random.chance(50) ? (float) (Math.PI / 4) : 0;

			for (int i = 0; i < (eight ? 8 : 4); i++) {
				BulletProjectile ball = new BulletProjectile() {
					@Override
					public void control() {
						mod(velocity, ivel, angle, dist, t);
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
				ball.velocity = new Point((float) Math.cos(a) / 2f, (float) Math.sin(a) / 2f).mul(boneSpeed * shotSpeedMod * 0.5f);
				ball.x = (float) (self.x + self.w / 2 + Math.cos(a) * 8);
				ball.damage = 2;
				ball.canBeRemoved = false;
				ball.owner = self;
				ball.circleShape = true;
				ball.rotates = true;
				ball.second = false;
				ball.y = (float) (self.y + Math.sin(a) * 8 + 6);

				ball.letter = "bone";
				ball.bad = true;
				ball.canBeRemoved = false;

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

			if (self.rem) {
				return;
			}

			self.ignoreRooms = true;
			self.killed.setFrame(0);
			self.killed.setPaused(false);
			self.okm = self.getStat("knockback");
			self.setStat("knockback", 0);
			delay = Random.newFloat(40f, 60f);
		}

		@Override
		public void onExit() {
			super.onExit();
			self.setStat("knockback", self.okm);
			self.ignoreRooms = false;
			depth = 0;
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (self.rem) {
				return;
			}

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
			self.okm = self.getStat("knockback");
			self.setStat("knockback", 0);
			self.setUnhittable(true);
		}

		@Override
		public void onExit() {
			super.onExit();
			self.setStat("knockback", self.okm);
			self.setUnhittable(false);
			depth = 0;
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
				if (self.target != null) {
					self.become("preattack");
				} else {
					self.noticeSignT = 0f;
					self.hideSignT = 2f;
					self.become("idle");
				}
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

	public static Skeleton random() {
		float r = Random.newFloat(1);

		if (r < 0.5f) {
			return new Skeleton();
		} else if (r < 0.8f) {
			return new BlackSkeleton();
		} else {
			return new BrownSkeleton();
		}
	}
}