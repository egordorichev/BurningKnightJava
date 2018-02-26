package org.rexellentgames.dungeon.entity.item.weapon;

import com.badlogic.gdx.physics.box2d.Body;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.Tween;

public class Sword extends Weapon {
	{
		name = "Sword";
		sprite = 0;
	}

	protected float angle;
	protected float added;

	@Override
	public void render(float x, float y, boolean flipped) {
		float angle = (float) Math.toDegrees(Math.atan2(y - Input.instance.worldMouse.y,
			x - Input.instance.worldMouse.x)) + added;

		Graphics.render(Graphics.items, this.sprite, x, y + 1, 1, 1, angle, 8, 3, false,
			false);
	}

	@Override
	public void use() {
		super.use();

		Tween.to(new Tween.Task(150, 0.1f) {
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
				Tween.to(new Tween.Task(0, 0.1f) {
					@Override
					public float getValue() {
						return added;
					}

					@Override
					public void setValue(float value) {
						added = value;
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