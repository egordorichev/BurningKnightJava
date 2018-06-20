package org.rexcellentgames.burningknight.entity.creature.mob.boss;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.Buff;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.creature.fx.HpFx;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.mob.hall.RangedKnight;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.key.KeyC;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.CKGun;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Part;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.ui.UiBanner;
import org.rexcellentgames.burningknight.util.*;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.io.IOException;
import java.util.ArrayList;

public class CrazyKing extends Boss {
	public static Animation animations = Animation.make("actor-towelking");
	private static AnimationData idle = animations.get("idle");
	private static AnimationData killed = animations.get("dead");
	private static AnimationData jump = animations.get("jump");
	private static AnimationData land = animations.get("land");
	private static AnimationData run = animations.get("run");
	private static AnimationData hurt = animations.get("hurt");
	private static Dialog dialogs = Dialog.make("crazy-king");
	private static DialogData onNotice = dialogs.get("on_notice");
	private AnimationData animation = idle;
	public boolean secondForm;
	private CKGun gun;

	{
		hpMax = 100;
		w = 20;
		h = 24;
		texture = "ui-bkbar-ck_head";
		mind = Mind.ATTACKER;
		ignoreHealthbar = true;

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
		this.shouldBeInTheSameRoom = !this.talked;

		this.gun = new CKGun();
		this.gun.setOwner(this);
	}

	@Override
	protected void die(boolean force) {
		this.become("toDeath");
		this.done = false;
	}

	@Override
	public void destroy() {
		super.destroy();
		this.gun.destroy();
	}

	private float sx = 1f;
	private float sy = 1f;
	private float sa = 1f;

