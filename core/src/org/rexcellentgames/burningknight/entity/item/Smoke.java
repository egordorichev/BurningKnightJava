package org.rexcellentgames.burningknight.entity.item;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;

public class Smoke extends Entity {
	private AnimationData animation = Explosion.boom.get("smoke");
	public float delay;
	private float r;
	private float g;
	private float b;

	{
		depth = 31;
	}

	public Smoke(float x, float y) {
		this.x = x;
		this.y = y;
		this.alwaysActive = true;
	}

	@Override
	public void init() {
		super.init();

		this.r = Random.newFloat(0.6f, 1f);
		this.g = Random.newFloat(0.6f, 1f);
		this.b = Random.newFloat(0.6f, 1f);
	}

	@Override
	public void update(float dt) {
		if (this.delay > 0) {
			this.delay -= dt;
			return;
		}

		if (this.animation.update(dt)) {
			this.done = true;
		}
	}

	@Override
	public void render() {
		if (this.delay > 0) {
			return;
		}

		TextureRegion region = this.animation.getCurrent().frame;
		Graphics.batch.setColor(r, g, b, 1);
		this.animation.render(this.x, this.y, false, false, region.getRegionWidth() / 2, region.getRegionHeight() / 2, 0);
	}
}