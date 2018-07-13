package org.rexcellentgames.burningknight.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.util.CollisionHelper;
import org.rexcellentgames.burningknight.util.Tween;

public class UiButton extends UiEntity {
	public int h;
	public int w;

	protected boolean hover;
	protected float scale = 1f;
	private Tween.Task last;
	protected String label;
	protected float mx = 3f;
	public boolean sparks;
	protected float r = 1f;
	protected float g = 1f;
	protected float b = 1f;
	protected float rr = 1f;
	protected float rg = 1f;
	protected float rb = 1f;
	protected float ar = 1f;
	protected float ag = 1f;
	protected float ab = 1f;
	private boolean disableClick;

	public UiButton(String label, int x, int y) {
		this.setLabel(label);

		this.y = y;
		this.x = x;
	}

	public UiButton(String label, int x, int y, boolean disableClick) {
		this.setLabel(label);

		this.y = y;
		this.x = x;
		this.disableClick = disableClick;
	}

	public UiButton setSparks(boolean sparks) {
		this.sparks = sparks;
		return this;
	}
	
	public void setLabel(String label) {
		if (label == null) {
			return;
		}

		if (Locale.has(label)) {
			label = Locale.get(label);
		}

		this.label = label;

		Graphics.layout.setText(Graphics.medium, this.label);

		this.w = (int) Graphics.layout.width;
		this.h = (int) Graphics.layout.height * 2;
	}

	protected float scaleMod = 1;

	@Override
	public void render() {
		Graphics.batch.setColor(this.rr * this.ar, this.rg * this.ag, this.rb * this.ab, 1);

		Graphics.batch.end();
		Graphics.surface.end();
		Graphics.text.begin();
		Graphics.batch.begin();

		Graphics.batch.setProjectionMatrix(Camera.nil.combined);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
		Graphics.medium.draw(Graphics.batch, this.label, 2, 16);

		Graphics.batch.end();
		Graphics.text.end();
		Graphics.surface.begin();
		Graphics.batch.begin();
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		Texture texture = Graphics.text.getColorBufferTexture();
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		Graphics.batch.draw(texture, this.x - this.w / 2 + 2, this.y - this.h / 2, this.w / 2 + 4, this.h / 2,
			this.w, this.h, this.scale, this.scale, (float) (Math.cos(this.y / 12 + Dungeon.time * 6) * (this.mx / this.w * 20)),
			0, 0, this.w + 4, this.h, false, true);

		Graphics.batch.setColor(1, 1, 1, 1);
	}

	protected boolean playSfx = true;

	public UiButton setPlaySfx(boolean playSfx) {
		this.playSfx = playSfx;
		return this;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (Input.instance.wasPressed("uiAccept") && !disableClick) {
			if ((this.hover && !Input.instance.wasPressed("mouse0") && this.isSelected) || (checkHover() && Input.instance.wasPressed("mouse0"))) {
				if (this.last != null) {
					Tween.remove(this.last);
				}

				this.rr = 1f;
				this.rg = 1f;
				this.rb = 1f;

				this.last = Tween.to(new Tween.Task(1f - 0.2f * scaleMod, 0.05f) {
					@Override
					public float getValue() {
						return scale;
					}

					@Override
					public void setValue(float value) {
						scale = value;
					}

					@Override
					public void onEnd() {
						super.onEnd();
						last = null;
						onClick();

						r = 0.7f;
						g = 0.7f;
						b = 0.7f;

						last = Tween.to(new Tween.Task(1 + 0.2f * scaleMod, 0.05f) {
							@Override
							public float getValue() {
								return scale;
							}

							@Override
							public void setValue(float value) {
								scale = value;
							}

							@Override
							public void onEnd() {
								super.onEnd();
								last = null;
							}

							@Override
							public boolean runWhenPaused() {
								return true;
							}
						});
					}

					@Override
					public boolean runWhenPaused() {
						return true;
					}
				});
			}
		}

		if (this.sparks) {
			// still wrong
			// Spark.random(this.x - this.w / 2, this.y - this.h / 4, this.w, this.h / 2, true);
		}

		this.rr += (this.r - this.rr) * dt * 10;
		this.rg += (this.g - this.rg) * dt * 10;
		this.rb += (this.b - this.rb) * dt * 10;

		boolean h = this.hover;
		this.hover = checkHover() || isSelected;

		if (h && !this.hover) {
			if (this.last != null) {
				Tween.remove(this.last);
			}

			this.r = 1f;
			this.g = 1f;
			this.b = 1f;

			this.last = Tween.to(new Tween.Task(1f, 0.1f) {
				@Override
				public float getValue() {
					return scale;
				}

				@Override
				public void setValue(float value) {
					scale = value;
				}

				@Override
				public void onEnd() {
					super.onEnd();
					last = null;
				}

				@Override
				public boolean runWhenPaused() {
					return true;
				}
			});

			Tween.to(new Tween.Task(3, 0.1f) {
				@Override
				public float getValue() {
					return mx;
				}

				@Override
				public void setValue(float value) {
					mx = value;
				}

				@Override
				public boolean runWhenPaused() {
					return true;
				}
			});

		} else if ((!h && this.hover)) {
			if (this.last != null) {
				Tween.remove(this.last);
			}

			this.r = 0.7f;
			this.g = 0.7f;
			this.b = 0.7f;

			Audio.playSfx("menu/moving");

			Tween.to(new Tween.Task(20, 0.1f) {
				@Override
				public float getValue() {
					return mx;
				}

				@Override
				public void setValue(float value) {
					mx = value;
				}

				@Override
				public boolean runWhenPaused() {
					return true;
				}
			});

			this.last = Tween.to(new Tween.Task(1f + 0.2f * scaleMod, 0.1f) {
				@Override
				public float getValue() {
					return scale;
				}

				@Override
				public void setValue(float value) {
					scale = value;
				}

				@Override
				public void onEnd() {
					super.onEnd();
					last = null;
				}

				@Override
				public boolean runWhenPaused() {
					return true;
				}
			});
		}


		if (this.hover) {
			this.area.select(this);
		}

	}

	public void onClick() {
		if (this.playSfx) {
			Audio.playSfx("menu/select");
		}
	}
	
	private boolean checkHover() {
		return CollisionHelper.check((int) Input.instance.uiMouse.x, (int) Input.instance.uiMouse.y,
			(int) (this.x - this.w / 2 * 1.2f),
			(int) (this.y - this.h / 2),
			(int) (this.w * 1.2f), this.h);
	}
}