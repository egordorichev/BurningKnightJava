package org.rexellentgames.dungeon.entity.item.weapon;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.Tween;

public class Sword extends Weapon {
	{
		name = "Sword";
		sprite = "item (sword)";
		damage = 3;
	}

	protected float added;

	@Override
	public void render(float x, float y, boolean flipped) {
		float angle = this.added;

		float w = 16;

		if (this.owner != null) {
			w = this.owner.w;

			if (this.owner instanceof Player) {
				float dx = this.owner.x + this.owner.w / 2 - Input.instance.worldMouse.x - 8;
				float dy = this.owner.y + this.owner.h / 2 - Input.instance.worldMouse.y - 8;
				float a = (float) Math.toDegrees(Math.atan2(dy, dx));

				angle += (flipped ? a : -a);
			}
		}

		Graphics.render(this.getSprite(), x + (flipped ? -w / 4 : w / 4) + (w - 16) / 2, y + 1 + (w - 16) / 3,
			flipped ? angle : 180 - angle, 8, 2, false, false);
	}

	@Override
	public void use() {
		super.use();

		Tween.to(new Tween.Task(180, this.useTime / 3) {
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