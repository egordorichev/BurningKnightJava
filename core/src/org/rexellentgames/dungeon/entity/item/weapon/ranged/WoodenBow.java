package org.rexellentgames.dungeon.entity.item.weapon.ranged;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.game.input.Input;

public class WoodenBow extends Bow {
	{
		name = "Wooden Bow";
		sprite = "item (bow)";
		description = "Robin is good!";
		useTime = 0.4f;
		damage = 6;
	}

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		TextureRegion s = this.getSprite();
		float dx = Input.instance.worldMouse.x - this.owner.x - this.owner.w / 2;
		float dy = Input.instance.worldMouse.y - this.owner.y - this.owner.h / 2;
		float a = (float) Math.toDegrees(Math.atan2(dy, dx));

		Graphics.render(s, x + w / 2, y + h / 2, a, -4, s.getRegionHeight() / 2, false, false);
	}
}