package org.rexcellentgames.burningknight.entity.fx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Random;

public class UpgradeFx extends Entity {
	private static TextureRegion small = Graphics.getTexture("particle-upgrade_big");
	private static TextureRegion huge = Graphics.getTexture("particle-upgrade_huge");

	private boolean second;
	private float tarScale;
	private float scale;
	private boolean big;
	private float val;
	private float speed;
	private float grow;

	@Override
	public void init() {
		super.init();

		grow = Random.newFloat(3f, 6f);
		speed = Random.newFloat(12, 24);
		val = Random.newFloat(0.5f, 1f);
		big = Random.chance(30);
		tarScale = Random.newFloat(0.5f, 1f);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.y += speed * dt;

		if (second) {
			this.scale -= dt / 2;
		} else {
			this.scale += dt * grow;

			if (this.scale >= this.tarScale) {
				this.scale = this.tarScale;
				this.second = true;
			}
		}

		if (this.scale <= 0f) {
			this.done = true;
		}
	}

	@Override
	public void render() {
		TextureRegion region = (big ? huge : small);
		Graphics.batch.setColor(val, val, val, 1);
		Graphics.render(region, this.x, this.y, 0, region.getRegionWidth() / 2, region.getRegionHeight() / 2, false, false, scale, scale);
		Graphics.batch.setColor(1, 1, 1, 1);
	}
}