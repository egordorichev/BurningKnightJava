package org.rexellentgames.dungeon.ui;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.CollisionHelper;
import org.rexellentgames.dungeon.util.Tween;

public class UiButton extends UiEntity implements InputProcessor {
	private String label;

	public int h = 12;
	public int w;

	private boolean hover;
	private float scale = 0.8f;
	private Tween.Task last;

	public UiButton(String label, int x, int y) {
		this.label = label;

		Graphics.layout.setText(Graphics.medium, this.label);
		this.w = (int) Graphics.layout.width;

		if (x == -1) {
			this.x = (Display.GAME_WIDTH - this.w) / 2;
		}

		this.y = y;

		Input.multiplexer.addProcessor(this);
	}

	@Override
	public void render() {
		Matrix4 m = new Matrix4();
		m.setToRotation(0, 0, 1, 10);
		m.translate(-1, -40, 0);
		Matrix4 old = Graphics.batch.getTransformMatrix();
		Graphics.batch.setTransformMatrix(m);
		Graphics.medium.draw(Graphics.batch, this.label, this.x, this.y);
		Graphics.batch.setTransformMatrix(old);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		boolean h = this.hover;
		this.hover = CollisionHelper.check((int) Input.instance.uiMouse.x, (int) Input.instance.uiMouse.y, (int) this.x,
			(int) this.y, this.w, this.h);

		if (h && !this.hover) {
			if (this.last != null) {
				Tween.remove(this.last);
			}

			this.last = Tween.to(new Tween.Task(0.8f, 0.3f) {
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
		} else if (!h && this.hover) {
			if (this.last != null) {
				Tween.remove(this.last);
			}

			this.last = Tween.to(new Tween.Task(1f, 0.3f) {
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
			this.onClick();
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