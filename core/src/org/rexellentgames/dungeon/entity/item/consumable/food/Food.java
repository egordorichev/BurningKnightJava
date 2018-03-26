package org.rexellentgames.dungeon.entity.item.consumable.food;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.consumable.Consumable;
import org.rexellentgames.dungeon.util.Tween;

public class Food extends Consumable {
	protected float added;
	protected int hunger;

	{
		useTime = 10f;
		hunger = 10;
		identified = true;
	}

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		if (this.added != 0) {
			float angle = (flipped ? this.added : -this.added);
			TextureRegion sprite = this.getSprite();

			Graphics.render(sprite, x + (flipped ? w / 3 : w / 3 * 2), y, angle, 0,
				0, false, false);
		}
	}

	@Override
	public void use() {
		super.use();

		if (this.owner instanceof Player) {
			Player player = (Player) this.owner;
			player.setHunger(player.getHunger() - this.hunger);
		}

		Tween.to(new Tween.Task(-70, 0.3f) {
			@Override
			public float getValue() {
				return added;
			}

			@Override
			public void setValue(float value) {
				added = value;
			}

			@Override
			public void onEnd() {
				count -= 1;
				added = 0;
			}
		});
	}
}