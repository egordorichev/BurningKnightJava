package org.rexcellentgames.burningknight.entity.item;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Random;

public class TinyParticle extends Entity {
	private AnimationData animation = Explosion.boom.get("particle");
	private float r;
	private float g;
	private float b;
	private float a;

	public TinyParticle(float x, float y) {
		this.x = x;
		this.y = y;
		this.alwaysActive = true;
	}

	@Override
	public void init() {
		super.init();

		this.r = Random.newFloat(0.8f, 1f);
		this.g = Random.newFloat(0.8f, 1f);
		this.b = Random.newFloat(0.8f, 1f);
		this.a = Random.newFloat(360f);
	}

	@Override
	public void update(float dt) {
		if (this.animation.update(dt)) {
			this.done = true;
		}
	}

	@Override
	public void render() {
		TextureRegion region = this.animation.getCurrent().frame;
		Graphics.batch.setColor(r, g, b, 1);

		float w = region.getRegionWidth() / 2;
		float h = region.getRegionHeight() / 2;

		this.animation.render(this.x - w, this.y - h, false, false, w, h, this.a);
	}
}