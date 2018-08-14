package org.rexcellentgames.burningknight.entity.item.weapon.sword;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.entity.item.weapon.Weapon;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class SlashSword extends Weapon {
	protected static Animation animations = Animation.make("sword-fx");
	protected AnimationData animation;
	protected float added;
	protected float maxAngle = 150;

	protected float delayA;
	protected float timeC;
	protected float delayB;
	protected float moveXA;
	protected float moveXB;
	protected float moveYA;
	protected float moveYB;
	protected float backAngle = -60;

	protected float oy;
	protected float ox;
	protected float move;
	protected float moveY;

	{
		moveXA = 10;
		moveXB = -8;
		moveYA = 8;
		moveYB = -4;
		timeA = 0.3f;
		delayA = 0.15f;
		timeB = 0.2f;
		delayB = 0.1f;
		timeC = 0.3f;

		useTime = timeA + delayA + timeB + delayB + timeC;
	}


	protected float lastAngle;
	private float pure;


	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.animation != null && this.animation.update(dt)) {
			this.animation.setPaused(true);
		}
	}

	public void render(float x, float y, float w, float h, boolean flipped) {
		if (this.animation == null) {
			this.animation = animations.get("idle");
			this.animation.setPaused(true);
		}

		float angle = added;
		this.pure = 0;

		if (this.owner != null) {
			Point aim = this.owner.getAim();

			float an = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI);
			an = Gun.angleLerp(this.lastAngle, an, 0.15f);
			this.lastAngle = an;
			float a = (float) Math.toDegrees(this.lastAngle);

			angle += (flipped ? a : -a);
			pure = a - 180;

			angle = flipped ? angle : 180 - angle;
		}

		TextureRegion sprite = this.getSprite();

		float xx = x + w / 2 + (flipped ? move : w / 4 - move);
		float yy = y + h / 4 + moveY;

		if (!this.animation.isPaused() && !this.owner.isDead()) {
			this.animation.render(x + w / 2, y - this.owner.hh / 2, false, false, 0, 11, pure, 1, this.owner.isFlipped() ? -1 : 1);
		}

		this.renderAt(xx - (flipped ? sprite.getRegionWidth() / 2 : 0), yy,
			angle, sprite.getRegionWidth() / 2 + this.ox, this.oy, false, false, flipped ? -1 : 1, 1);

		if (this.body != null) {
			float a = (float) Math.toRadians(angle);
			this.body.setTransform(xx + (flipped ? - w / 4 : 0), yy, a);
		}
	}

	public void use() {
		if (this.delay > 0) {
			return;
		}

		super.use();
		float a = owner.getAngleTo(Input.instance.worldMouse.x, Input.instance.worldMouse.y);

		Tween.to(new Tween.Task(moveXA, this.timeA, Tween.Type.QUAD_OUT) {
			@Override
			public float getValue() {
				return move;
			}

			@Override
			public void setValue(float value) {
				move = value;
			}
		});

		Tween.to(new Tween.Task(moveYA * (float) -Math.sin(a), this.timeA, Tween.Type.QUAD_OUT) {
			@Override
			public float getValue() {
				return moveY;
			}

			@Override
			public void setValue(float value) {
				moveY = value;
			}
		});

		Tween.to(new Tween.Task(this.backAngle, this.timeA, Tween.Type.QUAD_OUT) {
			@Override
			public float getValue() {
				return added;
			}

			@Override
			public void setValue(float value) {
				added = value;
			}

			@Override
			public void onEnd() {
				Tween.to(new Tween.Task(maxAngle, timeB) {
					@Override
					public void onStart() {
						super.onStart();


						float a = owner.getAngleTo(Input.instance.worldMouse.x, Input.instance.worldMouse.y);

						owner.vel.x += -Math.cos(a) * 30f;
						owner.vel.y += -Math.sin(a) * 30f;


						owner.playSfx("sword_1");

						if (animation != null) {
							animation.setPaused(false);
						}

						Tween.to(new Tween.Task(moveXB, timeB) {
							@Override
							public float getValue() {
								return move;
							}

							@Override
							public void setValue(float value) {
								move = value;
							}
						});

						Tween.to(new Tween.Task(moveYB * (float) -Math.sin(a), timeB) {
							@Override
							public float getValue() {
								return moveY;
							}

							@Override
							public void setValue(float value) {
								moveY = value;
							}
						});
					}

					@Override
					public float getValue() {
						return added;
					}

					@Override
					public void setValue(float value) {
						added = value;
					}

					@Override
					public void onEnd() {
						Tween.to(new Tween.Task(0, timeC) {
							@Override
							public float getValue() {
								return added;
							}

							@Override
							public void setValue(float value) {
								added = value;
							}

							@Override
							public void onEnd() {
								endUse();
							}

							@Override
							public void onStart() {
								Tween.to(new Tween.Task(0, timeC) {
									@Override
									public float getValue() {
										return move;
									}

									@Override
									public void setValue(float value) {
										move = value;
									}
								});

								Tween.to(new Tween.Task(0, timeC) {
									@Override
									public float getValue() {
										return moveY;
									}

									@Override
									public void setValue(float value) {
										moveY = value;
									}
								});
							}
						}).delay(delayB);
					}
				}).delay(delayA);
			}
		});
	}
}