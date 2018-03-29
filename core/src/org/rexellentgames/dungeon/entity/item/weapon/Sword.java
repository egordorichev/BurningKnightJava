package org.rexellentgames.dungeon.entity.item.weapon;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.Tween;

public class Sword extends Weapon {
	{
		name = "Sword";
		sprite = "item (iron sword)";
		damage = 3;
	}

	@Override
	public void render(float x, float y, float w, float h, boolean flipped) {
		float angle = this.added;

		if (this.owner != null) {
			if (this.owner instanceof Player) {
				float dx = this.owner.x + this.owner.w / 2 - Input.instance.worldMouse.x - 8;
				float dy = this.owner.y + this.owner.h / 2 - Input.instance.worldMouse.y - 8;
				float a = (float) Math.toDegrees(Math.atan2(dy, dx));

				angle += (flipped ? a : -a);
			} else {
				angle += (flipped ? 180 : 0); // ?!?!?
			}
		}

		angle = flipped ? angle : 180 - angle;

		TextureRegion sprite = this.getSprite();

		float xx = x + w / 2 + (flipped ? -w / 4 : w / 4);
		float yy = y + h / 4;

		Graphics.render(sprite, xx, yy,
			angle, sprite.getRegionWidth() / 2, 0, false, false);

		if (this.body != null) {
			float a = (float) Math.toRadians(angle);
			this.body.setTransform(xx, yy, a);
		}
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