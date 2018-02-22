package org.rexellentgames.dungeon.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

import java.util.HashMap;
import java.util.Map;

public class Input implements InputProcessor {
	public static InputMultiplexer multiplexer = new InputMultiplexer();
	public static Input instance;

	static {
		Gdx.input.setInputProcessor(multiplexer);
	}

	public enum State {
		UP, DOWN, HELD, RELEASED
	}

	private HashMap<String, State> keys = new HashMap<String, State>();
	private int amount;

	public Input() {
		instance = this;
		multiplexer.addProcessor(this);

		this.keys.put("MouseWheel", State.RELEASED);
	}

	public void bind(String key) {
		this.keys.put(key, State.RELEASED);
	}

	public void update() {
		for (Map.Entry<String, State> pair : this.keys.entrySet()) {
			State state = pair.getValue();

			if (state == State.UP) {
				pair.setValue(State.RELEASED);
			} else if (state == State.DOWN) {
				pair.setValue(pair.getKey().equals("MouseWheel") ? State.RELEASED : State.HELD);
			}
		}
	}

	public boolean isDown(String key) {
		State state = this.keys.get(key);
		return state == State.DOWN || state == State.HELD;
	}

	public boolean wasPressed(String key) {
		return this.keys.get(key) == State.DOWN;
	}

	public boolean wasReleased(String key) {
		return this.keys.get(key) == State.UP;
	}

	public int getAmount() {
		return this.amount;
	}

	@Override
	public boolean keyDown(int keycode) {
		this.keys.put(com.badlogic.gdx.Input.Keys.toString(keycode), State.DOWN);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		this.keys.put(com.badlogic.gdx.Input.Keys.toString(keycode), State.UP);
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		this.keys.put("Mouse" + button, State.DOWN);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		this.keys.put("Mouse" + button, State.UP);
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
		this.keys.put("MouseWheel", State.DOWN);
		this.amount = amount;

		return false;
	}
}