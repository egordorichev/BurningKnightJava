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
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.BulletEntity;
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.Shell;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;

public class Gun extends Item {
	protected float accuracy = 10f;

	{
		identified = true;
		auto = true;
		useTime = 0.1f;
	}

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		TextureRegion sprite = this.getSprite();

		float an = this.owner.getAngleTo(Input.instance.worldMouse.x, Input.instance.worldMouse.y);
		float a = (float) Math.toDegrees(an);

		Graphics.startShadows();
		Graphics.render(sprite, x + w / 2 + (flipped ? -7 : 7), y - h / 4, -a, 3, sprite.getRegionHeight() / 2,
			false, false, 1f, flipped ? 1f : -1f);
		Graphics.endShadows();

		Graphics.render(sprite, x + w / 2 + (flipped ? -7 : 7), y + h / 4, a, 3, sprite.getRegionHeight() / 2,
			false, false, 1f, flipped ? -1f : 1f);

		if (this.delay + 0.02f >= this.useTime) {
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
		if (!(this.owner instanceof Player)) {
			return;
		}

		Player player = (Player) this.owner;

		if (!player.getInventory().find(Bullet.class)) {
			return;
		}

		super.use();

		Camera.instance.shake(2);

		float a = (float) (this.owner.getAngleTo(Input.instance.worldMouse.x, Input.instance.worldMouse.y) - Math.PI * 2);

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

		this.sendBullets();
	}

	protected void sendBullets() {
		float a = (float) (this.owner.getAngleTo(Input.instance.worldMouse.x, Input.instance.worldMouse.y) - Math.PI * 2);

		this.sendBullet((float) (a + Math.toRadians(Random.newFloat(-this.accuracy, this.accuracy))));
	}

	protected void sendBullet(float an) {
		Player player = (Player) this.owner;
		TextureRegion sprite = this.getSprite();

		BulletEntity bullet = new BulletEntity();
		float a = (float) Math.toDegrees(an);

		Bullet b = ((Bullet) player.getInventory().remove(Bullet.class));

		bullet.sprite = Graphics.getTexture("bullet (bullet " + b.bulletName + ")");

		float x = this.owner.x + this.owner.w / 2 + (this.owner.isFlipped() ? -7 : 7) + 3 - 2;
		float y = this.owner.y + this.owner.h / 4 + region.getRegionHeight() / 2 - 2;

		float px = sprite.getRegionWidth();
		float py = sprite.getRegionHeight();

		float w = px + bullet.sprite.getRegionWidth() / 2;
		float h = py + bullet.sprite.getRegionHeight() / 2;

		px = (float) Math.cos(an);
		py = (float) Math.sin(an);

		bullet.x = x + px * w;
		bullet.y = y + py * h;
		bullet.damage = b.damage;

		float s = 6f;

		bullet.vel = new Point(
			px * s, py * s
		);

		bullet.a = a;

		Dungeon.area.add(bullet);
	}
}