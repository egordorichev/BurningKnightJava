package org.rexcellentgames.burningknight.entity.item.weapon.sword;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.item.weapon.Weapon;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.ArrayList;

public class Sword extends Weapon {
	protected float oy;
	protected float ox;
	protected static Animation animations = Animation.make("sword-fx");
	protected AnimationData animation;

	protected int maxAngle = 200;

	{
		name = "Sword";
		sprite = "item-sword_b";
		damage = 3;
	}

	protected String getSfx() {
		return "sword_1";
	}

	private float lastFrame;
	private ArrayList<Frame> frames = new ArrayList<>();

	private static class Frame {
		float added;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.animation != null && this.animation.update(dt)) {
			this.animation.setPaused(true);
		}

		this.lastFrame += dt;

		if (this.lastFrame >= 0.005f) {
			this.lastFrame = 0;

			if (this.added > 0) {
				Frame frame = new Frame();
				frame.added = (float) Math.toRadians(this.added);

				this.frames.add(frame);

				if (this.frames.size() > 10) {
					this.frames.remove(0);
				}
			} else if (this.frames.size() > 0) {
				this.frames.remove(0);

				if (this.frames.size() > 0) {
					this.frames.remove(0);
				}
			}
		}
	}

	protected float tr = 1f;
	protected float tg = 1f;
	protected float tb = 1f;

	protected float lastAngle;
	private float pure;

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

		float xx = x + w / 2 + (flipped ? 0 : w / 4);
		float yy = y + h / 4;//(this.ox == 0 ? h / 4 : h / 2);

		if (!this.animation.isPaused() && !this.owner.isDead()) {
			this.animation.render(x + w / 2, y - this.owner.hh / 2, false, false, 0, 11, pure, 1, this.owner.isFlipped() ? -1 : 1);
		}

		if (this.tail && this.frames.size() > 0) {
			double radAngle = Math.toRadians(pure);

			float rx = xx - this.ox - sprite.getRegionWidth() / 2;
			float ry = yy - this.oy;
			float d = this.region.getRegionHeight();

			Frame self = new Frame();
			self.added = (float) Math.toRadians(added);

			Graphics.batch.end();

			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			Graphics.shape.setProjectionMatrix(Camera.game.combined);
			Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);

			for (int i = 0; i < this.frames.size(); i++) {
				Frame frame = this.frames.get(i);

				double ad = radAngle + frame.added - Math.PI / 2;
				double sad = radAngle + self.added - Math.PI / 2;

				Graphics.shape.setColor(this.tr, this.tg, this.tb, 0.7f / (this.frames.size() - i));

				Graphics.shape.triangle(rx, ry, rx + (float) Math.cos(ad) * d, ry + (float) Math.sin(ad) * d,
					rx + (float) Math.cos(sad) * d, ry + (float) Math.sin(sad) * d);

				self = frame;
			}

			Graphics.shape.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
			Graphics.batch.begin();
		}

		this.renderAt(xx - (flipped ? sprite.getRegionWidth() / 2 : 0), yy,
			angle, sprite.getRegionWidth() / 2 + this.ox, this.oy, false, false, flipped ? -1 : 1, 1);

	  if (this.body != null) {
			float a = (float) Math.toRadians(angle);
			World.checkLocked(this.body).setTransform(xx + (flipped ? - w / 4 : 0), yy, a);
		}
	}

	protected  boolean tail;

	@Override
	public void onHit(Creature creature) {
		super.onHit(creature);

		// Camera.shake(4);

		float a = this.owner.getAngleTo(creature.x + creature.w / 2, creature.y + creature.h / 2);
		this.owner.velocity.x += -Math.cos(a) * 120f;
		this.owner.velocity.y += -Math.sin(a) * 120f;
	}

	@Override
	public void use() {
		if (this.delay > 0) {
			return;
		}

		this.owner.playSfx(this.getSfx());

		if (this.animation != null) {
			this.animation.setPaused(false);
		}

		super.use();

		float a = this.owner.getAngleTo(Input.instance.worldMouse.x, Input.instance.worldMouse.y);

		this.owner.velocity.x += -Math.cos(a) * 30f;
		this.owner.velocity.y += -Math.sin(a) * 30f;

		Tween.to(new Tween.Task(this.maxAngle, this.timeA) {
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
				if (timeB == 0) {
					added = 0;
				} else {
					Tween.to(new Tween.Task(0, timeB) {
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
					}).delay(timeDelay);
				}
			}
		});
	}
}