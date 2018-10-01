package org.rexcellentgames.burningknight.game.fx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Random;

public class WindFx extends Entity {
	{
		alwaysActive = true;
		alwaysRender = true;
		depth = 30;
	}

	private static TextureRegion region = Graphics.getTexture("particle-big");
	private float scale;
	private float al;
	private float t;
	private float vl;
	private float fl;
	private float rot;
	private float mod;

	@Override
	public void init() {
		super.init();
		recreate();
		x = Random.newFloat(-Display.GAME_WIDTH, Display.GAME_WIDTH / 2) + Camera.game.position.x;
	}

	private void recreate() {
		scale = Random.newFloat(0.2f, 0.8f);
		al = Random.newFloat(0.3f, 0.8f);
		vl = Random.newFloat(0.5f, 1f);
		t = Random.newFloat(32);
		mod = Random.newFloat(0.5f, 1f);
		fl = Random.newFloat(0.5f, 1f);
		rot = Random.newFloat(-1, 1);

		x = Camera.game.position.x - Random.newFloat(Display.GAME_WIDTH / 2, Display.GAME_WIDTH) - 3;
		y = Random.newFloat(-Display.UI_HEIGHT / 2, Display.UI_HEIGHT / 2) + Camera.game.position.y;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.t += dt;
		this.x += this.scale * dt * 60;
		this.y += Math.cos(this.t * this.fl) * this.mod;

		if (this.x >= Camera.game.position.x + Display.GAME_WIDTH / 2 || this.x < Camera.game.position.x - Display.GAME_WIDTH - 10) {
			this.recreate();
		}
	}

	@Override
	public void render() {
		Graphics.batch.setColor(vl, vl, vl, al);
		Graphics.render(region,  this.x, this.y, this.t * rot * 512, 3, 3, false, false, scale, scale);
		Graphics.batch.setColor(1, 1, 1, 1);
	}
}