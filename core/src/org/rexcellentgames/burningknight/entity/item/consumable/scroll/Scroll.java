package org.rexcellentgames.burningknight.entity.item.consumable.scroll;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.item.consumable.Consumable;

public class Scroll extends Consumable {
	{
		useTime = 1f;
	}

	public Scroll() {
		this.onPickup();
	}

	@Override
	public void onPickup() {

	}

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		if (this.delay > 0) {
			TextureRegion sprite = this.getSprite();he b

			// todo: fix this render
			Graphics.render(sprite, x + (flipped ? -w / 3 : w / 3), y + h / 3, 0, sprite.getRegionWidth() / 2,sprite.getRegionHeight() / 2,
				!flipped, false);
		}
	}

	@Override
	public void use() {
		Audio.playSfx("scroll");
		this.identify();
		super.use();
	}
}