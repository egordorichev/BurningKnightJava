package org.rexellentgames.dungeon.ui;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.item.Spark;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.CollisionHelper;
import org.rexellentgames.dungeon.util.Tween;

public class UiButton extends UiEntity implements InputProcessor {
	public int h;
	public int w;

	protected boolean hover;
	protected float scale = 0.8f;
	private Tween.Task last;
	protected TextureRegion region;
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

	public UiButton setSparks(boolean sparks) {
		this.sparks = sparks;
		return this;
	}

	public UiButton(String label, int x, int y) {
		this.setLabel(label);

		Input.multiplexer.addProcessor(this);

		this.y = y;
		this.x = x;
	}

	public void setLabel(String label) {
		this.region = Graphics.getTexture(label);

		this.w = this.region.getRegionWidth();
		this.h = this.region.getRegionHeight();
	}

	@Override
	public void destroy() {
		super.destroy();
		Input.multiplexer.removeProcessor(this);
	}

	@Override
	public void render() {
		if (this.sparks) {
			Spark.random(this.x - this.w / 2, this.y - this.h / 2, this.w, this.h);
		}

		Graphics.batch.setColor(this.rr * this.ar, this.rg * this.ag, this.rb * this.ab, 1);

		Graphics.render(this.region, this.x, this.y, (float) (Math.cos(this.y / 12 + Dungeon.time * 6) * (this.mx / this.w * 20)), this.region.getRegionWidth() / 2, this.region.getRegionHeight() / 2, false, false, this.scale, this.scale);
		Graphics.batch.setColor(1, 1, 1, 1);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.rr += (this.r - this.rr) * dt * 10;
		this.rg += (this.g - this.rg) * dt * 10;
		this.rb += (this.b - this.rb) * dt * 10;

		boolean h = this.hover;
		this.hover = CollisionHelper.check((int) Input.instance.worldMouse.x, (int) Input.instance.worldMouse.y, (int) this.x - this.w / 2, (int) this.y - this.h / 2, this.w + 8, this.h);

		if (h && !this.hover) {
			if (this.last != null) {
				Tween.remove(this.last);
			}

			this.r = 1f;
			this.g = 1f;
			this.b = 1f;

			this.last = Tween.to(new Tween.Task(0.8f, 0.1f) {
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
			});

		} else if (!h && this.hover) {
			if (this.last != null) {
				Tween.remove(this.last);
			}

			this.r = 0.7f;
			this.g = 0.7f;
			this.b = 0.7f;

			Graphics.playSfx("menu/moving");

			Tween.to(new Tween.Task(20, 0.1f) {
				@Override
				public float getValue() {
					return mx;
				}

				@Override
				public void setValue(float value) {
					mx = value;
				}
			});

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
			});
		}
	}

	public void onClick() {
		Graphics.playSfx("menu/select");
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (this.hover) {
			if (this.last != null) {
				Tween.remove(this.last);
			}

			this.rr = 1f;
			this.rg = 1f;
			this.rb = 1f;

			this.last = Tween.to(new Tween.Task(1.2f, 0.05f) {
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

					last = Tween.to(new Tween.Task(1f, 0.05f) {
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
					});
				}
			});

			return true;
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}