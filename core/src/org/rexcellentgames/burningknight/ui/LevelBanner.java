package org.rexcellentgames.burningknight.ui;

import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Tween;

public class LevelBanner extends Entity {
	{
		alwaysActive = true;
		alwaysRender = true;
		depth = 32;
	}

	public String text;

	@Override
	public void init() {
		super.init();

		this.w = 0;
		Graphics.layout.setText(Graphics.small, this.text);

		Tween.to(new Tween.Task(Graphics.layout.width + 16, 0.5f) {
			@Override
			public float getValue() {
				return w;
			}

			@Override
			public void setValue(float value) {
				w = value;
			}

			@Override
			public void onEnd() {
				Tween.to(new Tween.Task(1, 0.2f) {
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
						Tween.to(new Tween.Task(0, 0.2f) {
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
								Tween.to(new Tween.Task(0, 0.5f) {
									@Override
									public float getValue() {
										return w;
									}

									@Override
									public void setValue(float value) {
										w = value;
									}

									@Override
									public void onEnd() {
										setDone(true);
									}
								});
							}
						}).delay(2f);
					}
				});
			}
		}).delay(1f);
	}

	private float a;

	@Override
	public void render() {
		Graphics.startShape();
		Graphics.shape.setProjectionMatrix(Camera.ui.combined);
		Graphics.shape.setColor(1, 1, 1, 1);
		Graphics.shape.rect(Display.GAME_WIDTH / 2 - this.w / 2, 48, this.w, this.h);
		Graphics.endShape();

		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		if (this.a > 0) {
			Graphics.small.setColor(1, 1, 1, this.a);
			Graphics.small.draw(Graphics.batch, this.text, Display.GAME_WIDTH / 2 - this.w / 2 + 8, 48 + 12);
			Graphics.small.setColor(1, 1, 1, 1);
		}
	}
}