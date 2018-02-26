package org.rexellentgames.dungeon.entity.creature.fx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.Tween;

public class HpFx extends Entity {
	public static Color bad = Color.valueOf("#ac3232");
	public static Color good = Color.valueOf("#99e550");

	private String text;
	private boolean low;
	private float a = 1f;

	public HpFx(Creature creature, int change) {
		this.text = String.valueOf(change);

		GlyphLayout layout = new GlyphLayout(Graphics.small, this.text);

		this.x = creature.x + creature.w - layout.width / 2 - 6 + Random.newInt(3);
		this.y = creature.y + creature.h - 4;
		this.low = change < 0;
		this.depth = 10;

		Tween.to(new Tween.Task(this.y + 32, 0.5f) {
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
				Tween.to(new Tween.Task(0, 0.3f) {
					@Override
					public void setValue(float value) {
						a = value;
					}

					@Override
					public float getValue() {
						return a;
					}

					@Override
					public void onEnd() {
						done = true;
					}
				});
			}
		});
	}

	@Override
	public void render() {
		Color color = this.low ? bad : good;

		Graphics.small.setColor(color.r, color.g, color.b, this.a);
		Graphics.small.draw(Graphics.batch, this.text, this.x, this.y);
		Graphics.small.setColor(1, 1, 1, this.a);
	}
}