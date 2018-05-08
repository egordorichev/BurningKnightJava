package org.rexellentgames.dungeon.entity.creature.mob.boss;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.Settings;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.buff.Buff;
import org.rexellentgames.dungeon.entity.creature.buff.BurningBuff;
import org.rexellentgames.dungeon.entity.creature.fx.BloodFx;
import org.rexellentgames.dungeon.entity.creature.fx.Fireball;
import org.rexellentgames.dungeon.entity.creature.fx.GoreFx;
import org.rexellentgames.dungeon.entity.creature.mob.Knight;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.key.KeyC;
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.Part;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.util.*;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.util.ArrayList;

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
 * - Creates a shield from projectiles with only one small hole in it, that you can attack through
 *  + Waves his hand around him
 * - Creates a fireball circle around the player, that slowly moves to him. Blocked via sword
 *
 * - Ranged attack needed
 *
 * Ways to defend for the boss:
 *
 * - Summon enemies and go unhittable
 * - Break BK projectiles via sword
 */

public class CrazyKing extends Boss {
	public static Animation animations = Animation.make("actor_towelking");
	private static AnimationData idle = animations.get("idle");
	private static AnimationData killed = animations.get("dead");
	private static Dialog dialogs = Dialog.make("crazy-king");
	private static DialogData onNotice = dialogs.get("on_notice");
	private AnimationData animation = idle;
	public float z;

