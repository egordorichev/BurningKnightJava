package org.rexellentgames.dungeon.entity.item.weapon;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.util.Tween;

public class Sword extends Weapon {
	{
		name = "Sword";
		sprite = 0;
		damage = 3;
	}

	protected float added;

	@Override
	public void render(float x, float y, boolean flipped) {
		float angle = (flipped ? this.added : -this.added);

		float w = 16;

		if (this.owner != null) {
			w = this.owner.w;
		}

		Graphics.render(Graphics.items, this.sprite, x + (flipped ? -w / 4 : w / 4) + (w - 16) / 2, y + 1 + (w - 16) / 3, 1, 1, angle, 8, 1, false,
			false);
	}

	@Override
	public void use() {
		super.use();

		Tween.to(new Tween.Task(150, this.useTime / 3) {
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
				Tween.to(new Tween.Task(0, useTime / 2) {
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
						endUse();
					}
				});
			}
		});
	}

	@Override
	public void secondUse() {
		super.secondUse();
	}
}