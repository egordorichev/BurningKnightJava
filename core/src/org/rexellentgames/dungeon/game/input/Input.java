package org.rexellentgames.dungeon.game.input;

import com.badlogic.gdx.InputProcessor;

import java.util.HashMap;
import java.util.Map;

public class Input implements InputProcessor {
	public enum State {
		UP, DOWN, HELD, RELEASED
	};

	private static HashMap<String, State> keys = new HashMap<String, State>();
	private static int amount;

	public static void bind(String key) {
		keys.put(key, State.RELEASED);
	}

	public static void update() {
		for (Map.Entry<String, State> pair : keys.entrySet()) {
			State state = pair.getValue();

			if (state == State.UP) {
				pair.setValue(State.RELEASED);
			} else if (state == State.DOWN) {
				pair.setValue(State.HELD);
			}
		}
	}

	public static boolean isDown(String key) {
		return keys.get(key) == State.DOWN;
	}

	public static boolean wasPressed(String key) {
		return keys.get(key) == State.DOWN;
	}

	public static boolean wasReleased(String key) {
		return keys.get(key) == State.UP;
	}

	public static int getAmount() {
		return amount;
	}

	@Override
	public boolean keyDown(int keycode) {
		keys.put(com.badlogic.gdx.Input.Keys.toString(keycode), State.DOWN);

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		keys.put(com.badlogic.gdx.Input.Keys.toString(keycode), State.UP);

		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		keys.put("Mouse" + button, State.DOWN);

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		keys.put("Mouse" + button, State.UP);

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
		keys.put("MouseWheel", State.DOWN);
		this.amount = amount;

		return false;
	}
}