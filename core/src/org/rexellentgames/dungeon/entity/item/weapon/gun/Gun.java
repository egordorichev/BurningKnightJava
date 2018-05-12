package org.rexellentgames.dungeon.entity.item.weapon.gun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.Bullet;
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.BulletA;
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.BulletEntity;
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.Shell;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.Tween;
import org.rexellentgames.dungeon.util.geometry.Point;

public class Gun extends Item {
	protected float accuracy = 10f;
	protected float sx = 1f;
	protected float sy = 1f;
	protected float vel = 6f;
	protected int damage;

	{
		identified = true;
		auto = true;
		useTime = 0.2f;
	}

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		TextureRegion sprite = this.getSprite();
		Point aim = this.owner.getAim();

		float an = this.owner.getAngleTo(aim.x, aim.y);
		float a = (float) Math.toDegrees(an);

		Graphics.startShadows();
		Graphics.render(sprite, x + w / 2 + (flipped ? -7 : 7), y - h / 4 - this.owner.z, -a, 3, sprite.getRegionHeight() / 2,
			false, false, this.sx, flipped ? this.sy : -this.sy);
		Graphics.endShadows();

		Graphics.render(sprite, x + w / 2 + (flipped ? -7 : 7), y + h / 4 + this.owner.z, a, 3, sprite.getRegionHeight() / 2,
			false, false, this.sx, flipped ? -this.sy : this.sy);

		if (this.delay + 0.1f >= this.useTime) {
			Graphics.batch.end();

			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			Graphics.shape.setProjectionMatrix(Camera.instance.getCamera().combined);
			Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);

			Graphics.shape.setColor(1, 0.5f, 0, 0.7f);
			float r = 6;

			x = this.owner.x + this.owner.w / 2 + (this.owner.isFlipped() ? -7 : 7) + 3 - 2;
			y = this.owner.y + this.owner.h / 4 + region.getRegionHeight() / 2 - 2;

			float px = sprite.getRegionWidth();
			float py = sprite.getRegionHeight();


			px = (float) Math.cos(an) * px;
			py = (float) Math.sin(an) * py;

			Graphics.shape.circle(px + x, py + y, r);

			Graphics.shape.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
			Graphics.batch.begin();
		}
	}

	@Override
	public void use() {
		super.use();
		this.owner.playSfx("gun_machinegun");
		Point aim = this.owner.getAim();

		float a = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI * 2);

		Shell shell = new Shell();

		float x = this.owner.x + this.owner.w / 2 + (this.owner.isFlipped() ? -7 : 7) + 3 - 2;
		float y = this.owner.y + this.owner.h / 4 + region.getRegionHeight() / 2 - 2;

		shell.x = x;
		shell.y = y - 10;

		shell.vel = new Point(
			(float) -Math.cos(a) * 2f,
			1.5f
		);

		Dungeon.area.add(shell);

		this.owner.vel.x -= Math.cos(a) * 40f;
		this.owner.vel.y -= Math.sin(a) * 40f;

		Camera.instance.push(a, 8f);
		Camera.instance.shake(2);

		Tween.to(new Tween.Task(0.5f, 0.1f) {
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
				Tween.to(new Tween.Task(1f, 0.2f, Tween.Type.BACK_OUT) {
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

		Tween.to(new Tween.Task(1.4f, 0.1f) {
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
				Tween.to(new Tween.Task(1f, 0.2f, Tween.Type.BACK_OUT) {
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

		this.sendBullets();
	}

	protected void sendBullets() {
		Point aim = this.owner.getAim();
		float a = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI * 2);

		this.sendBullet((float) (a + Math.toRadians(Random.newFloat(-this.accuracy, this.accuracy))));
	}

	protected void sendBullet(float an) {
		sendBullet(an, 0, 0);
	}


	protected void sendBullet(float an, float xx, float yy) {
		sendBullet(an, xx, yy, new BulletEntity());
	}

	protected void sendBullet(float an, float xx, float yy, BulletEntity bullet) {
		TextureRegion sprite = this.getSprite();
		float a = (float) Math.toDegrees(an);

		Bullet b = (Bullet) this.owner.getAmmo("bullet");
		bullet.sprite = Graphics.getTexture("bullet (bullet " + b.bulletName + ")");

		float x = this.owner.x + this.owner.w / 2 + (this.owner.isFlipped() ? -7 : 7) + 3 - 2;
		float y = this.owner.y + this.owner.h / 4 + region.getRegionHeight() / 2 - 2;

		float px = sprite.getRegionWidth();
		float py = sprite.getRegionHeight();

		float w = px;
		float h = py + bullet.sprite.getRegionHeight() / 2;

		px = (float) Math.cos(an);
		py = (float) Math.sin(an);

		bullet.x = x + px * w + xx;
		bullet.y = y + py * h + yy;
		bullet.damage = b.damage + this.damage;
		bullet.letter = b.bulletName;

		float s = this.vel;

		bullet.vel = new Point(
			px * s, py * s
		);

		bullet.a = a;

		Dungeon.area.add(bullet);
	}
}