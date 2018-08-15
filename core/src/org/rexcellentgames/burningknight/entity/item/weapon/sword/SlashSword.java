package org.rexcellentgames.burningknight.entity.item.weapon.sword;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.fx.BKSFx;
import org.rexcellentgames.burningknight.entity.item.weapon.Weapon;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.physics.World;
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
		moveXA = 5 * 2;
		moveXB = -16 * 2;
		moveYA = 8 * 2;
		moveYB = 0;
		timeA = 0.3f;
		delayA = 0.15f;
		timeB = 0.2f;
		delayB = 0.1f;
		timeC = 0.3f;

		useTime = timeA + delayA + timeB + delayB + timeC;
	}


	protected float lastAngle;

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.animation != null && this.animation.update(dt)) {
			this.animation.setPaused(true);
		}

		if (fx != null) {
			Dungeon.area.add(fx);
			fx = null;
		}
	}

	private float last;

	public void render(float x, float y, float w, float h, boolean flipped) {
		if (this.animation == null) {
			this.animation = animations.get("idle");
			this.animation.setPaused(true);
		}

		if (this.delay > 0) {
			last += Gdx.graphics.getDeltaTime();
		} else {
			last = 0;
		}

		float angle = added;
		float pure = 0;

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

		float xx = x + w / 2 + (flipped ? 0 : w / 4) + move;
		float yy = y + h / 4 + moveY;

		if (!this.animation.isPaused() && !this.owner.isDead()) {
			this.animation.render(x + w / 2, y - this.owner.hh / 2, false, false, 0, 11, pure, 1, this.owner.isFlipped() ? -1 : 1);
		}

		if (last >= 0.01f) {
			last = 0;

			 fx = new BKSFx();

			fx.x = xx - (flipped ? sprite.getRegionWidth() / 2 : 0);
			fx.y = yy;
			fx.a = angle;
			fx.region = sprite;
			fx.ox = sprite.getRegionWidth() / 2 + this.ox;
			fx.oy = oy;
			fx.flipped = flipped;
		}

		this.renderAt(xx - (flipped ? sprite.getRegionWidth() / 2 : 0), yy,
		angle, sprite.getRegionWidth() / 2 + this.ox, this.oy, false, false, flipped ? -1 : 1, 1);

		if (this.body != null) {
			float a = (float) Math.toRadians(angle);
			this.body.setTransform(xx + (flipped ? - w / 4 : 0), yy, a);
		}
	}

	private BKSFx fx;

	public void use() {
		if (this.delay > 0) {
			return;
		}

		this.delay = this.useTime;
		Point aim = this.owner.getAim();
		float an = owner.getAngleTo(aim.x, aim.y);

		float ya = moveYA;
		float yb = moveYB;
		float xa = -moveXA;
		float xb = -moveXB;

		if (this.owner.isFlipped()) {
			ya *= -1;
			yb *= -1;
		}

		double c = Math.cos(an);
		double s = Math.sin(an);

		float mxb = (float) (xb * c - yb * s);
		float myb = (float) (xb * s + yb * c);

		float mxa = (float) (xa * c - ya * s);
		float mya = (float) (xa * s + ya * c);

		Tween.to(new Tween.Task(mxa, this.timeA, Tween.Type.QUAD_OUT) {
			@Override
			public float getValue() {
				return move;
			}

			@Override
			public void setValue(float value) {
				move = value;
			}
		});

		Tween.to(new Tween.Task(mya, this.timeA, Tween.Type.QUAD_OUT) {
			@Override
			public float getValue() {
				return moveY;
			}

			@Override
			public void setValue(float value) {
				moveY = value;
			}
		});

		float finalMyb = myb;
		float finalMxb = mxb;
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



						if (body != null) {
							body = World.removeBody(body);
						}

						createHitbox();

						owner.playSfx("sword_1");

						if (animation != null) {
							animation.setPaused(false);
						}

						Tween.to(new Tween.Task(finalMxb, timeB) {
							@Override
							public float getValue() {
								return move;
							}

							@Override
							public void setValue(float value) {
								move = value;
							}
						});

						Tween.to(new Tween.Task(finalMyb, timeB) {
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