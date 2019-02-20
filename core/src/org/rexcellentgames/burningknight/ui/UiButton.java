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
import org.rexcellentgames.burningknight.game.state.InGameState;
import org.rexcellentgames.burningknight.util.CollisionHelper;
import org.rexcellentgames.burningknight.util.Tween;

public class UiButton extends UiEntity {
	public int h;
	public int w;
	public float scale = 1f;
	public boolean sparks;
	public float r = 0.7f;
	public float g = 0.7f;
	public float b = 0.7f;
	public float rr = 0.7f;
	public float rg = 0.7f;
	public float rb = 0.7f;
	protected boolean hover;
	protected String label;
	protected float mx = 1f;
	protected float ar = 1f;
	protected float ag = 1f;
	protected float ab = 1f;
	protected boolean playSfx = true;
	private boolean disableClick;
	private float t;
	private Tween.Task last;
	private float scaleMod = 1f;

	public UiButton(String label, int x, int y) {
		this.setLabel(label);

		this.y = y;
		this.x = x;
	}

	public void setLabel(String label) {
		if (label == null) {
			return;
		}

		String old = label;

		if (Locale.has(label)) {
			label = Locale.get(label);
		}

		this.label = label;
		this.modLabel(old);

		Graphics.layout.setText(Graphics.medium, this.label);

		this.w = (int) Graphics.layout.width;
		this.h = (int) Graphics.layout.height;
	}

	protected void modLabel(String old) {

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

	public UiButton setPlaySfx(boolean playSfx) {
		this.playSfx = playSfx;
		return this;
	}

	@Override
	public void update(float dt) {
		if (Input.instance.wasPressed("ui_accept") && !disableClick) {
			if ((this.hover && this.isSelected && Input.instance.wasPressed("ui_accept")) || (checkHover() && Input.instance.wasPressed("ui_accept"))) {
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

		boolean h = this.hover;
		this.hover = isSelected || checkHover();

		if (h && !this.hover) {
			if (this.last != null) {
				Tween.remove(this.last);
			}

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

			Tween.to(new Tween.Task(1, 0.1f) {
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

			onUnhover();
			this.r = 0.7f;
			this.g = 0.7f;
			this.b = 0.7f;

		} else if ((!h && this.hover)) {
			if (this.last != null) {
				Tween.remove(this.last);
			}

			this.r = 1;
			this.g = 1;
			this.b = 1;

			if (!isSelected) {
				this.area.select(this);
			}

			Audio.playSfx("menu/moving");
			onHover();

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

		this.t += dt;
		float s = dt * 4f;

		this.rr += (this.r - this.rr) * s;
		this.rg += (this.g - this.rg) * s;
		this.rb += (this.b - this.rb) * s;

		super.update(dt);
	}

	@Override
	public void render() {
		Graphics.batch.setColor(this.r * this.ar, this.g * this.ag, this.b * this.ab, 1);

		Graphics.batch.end();
		Graphics.shadows.end();
		Graphics.text.begin();
		Graphics.batch.begin();

		Graphics.batch.setProjectionMatrix(Camera.nil.combined);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));
		Graphics.medium.draw(Graphics.batch, this.label, 2, 16);

		Graphics.batch.end();
		Graphics.text.end();
		Graphics.shadows.begin();
		Graphics.batch.begin();
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);

		Texture texture = Graphics.text.getColorBufferTexture();
		texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

		Graphics.batch.draw(texture, this.x - this.w / 2 + 2, this.y - this.h / 2, this.w / 2 + 4, this.h / 2 + 8,
			this.w, this.h + 16, this.scale, this.scale, (float) (Math.cos(this.y / 12 + Dungeon.time * 6) * (this.mx / this.w * 20)),
			0, 0, this.w + 4, this.h + 16, false, true);

		Graphics.batch.setColor(1, 1, 1, 1);
	}

	protected boolean checkHover() {
		return CollisionHelper.check((int) (Input.instance.uiMouse.x + InGameState.settingsX), (int) Input.instance.uiMouse.y,
			(int) (this.x - this.w / 2),
			(int) (this.y - this.h / 2 + 3),
			(int) (this.w), this.h);
	}

	public void onClick() {
		if (this.playSfx) {
			Audio.playSfx("menu/select");
		}
	}

	protected void onUnhover() {

	}

	protected void onHover() {

	}
}