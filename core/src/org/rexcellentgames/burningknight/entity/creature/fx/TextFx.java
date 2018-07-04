package org.rexcellentgames.burningknight.entity.creature.fx;

import com.badlogic.gdx.graphics.Color;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Tween;

public class TextFx extends Entity {
	private String text;
	private Color color = Color.WHITE;
	private float a = 1f;

	public TextFx(String text, float x, float yv) {
		this.text = text;
		this.depth = 15;

		Graphics.layout.setText(Graphics.medium, this.text);

		this.x = x - Graphics.layout.width / 2;
		this.y = yv;

		Tween.to(new Tween.Task(this.y + 8, 0.3f) {
			@Override
			public float getValue() {
				return y;
			}

			@Override
			public void setValue(float value) {
				y = value;
			}

			@Override
			public void onEnd() {
				Tween.to(new Tween.Task(0, 0.5f) {
					@Override
					public float getValue() {
						return a;
					}

					@Override
					public void setValue(float value) {
						a = value;
					}

					@Override
					public void onEnd() {
						done = true;
					}
				});
			}
		});
	}

	public TextFx(String text, Entity entity) {
		this(text, entity.x + entity.w / 2, entity.y + entity.h - 4);
	}

	public TextFx setColor(Color color) {
		this.color = color;
		return this;
	}

	@Override
	public void render() {
		Graphics.medium.setColor(this.color.r, this.color.g, this.color.b, this.a);
		Graphics.write(this.text, Graphics.medium, this.x, this.y);
		Graphics.medium.setColor(1, 1, 1, 1);
	}
}