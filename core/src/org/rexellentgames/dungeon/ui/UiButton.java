package org.rexellentgames.dungeon.ui;

import com.badlogic.gdx.InputProcessor;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.CollisionHelper;

public class UiButton extends UiEntity implements InputProcessor {
	private String label;

	public int w = 32;
	public int h = 12;

	@Override
	public void init() {
		Graphics.layout.setText(Graphics.medium, this.label);
		this.w = (int) Graphics.layout.width;
		this.x = (Display.GAME_WIDTH - this.w) / 2;

		Input.multiplexer.addProcessor(this);
	}

	@Override
	public void render() {
		Graphics.medium.draw(Graphics.batch, this.label, this.x, this.y + 12);
	}

	public void setLabel(String label) {
		this.label = label;
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
		if (CollisionHelper.check((int) Input.instance.uiMouse.x, (int) Input.instance.uiMouse.y, (int) this.x,
			(int) this.y, this.w, this.h)) {

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