package org.rexellentgames.dungeon.entity.item.consumable.potion;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.item.consumable.Consumable;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Tween;

public class Potion extends Consumable {
	protected float added;
	protected PotionRegistry.Type type;

	public Potion() {
		this.type = PotionRegistry.types.get(this.getClass().getSimpleName());
		this.sprite = (short) this.type.getSprite();
		this.identified = PotionRegistry.identified.get(this.type);
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
		this.identify();

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

	@Override
	public String getName() {
		if (this.identified) {
			return super.getName();
		} else {
			String name = this.type.toString().toLowerCase();
			name = name.substring(0, 1).toUpperCase() + name.substring(1);

			return name + " Potion";
		}
	}

	@Override
	public void identify() {
		super.identify();
		PotionRegistry.identified.put(this.type, true);
	}
}