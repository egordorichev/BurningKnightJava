package org.rexellentgames.dungeon.entity.level.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;

public class Prop extends SaveableEntity {
	protected String sprite;
	protected TextureRegion region;

	@Override
	public void init() {
		super.init();

		if (this.sprite != null) {
			region = Graphics.getTexture(sprite);
			this.w = region.getRegionWidth();
			this.h = region.getRegionHeight();
		}
	}

	@Override
	public void render() {
		Graphics.startShadows();
		Graphics.render(region, this.x, this.y + 0.3f, 0, 0, 0, false, false, 1, -1);
		Graphics.endShadows();
		Graphics.render(region, this.x, this.y);
	}
}