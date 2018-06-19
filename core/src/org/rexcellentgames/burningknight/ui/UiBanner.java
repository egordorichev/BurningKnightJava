package org.rexcellentgames.burningknight.ui;

import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.util.Tween;

public class UiBanner extends Entity {
	{
		alwaysActive = true;
		alwaysRender = true;
		depth = 32;
	}

	public String text;
	public String extra;

	private float w1;
	private float w2;

	@Override
	public void init() {
		super.init();

		this.w = 0;
		Graphics.layout.setText(Graphics.small, this.text);
		this.w1 = Graphics.layout.width;

		if (this.extra != null) {
			Graphics.layout.setText(Graphics.small, this.extra);
			this.w2 = Graphics.layout.width;
			this.h += Graphics.layout.height + 4;
		}

		Tween.to(new Tween.Task(Math.max(this.w2, this.w1) + 16, 0.5f) {
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
		});
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
			Graphics.small.draw(Graphics.batch, this.text, Display.GAME_WIDTH / 2 - (this.w1) / 2, 48 + this.h - 4);

			if (this.extra != null) {
				Graphics.small.draw(Graphics.batch, this.extra, Display.GAME_WIDTH / 2 - (this.w2) / 2, 48 + 12);
			}

			Graphics.small.setColor(1, 1, 1, 1);
		}
	}
}