	@Override
	public void render() {
		if (this.invt > 0) {
			this.animation = hurt;
		} else if (this.state.equals("fadeIn") || (this.state.equals("jump") && !this.up)) {
			this.animation = land;
		} else if (this.state.equals("fadeOut") || (this.state.equals("jump") && this.up)) {
			this.animation = jump;
		} else if (this.vel.len2() > 5) {
			this.animation = run;
		} else {
			this.animation = idle;
		}

		if (this.target != null) {
			float dx = this.x + this.w / 2 - this.target.x - this.target.w / 2;
			this.flipped = dx >= 0;
		}

		Graphics.batch.setColor(1, 1, 1, this.a);
		this.animation.render(this.x, this.y + this.z, false, false, this.w / 2, 0, 0, this.flipped ? -this.sx : this.sx, this.sy, false);

		if (this.secondForm) {
			this.gun.render(this.x, this.y, this.w, this.h, this.flipped);
		}
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x + 1, this.y, 16, 16, this.z / 2);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.freezed) {
			return;
		}

		this.animation.update(dt * speedMod);
		super.common();

		this.gun.update(dt * speedMod);
		this.secondForm = (this.hp < this.hpMax / 2);

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
			case "jump": return new JumpState();
			case "toDeath": return new ToDeathState();
		}

		return super.getAi(state);
	}

	public class CKState extends Boss.BossState<CrazyKing> {

	}

	public class ToDeathState extends CKState {
		@Override
		public void onEnter() {
			Camera.shake(4);
			Camera.follow(self, false);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= 3f) {
				self.die(false);

				Audio.highPriority("Reckless");
				self.dead = true;
				self.done = true;

				Camera.shake(10);
				deathEffect(killed);

				Tween.to(new Tween.Task(0, 0.1f) {
					@Override
					public float getValue() {
						return 0;
					}

					@Override
					public void setValue(float value) {
						Camera.follow(Player.instance, false);
					}
				}.delay(1f));
			}
		}
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

			this.checkForTarget();
		}
	}

	@Override
	protected void onHurt(float a, Creature creature) {
		super.onHurt(a, creature);

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
				if (talked) {
					self.become("chase");
				} else {
					Dialog.active = onNotice;
					Dialog.active.start();
					Camera.follow(self, false);

					if (self.target != null) {
						self.target.setUnhittable(true);
					}

					Dialog.active.onEnd(() -> {
						noticed = true;
						self.become("chase");

						if (self.target != null) {
							self.target.setUnhittable(false);
						}

						ignoreHealthbar = false;
						Camera.follow(Player.instance, false);
						talked = true;


						UiBanner banner = new UiBanner();
						banner.text = Locale.get("crazy_king");
						Dungeon.area.add(banner);
					});
				}
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

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.shouldBeInTheSameRoom = !this.talked;
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

	protected boolean up;

	public class JumpState extends CKState {
		private boolean set;

		@Override
		public void onEnter() {
			up = true;
			jump.setFrame(0);
			jump.setAutoPause(true);
			jump.setPaused(false);

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
					});
				}
			});

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

			jump.setListener(new AnimationData.Listener() {
				@Override
				public void onFrame(int frame) {
					if (frame == 5 && !set) {
						set = true;
						self.playSfx("CK_jump");

						Tween.to(new Tween.Task(32f, 0.4f, Tween.Type.SINE_OUT) {
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
								Camera.shake(5);
								up = false;
								land.setPaused(true);
								land.setFrame(0);

								Tween.to(new Tween.Task(0f, 0.4f, Tween.Type.SINE_IN) {
									@Override
									public float getValue() {
										return z;
									}

									@Override
									public void setValue(float value) {
										z = value;
										depth = (int) value;
									}

									private boolean nset;

									@Override
									public void onEnd() {
										self.playSfx("CK_attack");
										land.setPaused(false);
										land.setFrame(2);
										land.setAutoPause(true);

										for (Player player : self.colliding) {
											player.modifyHp(-4, self);
										}

										for (int i = 0; i < 8; i++) {
											BulletProjectile ball = new BulletProjectile();

											float a = (float) (i * Math.PI / 4);
											ball.vel = new Point((float) Math.cos(a) / 2f, (float) Math.sin(a) / 2f).mul(60f * shotSpeedMod);

											ball.parts = true;
											ball.x = (float) (self.x + self.w / 2 + Math.cos(a) * 8);
											ball.damage = 2;
											ball.y = (float) (self.y + Math.sin(a) * 8 + 6);

											ball.letter = "bad";
											Dungeon.area.add(ball);
										}

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
												});
											}
										});

										land.setListener(new AnimationData.Listener() {
											@Override
											public void onFrame(int frame) {
												if (frame == 4 && !nset) {
													self.become("chase");
												}
											}
										});
									}
								});
							}
						});
					}
				}
			});
		}
	}

	@Override
	public HpFx modifyHp(int amount, Creature owner, boolean ignoreArmor) {
		if (!this.state.equals("jump") && !this.state.equals("fadeIn") && !this.state.equals("fadeOut")) {
			return super.modifyHp(amount, owner, ignoreArmor);
		}

		return null;
	}

	public class FadeInState extends CKState {
		@Override
		public void onEnter() {
			land.setPaused(true);
			land.setFrame(0);

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

			Tween.to(new Tween.Task(0f, 1f, Tween.Type.SINE_IN) {
				@Override
				public float getValue() {
					return z;
				}

				@Override
				public void setValue(float value) {
					z = value;
					depth = (int) value;
				}

				private boolean nset;

				@Override
				public void onEnd() {
					Camera.shake(5);
					self.playSfx("CK_attack");

					for (Player player : self.colliding) {
						player.modifyHp(-4, self);
					}

					for (int i = 0; i < 8; i++) {
						BulletProjectile ball = new BulletProjectile();

						float a = (float) (i * Math.PI / 4);
						ball.vel = new Point((float) Math.cos(a) / 2f, (float) Math.sin(a) / 2f).mul(60f * shotSpeedMod);

						ball.x = (float) (self.x + self.w / 2 + Math.cos(a) * 8);
						ball.damage = 2;
						ball.y = (float) (self.y + Math.sin(a) * 8 + 6);
						ball.parts = true;

						ball.letter = "bad";
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
							});
						}
					});

					land.setPaused(false);
					land.setFrame(2);
					land.setAutoPause(true);

					land.setListener(new AnimationData.Listener() {
						@Override
						public void onFrame(int frame) {
							if (frame == 4 && !nset) {
								nset = true;
								self.become("chase");
							}
						}
					});
				}
			});
		}
	}

	public class FadeOutState extends CKState {
		private boolean set;

		@Override
		public void onEnter() {
			super.onEnter();

			jump.setFrame(0);
			jump.setAutoPause(true);
			jump.setPaused(false);

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
					});
				}
			});

			jump.setListener(new AnimationData.Listener() {
				@Override
				public void onFrame(int frame) {
					if (frame == 5 && !set) {
						self.playSfx("CK_jump");

						Tween.to(new Tween.Task(0, 1f) {
							@Override
							public float getValue() {
								return sa;
							}

							@Override
							public void setValue(float value) {
								sa = value;
							}
						});

						set = true;
						Tween.to(new Tween.Task(Camera.game.position.y + Display.GAME_HEIGHT * 1.5f, 1f, Tween.Type.QUAD_IN) {
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
								if (self.target != null) {
									self.tp(self.target.x + (self.target.w - self.w) / 2, self.target.y);
								}

								self.become("fadeIn");
							}
						});
					}
				}
			});
		}
	}

	public class PreattackState extends CKState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= (self.secondForm ? 2f : 4f)) {
				self.become("attack");
			}
		}
	}

	public class AttackState extends CKState {
		private boolean longShot;
		private float last;
		private int count;

		@Override
		public void update(float dt) {
			super.update(dt);

			if (self.secondForm) {
				if (this.longShot) {
					this.last += dt;

					if (this.last >= 0.2f) {
						self.gun.defaultShot();
						this.last = 0;
						this.count++;

						if (this.count >= 10) {
							self.become("chase");
						}
					}
				} else {
					float r = Random.newFloat();

					if (r < 0.3f) {
						self.gun.bigShot();
						self.become("chase");
					} else if (r < 0.5f) {
						this.longShot = true;
					} else if (r < 0.6f) {
						self.become("jump");
					} else if (r < 0.7f) {
						self.become("fadeOut");
					} else {
						self.gun.trippleShot();
						self.become("chase");
					}
				}
			} else {
				float r = Random.newFloat();

				if (r < 0.6f) {
					self.become("jump");
				} else if (Mob.all.size() < 2 && r < 0.7f) {
					self.playSfx("CK_call");

					for (int i = 0; i < 2; i++) {
						Mob mob = new RangedKnight();

						Dungeon.area.add(mob);
						LevelSave.add(mob);

						float a = (float) (i * Math.PI);

						mob.generate();
						mob.tp(self.x + (self.w - mob.w) / 2 + (float) Math.cos(a) * 24f,
							self.y + (self.h - mob.h) / 2 + (float) Math.sin(a) * 24f);


						for (int j = 0; j < 20; j++) {
							Part part = new Part();

							part.x = mob.x + Random.newFloat(mob.w);
							part.y = mob.y + Random.newFloat(mob.h);

							Dungeon.area.add(part);
						}
					}
					self.become("chase");
				} else {
					self.become("fadeOut");
				}
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