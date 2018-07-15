package org.rexcellentgames.burningknight.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.assets.Graphics;
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

		this.w = 18;
		Graphics.layout.setText(Graphics.smallSimple, this.text);
		this.w1 = Graphics.layout.width;

		if (this.extra != null) {
			Graphics.layout.setText(Graphics.smallSimple, this.extra);
			this.w2 = Graphics.layout.width;
			this.h += Graphics.layout.height + 4;
		}

		this.y = -128;

		Tween.to(new Tween.Task(0, 0.6f, Tween.Type.BACK_OUT) {
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
				Tween.to(new Tween.Task(Math.max(w2, w1) + 18 + 4, 0.5f) {
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
										Tween.to(new Tween.Task(18, 0.5f) {
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
												Tween.to(new Tween.Task(-68, 0.5f, Tween.Type.BACK_IN) {
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
												});
											}
										});
									}
								}).delay(3f);
							}
						});
					}
				});
			}
		}).delay(1f);
	}

	private Color color = Color.valueOf("#1a1932");

	private float a;
	private static TextureRegion left = Graphics.getTexture("ui-banner_left");
	private static TextureRegion center = Graphics.getTexture("ui-banner_center");
	private static TextureRegion right = Graphics.getTexture("ui-banner_right");

	@Override
	public void render() {
		/*Graphics.startShape();
		Graphics.shape.setProjectionMatrix(Camera.ui.combined);
		Graphics.shape.setColor(1, 1, 1, 1);
		Graphics.shape.rect(Display.GAME_WIDTH / 2 - this.w / 2, this.y + 48, this.w, this.h);
		Graphics.endShape();*/

		// Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		float x = Display.GAME_WIDTH / 2 - this.w / 2;
		float y = this.y + 48;

		Graphics.render(left, x, y);
		Graphics.render(center, x + 9, y + 4, 0, 0, 0, false, false, (this.w - 18), 1);
		Graphics.render(right, x + this.w - 9, y);

		if (this.a > 0) {
			Graphics.smallSimple.setColor(color.r, color.g, color.b, this.a);
			Graphics.smallSimple.draw(Graphics.batch, this.text, Display.GAME_WIDTH / 2 - (this.w1) / 2, this.y + 48 + this.h);

			if (this.extra != null) {
				Graphics.smallSimple.draw(Graphics.batch, this.extra, Display.GAME_WIDTH / 2 - (this.w2) / 2, this.y + 48 + 12);
			}

			Graphics.smallSimple.setColor(1, 1, 1, 1);
		}
	}
}