package org.rexellentgames.dungeon.entity.item.weapon.rocketlauncher;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.item.weapon.Weapon;
import org.rexellentgames.dungeon.entity.item.weapon.WeaponBase;
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.Shell;
import org.rexellentgames.dungeon.entity.item.weapon.rocketlauncher.rocket.Rocket;
import org.rexellentgames.dungeon.entity.item.weapon.rocketlauncher.rocket.RocketEntity;
import org.rexellentgames.dungeon.util.Tween;
import org.rexellentgames.dungeon.util.geometry.Point;

public class RocketLauncher extends WeaponBase {
	private float sx = 1f;
	private float sy = 1f;

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		Point aim = this.owner.getAim();

		float a = this.owner.getAngleTo(aim.x, aim.y);
		float an = (float) Math.toDegrees(a);
		TextureRegion sprite = this.getSprite();

		Graphics.startShadows();
		Graphics.render(sprite, x + w / 2, y - (h - sprite.getRegionHeight()) / 2 - h / 2, -an, 3, sprite.getRegionHeight() / 2, false, false, sx, flipped ? sy : -sy);
		Graphics.endShadows();
		this.applyColor();
		Graphics.render(sprite, x + w / 2, y + (h - sprite.getRegionHeight()) / 2, an, 3, sprite.getRegionHeight() / 2, false, false, sx, flipped ? -sy : sy);


		if (this.delay + 0.09f >= this.useTime) {
			Graphics.batch.end();

			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			Graphics.shape.setProjectionMatrix(Camera.instance.getCamera().combined);
			Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);

			Graphics.shape.setColor(1, 0.5f, 0, 0.7f);
			float r = 6;

			float xx = this.owner.x + this.owner.w / 2;
			float yy = this.owner.y + (this.owner.h - sprite.getRegionHeight()) / 2;

			float px = (float) Math.cos(a);
			float py = (float) Math.sin(a);
			w = sprite.getRegionWidth() - r;

			Graphics.shape.circle(xx + px * w, yy + py * w, r);

			Graphics.shape.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
			Graphics.batch.begin();
		}
	}

	@Override
	public void use() {
		super.use();
		Camera.instance.shake(2);
		Point aim = this.owner.getAim();

		this.owner.playSfx("gun_machinegun");

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

		this.sendRockets();

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
	}

	protected void sendRockets() {
		Point aim = this.owner.getAim();

		float a = (float) (this.owner.getAngleTo(aim.x, aim.y) - Math.PI * 2);
		this.sendRocket(a);

	}

	protected void sendRocket(float an) {
		TextureRegion sprite = this.getSprite();

		RocketEntity bullet = new RocketEntity();
		float a = (float) Math.toDegrees(an);

		Rocket b = (Rocket) this.owner.getAmmo("rocket");

		bullet.sprite = Graphics.getTexture("bullet (rocket " + b.rocketName + ")");

		float x = this.owner.x + this.owner.w / 2;
		float y = this.owner.y + (this.owner.h - sprite.getRegionHeight()) / 2;

		float px = (float) Math.cos(an);
		float py = (float) Math.sin(an);

		bullet.x = x + px * sprite.getRegionWidth();
		bullet.y = y + py * sprite.getRegionWidth();
		bullet.damage = b.damage + this.damage;
		bullet.letter = b.rocketName;

		float s = 1f;

		bullet.vel = new Point(
			px * s, py * s
		);

		bullet.owner = this.owner;
		bullet.a = a;

		Dungeon.area.add(bullet);
	}
}