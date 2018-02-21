package org.rexellentgames.dungeon.game.input;

import com.badlogic.gdx.InputProcessor;

import java.util.HashMap;

public class Input implements InputProcessor {
	public enum State {
		UP, DOWN, HELD
	};

	private HashMap<String, State> keys;

	public static boolean isDown(String key) {
		return false;//keys.get(key) == State.DOWN;
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