package org.rexellentgames.dungeon.entity.creature.mob.boss;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.buff.Buff;
import org.rexellentgames.dungeon.entity.creature.buff.BurningBuff;
import org.rexellentgames.dungeon.entity.creature.fx.Fireball;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.util.*;
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
 *  + Just fades away
 *  (done)
 * - Shoots a ring of fast moving fireballs, that can be dodged via distance or sword block
 *  + He jumps
 *  (done)
 * - Creates a shield from projectiles with only one small hole in it, that you can attack through
 *  + Waves his hand around him
 * - Summons helping enemies, goes unhittable till you defeat them all
 *  + Rings in a bell
 * - Creates a fireball circle around the player, that slowly moves to him. Blocked via sword
 *  + Moves hands casting this thing
 *  (done, tho it's hard to not to get hit)
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
	public float z;

	{
		hpMax = 100;
		w = 20;
		h = 23;
		mind = Mind.ATTACKER;

		alwaysActive = true;
	}

	@Override
	protected boolean canHaveBuff(Buff buff) {
		if (buff instanceof BurningBuff) {
			return false;
		}

		return super.canHaveBuff(buff);
	}

	@Override
	public void init() {
		super.init();
		this.body = this.createSimpleBody(2, 4, 16, 16, BodyDef.BodyType.DynamicBody, false);
		this.body.setTransform(this.x, this.y, 0);
	}

	private float sx = 1f;
	private float sy = 1f;
	private float sa = 1f;

	@Override
	public void render() {
		Graphics.startShadows();
		this.animation.render(this.x, this.y - this.h, false, false, this.w / 2, this.h / 2, 0, this.flipped ? -this.sx * this.sa : this.sx * this.sa,
			-this.sy * this.sa, false);
		Graphics.endShadows();
		Graphics.batch.setColor(1, 1, 1, this.a);
		this.animation.render(this.x, this.y + this.z, false, false, this.w / 2, this.h / 2, 0, this.flipped ? -this.sx : this.sx, this.sy, false);
		Graphics.print(this.state, Graphics.small, this.x, this.y);

		if (this.ai != null) {
			if (this.ai.nextPathPoint != null) {
				Graphics.batch.end();
				Graphics.shape.setColor(1, 0, 1, 1);
				Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);
				Graphics.shape.line(this.x + this.w / 2, this.y + this.h / 2, this.ai.nextPathPoint.x + 8, this.ai.nextPathPoint.y + 8);
				Graphics.shape.end();
				Graphics.shape.setColor(1, 1, 1, 1);
				Graphics.batch.begin();
			}

			if (this.ai.targetPoint != null) {
				Graphics.batch.end();
				Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);
				Graphics.shape.line(this.x + this.w / 2, this.y + this.h / 2, this.ai.targetPoint.x + 8, this.ai.targetPoint.y + 8);
				Graphics.shape.end();
				Graphics.batch.begin();
			}
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		this.animation.update(dt);
		super.common();
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
			case "fadeIn": return new FadeInState();
			case "fadeOut": return new FadeOutState();
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
			this.delay = Random.newFloat(4f, 10f);
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

			if (this.targetPoint == null && self.room != null) {
				for (int i = 0; i < 10; i++) {
					Point targetPoint = self.room.getRandomCell();

					if (Dungeon.level.checkFor((int) targetPoint.x, (int) targetPoint.y, Terrain.PASSABLE)) {
						this.targetPoint = targetPoint;
						this.targetPoint.mul(16);

						break;
					}
				}
			}

			if (this.targetPoint == null) {
				self.become("idle");
			} else if (this.moveTo(this.targetPoint, 4f, 32f)) {
				self.become("idle");
				Log.info("done");
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

				if (!self.canSee(self.target) || self.target.invisible) {
					self.target = null;
				}
			}

			if (this.moveTo(self.lastSeen, 6f, 64f)) {
				if (self.target == null || !self.canSee(self.target)) {
					self.target = null;
					self.lastSeen = null;
					self.become("idle");
					self.noticeSignT = 0f;
					self.hideSignT = 2f;
				} else {
					self.become("preattack");
					return;
				}

				return;
			}

			super.update(dt);
		}
	}

	public class FadeInState extends CKState {
		@Override
		public void onEnter() {
			Tween.to(new Tween.Task(0, 1f) {
				@Override
				public float getValue() {
					return z;
				}

				@Override
				public void setValue(float value) {
					z = value;
					depth = (int) value;
				}

				@Override
				public void onEnd() {
					Camera.instance.shake(10);

					Tween.to(new Tween.Task(0.8f, 0.2f, Tween.Type.QUAD_OUT) {
						@Override
						public float getValue() {
							return sy;
						}

						@Override
						public void setValue(float value) {
							sy = value;
						}

						@Override
						public void onEnd() {
							Tween.to(new Tween.Task(1f, 0.3f, Tween.Type.QUAD_IN) {
								@Override
								public float getValue() {
									return sy;
								}

								@Override
								public void setValue(float value) {
									sy = value;
								}

								@Override
								public void onEnd() {
									self.become("roam");
								}
							});
						}
					});
				}
			});

			Tween.to(new Tween.Task(1f, 1f) {
				@Override
				public float getValue() {
					return sa;
				}

				@Override
				public void setValue(float value) {
					sa = value;
				}
			});
		}
	}

	public class FadeOutState extends CKState {
		@Override
		public void onEnter() {
			super.onEnter();

			Tween.to(new Tween.Task(Camera.instance.getCamera().position.y + Display.GAME_HEIGHT * 3, 1f) {
				@Override
				public float getValue() {
					return z;
				}

				@Override
				public void setValue(float value) {
					z = value;
					depth = (int) value;
				}

				@Override
				public void onEnd() {
					Point center;

					do {
						center = self.room.getRandomCell();
					} while (!Dungeon.level.checkFor((int) center.x, (int) center.y, Terrain.PASSABLE));

					// self.tp(center.x * 16 - 16, center.y * 16 - 16);
					self.become("fadeIn");
				}
			});

			Tween.to(new Tween.Task(0f, 1f) {
				@Override
				public float getValue() {
					return sa;
				}

				@Override
				public void setValue(float value) {
					sa = value;
				}
			});
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

			float r = 0;//Random.newFloat();

			if (r < 0.1f) {
				self.become("fadeOut");
			} else if (r < 0.7f) {
				for (int i = 0; i < 16; i++) {
					Fireball ball = new Fireball();

					float a = (float) (i * Math.PI / 8);
					ball.vel = new Vector2((float) Math.cos(a) * 12f, (float) Math.sin(a) * 12f);

					ball.x = self.x + 5;
					ball.y = self.y + 10;

					ball.bad = true;
					Dungeon.area.add(ball);
				}
			} else {
				for (int i = 0; i < 16; i++) {
					Fireball ball = new Fireball();

					float a = (float) (i * Math.PI / 8);
					ball.vel = new Vector2((float) -Math.cos(a) * 4f, (float) -Math.sin(a) * 4f);

					ball.x = (float) (self.target.x + 8 + Math.cos(a) * 48f);
					ball.y = (float) (self.target.y + 8 + Math.sin(a) * 48f);

					ball.bad = true;
					Dungeon.area.add(ball);
				}
			}
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= 2f) {
				self.become("chase");
			}
		}
	}
}