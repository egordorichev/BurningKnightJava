package org.rexellentgames.dungeon.entity.level.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;

public class Statue extends SaveableEntity {
	private static TextureRegion region = Graphics.getTexture("biome-0 (statue A)");

	@Override
	public void init() {
		super.init();

		this.w = region.getRegionWidth();
		this.h = region.getRegionHeight();
	}

	@Override
	public void render() {
		Graphics.startShadows();
		Graphics.render(region, this.x, this.y + 0.3f, 0, 0, 0, false, false, 1, -1);
		Graphics.endShadows();
		Graphics.render(region, this.x, this.y);
	}
}