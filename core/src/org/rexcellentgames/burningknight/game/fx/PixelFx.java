package org.rexcellentgames.burningknight.game.fx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class PixelFx extends Entity {
	{
		alwaysActive = true;
		alwaysRender = true;
	}

	private static TextureRegion region = Graphics.getTexture("particle-rect");

	private float t;
	public float r;
	public float g;
	public float b;
	private float a = 1f;
	private Point vel = new Point();

	@Override
	public void init() {
		super.init();

		float a = Random.newFloat((float) (Math.PI * 2));
		vel.x = (float) Math.cos(a) * 60f;
		vel.y = (float) Math.sin(a) * 60f;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.t += dt;

		if (this.t <= 1f) {
			return;
		}

		this.a -= dt / 3;

		this.x += this.vel.x * dt;
		this.y += this.vel.y * dt;

		this.vel.mul(0.97f);

		if (this.a <= 0) {
			this.done = true;
		}
	}

	@Override
	public void render() {
		Graphics.batch.setColor(r, g, b, a);
		Graphics.render(region, this.x, this.y, 0, 0, 0, false, false, 0.25f, 0.25f);
		Graphics.batch.setColor(1, 1, 1, 1);
	}
}