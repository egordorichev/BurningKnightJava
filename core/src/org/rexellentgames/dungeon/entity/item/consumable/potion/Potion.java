package org.rexellentgames.dungeon.entity.item.consumable.potion;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.item.consumable.Consumable;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Tween;

public class Potion extends Consumable {
	protected float added;

	public Potion() {
		Log.info(this.getClass().getSimpleName());
		this.sprite = (short) PotionRegistry.types.get(this.getClass().getSimpleName()).getSprite();
	}

	@Override
	public void render(float x, float y, boolean flipped) {
		if (this.added != 0) {
			float angle = (flipped ? this.added : -this.added);

			Graphics.render(Graphics.items, this.sprite, x + (flipped ? -3 : 3), y - 4, 1, 1, angle, 8, 10, false,
				false);
		}
	}

	@Override
	public void use() {
		super.use();

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