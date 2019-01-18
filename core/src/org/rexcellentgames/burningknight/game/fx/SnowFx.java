package org.rexcellentgames.burningknight.game.fx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Random;

public class SnowFx extends Entity {
	{
		alwaysActive = true;
		alwaysRender = true;
		depth = 30;
	}

	private static TextureRegion region = Graphics.getTexture("particle-big");
	private float scale;
	private float t;
	private float vl;
	private float fl;
	private float rot;
	private float mod;

	@Override
	public void init() {
		super.init();
		x = Random.newFloat(-Display.GAME_WIDTH, Display.GAME_WIDTH / 2) + Camera.game.position.x;
		y = Camera.game.position.y + Display.GAME_HEIGHT / 2;
		scale = Random.newFloat(0.2f, 0.8f);
		al = Random.newFloat(0.3f, 0.8f);
		vl = Random.newFloat(0.5f, 1f);
		t = Random.newFloat(32);
		mod = Random.newFloat(0.2f, 1f);
		fl = Random.newFloat(0.5f, 1.5f);
		rot = Random.newFloat(-1, 1);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.y <= this.tar) {
			this.y = this.tar;
			this.onTar += dt;

			if (onTar > 1f) {
				al -= dt * 3;

				if (al <= 0) {
					done = true;
				}
			}
		} else {
			this.t += dt;
			this.x += Math.cos(this.t * this.fl) * this.mod;
			this.y -= this.scale * dt * 60;
		}
	}

	public float tar;
	private float al = 1;
	private float onTar;

	@Override
	public void render() {
		Graphics.batch.setColor(vl, vl, vl, al);
		Graphics.render(region,  this.x, this.y, this.t * rot * 512, 3, 3, false, false, scale, scale);
		Graphics.batch.setColor(1, 1, 1, 1);
	}
}