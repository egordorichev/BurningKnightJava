package org.rexcellentgames.burningknight.entity.creature.mob.tutorial;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Tween;

public class Slime extends Mob {
	public static Animation animations = Animation.make("actor-slime", "-red");
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData idle;
	private AnimationData animation;

	public Animation getAnimation() {
		return animations;
	}

	{
		hpMax = 20;

		idle = getAnimation().get("idle");
		hurt = getAnimation().get("hurt");
		killed = getAnimation().get("death");
		animation = this.idle;
	}

	@Override
	public void init() {
		super.init();

		this.body = this.createSimpleBody(2, 1, 12, 12, BodyDef.BodyType.DynamicBody, false);
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.freezed) {
			return;
		}

		if (this.dead) {
			super.common();
			return;
		}

		if (this.animation != null) {
			this.animation.update(dt * speedMod * 1.3f);
		}

		super.common();
	}


	@Override
	public void render() {
		this.animation = this.invt > 0 ? hurt : idle;

		Graphics.batch.setColor(1, 1, 1, this.a * 0.75f);
		this.renderWithOutline(this.animation);
		Graphics.batch.setColor(1, 1, 1, 1);
		super.renderStats();
	}

	@Override
	protected void onHurt(int a, Creature from) {
		super.onHurt(a, from);
		this.playSfx("damage_clown");
	}

	@Override
	protected void deathEffects() {
		super.deathEffects();
		this.playSfx("death_clown");

		this.done = true;
		deathEffect(killed);
	}

	public class SlimeState extends Mob.State<Slime> {

	}

	@Override
	protected State getAiWithLow(String state) {
		return getAi(state);
	}

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "alerted": case "idle": case "chase": case "roam": return new IdleState();
			case "jump": return new JumpState();
		}

		return super.getAi(state);
	}

	public class IdleState extends SlimeState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t >= 3f && self.animation.getFrame() == 3) {

				Tween.to(new Tween.Task(0.7f, 0.1f) {
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
						Tween.to(new Tween.Task(1, 0.2f) {
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

				Tween.to(new Tween.Task(1.3f, 0.1f) {
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
						Tween.to(new Tween.Task(1, 0.2f) {
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

				self.become("jump");
				self.animation.setPaused(true);
			}
		}
	}

	public class JumpState extends SlimeState {
		private float zvel;

		@Override
		public void onExit() {
			super.onExit();
			self.animation.setPaused(false);

			Tween.to(new Tween.Task(1.3f, 0.1f) {
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
					Tween.to(new Tween.Task(1, 0.2f) {
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

			Tween.to(new Tween.Task(0.5f, 0.1f) {
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
					Tween.to(new Tween.Task(1, 0.2f) {
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
		}

		@Override
		public void onEnter() {
			super.onEnter();
			zvel = 150;
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			zvel -= dt * 560;
			self.z = Math.max(0, self.z + zvel * dt);

			if (zvel < 0 && self.z == 0) {
				self.become("idle");
			}
		}
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(x - 1, y + 4, w, h, z * 0.4f + 6f);
	}
}