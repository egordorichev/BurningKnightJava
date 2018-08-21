package org.rexcellentgames.burningknight.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
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

		this.h = 24;

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
									public void onStart() {
										tweened = true;
									}

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
		});
	}

	private Color color = Color.valueOf("#1a1932");

	private float a;
	private static TextureRegion topLeft = Graphics.getTexture("ui-banner_top_left");
	private static TextureRegion top = Graphics.getTexture("ui-banner_top");
	private static TextureRegion topRight = Graphics.getTexture("ui-banner_top_right");

	private static TextureRegion left = Graphics.getTexture("ui-banner_left");
	private static TextureRegion center = Graphics.getTexture("ui-banner_center");
	private static TextureRegion right = Graphics.getTexture("ui-banner_right");

	private static TextureRegion bottomLeft = Graphics.getTexture("ui-banner_bottom_left");
	private static TextureRegion bottom = Graphics.getTexture("ui-banner_bottom");
	private static TextureRegion bottomRight = Graphics.getTexture("ui-banner_bottom_right");

	private boolean tweened;

	@Override
	public void update(float dt) {
		super.update(dt);

		if (!tweened && Dungeon.game.getState().isPaused()) {
			Tween.to(new Tween.Task(0, 0.1f) {
				@Override
				public float getValue() {
					return a;
				}

				@Override
				public void setValue(float value) {
					a = value;
				}

				@Override
				public boolean runWhenPaused() {
					return true;
				}

				@Override
				public void onEnd() {
					Tween.to(new Tween.Task(18, 0.2f) {
						@Override
						public float getValue() {
							return w;
						}

						@Override
						public boolean runWhenPaused() {
							return true;
						}

						@Override
						public void setValue(float value) {
							w = value;
						}

						@Override
						public void onEnd() {
							Tween.to(new Tween.Task(-68, 0.3f, Tween.Type.BACK_IN) {
								@Override
								public float getValue() {
									return y;
								}

								@Override
								public boolean runWhenPaused() {
									return true;
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
			});
		}
	}

	@Override
	public void render() {
		float x = Display.GAME_WIDTH / 2 - this.w / 2;
		float y = this.y + 48;

		float sx = (this.w - 18);
		float sy = (this.h - 13);

		Graphics.render(bottomLeft, x, y);
		Graphics.render(bottom, x + bottomLeft.getRegionWidth(), y + 4, 0, 0, 0, false, false, sx, 1);
		Graphics.render(bottomRight, x + this.w - bottomRight.getRegionWidth(), y);

		Graphics.render(left, x, y + bottomLeft.getRegionHeight(), 0, 0, 0,  false, false, 1, sy);
		Graphics.render(center, x + left.getRegionWidth(), y + bottomLeft.getRegionHeight(), 0, 0, 0,  false, false, sx, sy);
		Graphics.render(right, x + this.w - right.getRegionWidth(), y + bottomLeft.getRegionHeight(), 0, 0, 0,  false, false, 1, sy);

		Graphics.render(topLeft, x, y + h - topLeft.getRegionHeight());
		Graphics.render(top, x + topLeft.getRegionWidth(), y + h - topLeft.getRegionHeight(), 0, 0, 0, false, false, sx, 1);
		Graphics.render(topRight, x + this.w - topRight.getRegionWidth(), y + h - topLeft.getRegionHeight());

		if (this.a > 0) {
			Graphics.smallSimple.setColor(color.r, color.g, color.b, this.a);
			Graphics.smallSimple.draw(Graphics.batch, this.text, Display.GAME_WIDTH / 2 - (this.w1) / 2, this.y + 48 + this.h - 8);

			if (this.extra != null) {
				Graphics.smallSimple.draw(Graphics.batch, this.extra, Display.GAME_WIDTH / 2 - (this.w2) / 2, this.y + 48 + this.h - 8 - 12);
			}

			Graphics.smallSimple.setColor(1, 1, 1, 1);
		}
	}
}