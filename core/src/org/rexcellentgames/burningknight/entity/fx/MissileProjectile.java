package org.rexcellentgames.burningknight.entity.fx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Explosion;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.fx.RectFx;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class MissileProjectile extends BulletProjectile {
	public Point target;
	public Player to;
	public boolean up = true;
	private float startY;

	@Override
	public void init() {
		this.angle = (float) Math.PI;
		this.owner.playSfx("missile");
		this.startY = this.y;

		super.init();
		this.velocity.y = 300;
	}

	@Override
	protected void setup() {
		super.setup();

		sprite = Graphics.getTexture("bullet-missile_burning");
	}

	{
		depth = 32;
		alwaysRender = true;
	}

	@Override
	public void logic(float dt) {
		boolean dd = this.done;
		this.done = this.remove;

		if (broke) {
			if (!didDie) {
				didDie = true;
				this.death();
			}

			return;
		}

		if (this.done && !dd) {
			this.onDeath();

			for (int i = 0; i < 20; i++) {
				PoofFx part = new PoofFx();

				part.x = this.x;
				part.y = this.y;

				Dungeon.area.add(part);
			}
		}

		if (this.up) {
			if (this.y >= this.startY + Display.GAME_HEIGHT + 16) {
				this.up = false;
				this.velocity.y = -160;
				target = new Point(Player.instance.x + 8, Player.instance.y);
				this.x = target.x;
				this.y = target.y + Display.GAME_HEIGHT + 16;
			}
		} else {
			this.velocity.y -= dt * 148;
		}

		this.y += this.velocity.y * dt;

		World.checkLocked(this.body).setTransform(this.x, this.y + sprite.getRegionHeight() / 2, 0);
		this.body.setLinearVelocity(this.velocity);

		light.setPosition(x, y);

		if (!this.up && this.y <= target.y) {
			this.y = target.y;
			broke = true;
		}
	}

	@Override
	protected void onDeath() {
		super.onDeath();

		for (int i = 0; i < Random.newInt(2, 5); i++) {
			Explosion explosion = new Explosion(x + Random.newFloat(-16, 16), y + Random.newFloat(-16, 16));
			explosion.delay = Random.newFloat(0, 0.25f);
			Dungeon.area.add(explosion);
		}

		playSfx("explosion");

		for (int i = 0; i < 10; i++) {
			PoofFx fx = new PoofFx();

			fx.x = this.x;
			fx.y = this.y;

			Dungeon.area.add(fx);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		Camera.shake(3f);
	}

	@Override
	public void onCollision(Entity entity) {
		if (target != null && entity instanceof Player && this.y <= target.y + 16 && !this.up) {
			super.onCollision(entity);
		}
	}

	@Override
	public void update(float dt) {
		this.logic(dt);
	}

	@Override
	public void render() {
		TextureRegion reg = sprite;
		Texture texture = reg.getTexture();

		Graphics.batch.end();
		RectFx.shader.begin();
		RectFx.shader.setUniformf("white", (this.dissappearWithTime && this.t >= 4f && (this.t - 4f) % 0.3f > 0.15f) ? 1 : 0);

		RectFx.shader.setUniformf("r", 1f);
		RectFx.shader.setUniformf("g", 1f);
		RectFx.shader.setUniformf("b", 1f);
		RectFx.shader.setUniformf("a", 1);
		RectFx.shader.setUniformf("remove", 0f);

		RectFx.shader.setUniformf("pos", new Vector2(((float) reg.getRegionX()) / texture.getWidth(), ((float) reg.getRegionY()) / texture.getHeight()));
		RectFx.shader.setUniformf("size", new Vector2(((float) reg.getRegionWidth()) / texture.getWidth(), ((float) reg.getRegionHeight()) / texture.getHeight()));

		RectFx.shader.end();

		Graphics.batch.setShader(RectFx.shader);
		Graphics.batch.begin();

		Graphics.render(sprite, x, y + sprite.getRegionHeight() / 2, 0, sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2, false, false, 1, up ? 1 : -1);

		Graphics.batch.end();
		Graphics.batch.setShader(null);
		Graphics.batch.begin();
	}

	@Override
	public void renderShadow() {
	}
}