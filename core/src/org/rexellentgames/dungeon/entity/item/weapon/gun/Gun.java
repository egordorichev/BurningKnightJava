package org.rexellentgames.dungeon.entity.item.weapon.gun;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.item.Item;

public class Gun extends Item {
	{
		identified = true;
		auto = true;
	}

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		TextureRegion sprite = this.getSprite();

		Graphics.render(sprite, x + w / 2, h, 0, sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2, false, false);
	}
}