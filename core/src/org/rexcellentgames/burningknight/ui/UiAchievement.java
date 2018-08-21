package org.rexcellentgames.burningknight.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Tween;

// fixme: displayed under black bars, or not displayed at all
// fixme: hide ui banner, when dead or in pause, show depth / time in pause

public class UiAchievement extends Entity {
	{
		alwaysActive = true;
		alwaysRender = true;
		depth = 32;
	}

	public String text;
	public String extra;

	private float w2;

	private static TextureRegion left = Graphics.getTexture("ui-achievement_left");
	private static TextureRegion center = Graphics.getTexture("ui-achievement_center");
	private static TextureRegion right = Graphics.getTexture("ui-achievement_right");
	public TextureRegion icon;

	@Override
	public void init() {
		super.init();

		this.w = 0;
		Graphics.layout.setText(Graphics.small, this.text);
		float w1 = Graphics.layout.width;

		if (this.extra != null) {
			Graphics.layout.setText(Graphics.small, this.extra);
			this.w2 = Graphics.layout.width;
		}

		this.h = 38;
		this.w = Math.max(this.w2, w1) + 32 + 9 + 5;
		this.y = -this.h * 3;
		this.x = Display.GAME_WIDTH - 2 - this.w;

		Tween.to(new Tween.Task(2, 0.5f, Tween.Type.BACK_OUT) {
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
				Tween.to(new Tween.Task(-h, 0.5f, Tween.Type.BACK_OUT) {
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
						setDone(true);
					}
				}).delay(10);
			}
		});
	}

	private float a = 1;

	@Override
	public void render() {
		Graphics.render(icon, this.x + 3, this.y + 3);

		Graphics.render(left, this.x, this.y);
		Graphics.render(center, this.x + 38, this.y, 0, 0, 0, false, false, (this.w - 57), 1);
		Graphics.render(right, this.x + this.w - 19, this.y);

		Graphics.small.draw(Graphics.batch, this.text, this.x + 38 + 3, this.y + this.h - 4 - 4 - 2);
		Graphics.small.draw(Graphics.batch, this.extra, this.x + 38 + 3, this.y + this.h - 3 - 16 - 2);
	}
}