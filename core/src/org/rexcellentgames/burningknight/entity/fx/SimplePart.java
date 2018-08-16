package org.rexcellentgames.burningknight.entity.fx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class SimplePart extends Entity {
	public static TextureRegion defaul = Graphics.getTexture("particle-bullet_trail");
	public TextureRegion texture = defaul;
	public Point vel;
	public float speed = 1f;
	public boolean shadow = true;
	private float s = 1f;

	@Override
	public void init() {
		this.alwaysActive = true;

		if (this.vel == null) {
			this.vel = new Point(
				Random.newFloat(-1f, 1f),
				Random.newFloat(-1f, 1f)
			);
		}

		this.vel.mul(60);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.x += this.vel.x * dt;
		this.y += this.vel.y * dt;

		this.vel.mul(0.95f);
		this.s -= dt;

		if (this.s <= 0f) {
			this.done = true;
		}
	}

	@Override
	public void render() {
		Graphics.render(texture, this.x, this.y, 0, texture.getRegionWidth() / 2, texture.getRegionHeight() / 2, false, false, s, s);
	}
}