	{
		hpMax = 50;
		w = 20;
		h = 24;
		mind = Mind.ATTACKER;

		alwaysRender = true;
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
		this.body = this.createSimpleBody(2, 3, 16, 16, BodyDef.BodyType.DynamicBody, false);
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	protected void die(boolean force) {
		super.die(force);

		this.done = true;
		Dungeon.level.removeSaveable(this);

		if (Settings.gore) {
			for (Animation.Frame frame : killed.getFrames()) {
				GoreFx fx = new GoreFx();

				fx.texture = frame.frame;
				fx.x = this.x + this.w / 2;
				fx.y = this.y + this.h / 2;

				Dungeon.area.add(fx);
			}
		}

		BloodFx.add(this, 20);
	}


	private float sx = 1f;
	private float sy = 1f;
	private float sa = 1f;

	@Override
	public void render() {
		Graphics.startShadows();
		this.animation.render(this.x, this.y - this.z, false, false, this.w / 2, 0, 0, this.flipped ? -this.sx * this.sa : this.sx * this.sa,
			-this.sy * this.sa, false);
		Graphics.endShadows();
		Graphics.batch.setColor(1, 1, 1, this.a);
		this.animation.render(this.x, this.y + this.z, false, false, this.w / 2, 0, 0, this.flipped ? -this.sx : this.sx, this.sy, false);

		/* Graphics.print(this.state, Graphics.small, this.x, this.y);

		Graphics.shape.setProjectionMatrix(Camera.instance.getCamera().combined);

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

		if (this.lastSeen != null) {
			Graphics.batch.end();
			Graphics.shape.setColor(0, 0, 1, 1);
			Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);
			Graphics.shape.line(this.x + this.w / 2, this.y + this.h / 2, this.lastSeen.x + 8, this.lastSeen.y + 8);
			Graphics.shape.end();
			Graphics.shape.setColor(1, 1, 1, 1);
			Graphics.batch.begin();
		}*/
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		this.animation.update(dt);
		super.common();

		if (this.body != null) {
			this.body.setTransform(this.x, this.y, 0);
		}

		Dungeon.level.addLightInRadius(this.x + this.w / 2, this.y + this.h / 2, 0, 0, 0, 2f, 4f, false);
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

	@Override
	protected void onHurt() {
		super.onHurt();

		if (this.hp == 0) {
			this.done = true;
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
			}

			this.checkForTarget();
		}
	}

	private boolean noticed;

	public class AlertedState extends CKState {
		@Override
		public void onEnter() {
			super.onEnter();

			if (!noticed) {
				Dialog.active = onNotice;
				Dialog.active.start();
				Camera.instance.follow(self, false);

				if (self.target != null) {
					self.target.setUnhittable(true);
				}
				
				Dialog.active.onEnd(new Runnable() {
					@Override
					public void run() {
						noticed = true;
						self.become("chase");

						if (self.target != null) {
							self.target.setUnhittable(false);
						}

						Camera.instance.follow(Player.instance, false);
					}
				});
			}
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (noticed && this.t >= 1f) {
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

			if (this.moveTo(self.lastSeen, 6f, 128f)) {
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
					Camera.instance.shake(13);
					self.setUnhittable(false);

					for (Player player : self.colliding) {
						player.modifyHp(-10);
					}

					for (int i = 0; i < 16; i++) {
						Fireball ball = new Fireball();

						float a = (float) (i * Math.PI / 8);
						ball.vel = new Vector2((float) Math.cos(a) * 12f, (float) Math.sin(a) * 12f);

						ball.x = (float) (self.x + (self.w - 16) / 2 + Math.cos(a) * 8);
						ball.y = (float) (self.y + (self.h - 16) / 2 + Math.sin(a) * 8 + 6);

						ball.bad = true;
						Dungeon.area.add(ball);
					}

					Tween.to(new Tween.Task(1.4f, 0.2f, Tween.Type.QUAD_OUT) {
						@Override
						public float getValue() {
							return sx;
						}

						@Override
						public void setValue(float value) {
							sx = value;
						}

						@Override
						public void onEnd() {
							Tween.to(new Tween.Task(1f, 0.2f, Tween.Type.QUAD_IN) {
								@Override
								public float getValue() {
									return sx;
								}

								@Override
								public void setValue(float value) {
									sx = value;
								}
							});
						}
					});

					Tween.to(new Tween.Task(0.6f, 0.2f, Tween.Type.QUAD_OUT) {
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
							Tween.to(new Tween.Task(1f, 0.2f, Tween.Type.QUAD_IN) {
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
			self.setUnhittable(true);


			Tween.to(new Tween.Task(1.4f, 0.2f, Tween.Type.QUAD_OUT) {
				@Override
				public float getValue() {
					return sx;
				}

				@Override
				public void setValue(float value) {
					sx = value;
				}

				@Override
				public void onEnd() {
					Tween.to(new Tween.Task(0.6f, 0.15f, Tween.Type.QUAD_IN) {
						@Override
						public float getValue() {
							return sx;
						}

						@Override
						public void setValue(float value) {
							sx = value;
						}
					});
				}
			});

			Tween.to(new Tween.Task(0.5f, 0.2f, Tween.Type.QUAD_OUT) {
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
					Tween.to(new Tween.Task(1.2f, 0.15f, Tween.Type.QUAD_IN) {
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
									self.tp(self.target.x + (self.target.w - self.w) / 2, self.target.y + (self.target.h - self.h) / 2);
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
					});
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

			float r = Random.newFloat();

			if (r < 0.5f) {
				self.become("fadeOut");
			} else if (Mob.all.size() < 4 && r < 0.6f) {
				for (int i = 0; i < 4; i++) {
					Mob mob = new Knight();

					Dungeon.area.add(mob);
					Dungeon.level.addSaveable(mob);

					float a = (float) (i * Math.PI / 2);

					mob.generate();
					mob.tp(self.x + (self.w - mob.w) / 2 + (float) Math.cos(a) * 24f,
						self.y + (self.h - mob.h) / 2 + (float) Math.sin(a) * 24f);


					for (int j = 0; j < 20; j++) {
						Part part = new Part();

						part.x = mob.x + Random.newFloat(mob.w);
						part.y = mob.y - Random.newFloat(mob.h);

						Dungeon.area.add(part);
					}
				}
			} else if (r < 0.8f) {
				for (int i = 0; i < 16; i++) {
					Fireball ball = new Fireball();

					float a = (float) (i * Math.PI / 8);
					ball.vel = new Vector2((float) -Math.cos(a) * 8f, (float) -Math.sin(a) * 8f);

					ball.x = (float) (self.target.x + (self.target.w - 16) / 2 + Math.cos(a) * 68);
					ball.y = (float) (self.target.y + (self.target.h - 16) / 2 + Math.sin(a) * 68);

					ball.bad = true;
					Dungeon.area.add(ball);
				}
			} else {
				self.become("roam");
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

	@Override
	protected ArrayList<Item> getDrops() {
		ArrayList<Item> drops = super.getDrops();

		drops.add(new KeyC());

		return drops;
	}
}