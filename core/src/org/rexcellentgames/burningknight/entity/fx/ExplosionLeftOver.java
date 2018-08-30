package org.rexcellentgames.burningknight.entity.fx;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Random;

public class ExplosionLeftOver extends Entity {
	{
		depth = -9;
	}

	private static TextureRegion region = Graphics.getTexture("explosion-boom-00");
	private float a;

	@Override
	public void init() {
		super.init();
		this.x -= region.getRegionWidth() / 2;
		this.y -= region.getRegionHeight() / 2;
		this.a = Random.newFloat(360f);
	}

	@Override
	public void render() {
		Graphics.render(region, this.x + region.getRegionWidth() / 2, this.y + region.getRegionHeight() / 2, this.a, region.getRegionWidth() / 2, region.getRegionHeight() / 2, false, false);
	}
}