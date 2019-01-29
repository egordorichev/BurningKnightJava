package org.rexcellentgames.burningknight.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.util.Tween;

public class UiAchievement extends Entity {
	{
		alwaysActive = true;
		alwaysRender = true;
		depth = 32;
	}

	public String text;
	public String extra;
	public boolean unlock;

	private float w2;

	private static TextureRegion left = Graphics.getTexture("ui-achievement_left");
	private static TextureRegion center = Graphics.getTexture("ui-achievement_center");
	private static TextureRegion right = Graphics.getTexture("ui-achievement_right");
	private static TextureRegion ach = Graphics.getTexture("ui-ach");
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
		this.w = Math.max(this.w2, w1) + (icon == Item.missing ? 3 : 32) + 9 + 5;
		this.y = -this.h * 3;
		this.x = Display.UI_WIDTH - 2 - this.w;

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
			public boolean runWhenPaused() {
				return true;
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
					public boolean runWhenPaused() {
						return true;
					}

					@Override
					public void onEnd() {
						setDone(true);
					}
				}).delay(10);
			}
		});
	}

	@Override
	public void render() {
		if (Ui.hideUi) {
			return;
		}

		boolean missing = icon == Item.missing;

		Graphics.batch.setColor(1, 1, 1, 1);

		if (unlock ) {
			Graphics.startShape();
			Graphics.shape.setColor(0.3f, 0.3f, 0.3f, 1f);
			Graphics.shape.rect(this.x + 3, this.y + 3, 32, 32);
			Graphics.endShape();

			Graphics.render(icon, this.x + 3 + (32 - icon.getRegionWidth()) / 2, this.y + 3 + (32 - icon.getRegionHeight()) / 2);
		} else {
			if (!missing) {
				Graphics.render(icon, this.x + 3, this.y + 3);
			}
		}

		Graphics.render(missing ? ach : left, this.x, this.y);
		Graphics.render(center, this.x + (missing ? 3 : 38), this.y, 0, 0, 0, false, false, (this.w - (missing ? 21 : 57)), 1);
		Graphics.render(right, this.x + this.w - 19, this.y);

		float m = missing ? 0 : 32;

		if (this.extra != null) {
			Graphics.small.draw(Graphics.batch, this.text, this.x + m + 6 + 3, this.y + this.h - 4 - 4 - 2);
			Graphics.small.draw(Graphics.batch, this.extra, this.x + m + 6 + 3, this.y + this.h - 3 - 16 - 2);
		} else {
			Graphics.small.draw(Graphics.batch, this.text, this.x + m + 6 + 3, this.y + this.h - 4 - 4 - 2 - 6);
		}
	}
}