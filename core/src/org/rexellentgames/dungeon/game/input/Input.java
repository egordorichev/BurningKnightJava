package org.rexellentgames.dungeon.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Input implements InputProcessor {
	public static InputMultiplexer multiplexer = new InputMultiplexer();
	public static Input instance;

	public Point uiMouse = new Point();
	public Point worldMouse = new Point();
	public boolean blocked = false;

	static {
		Gdx.input.setInputProcessor(multiplexer);
	}

	public enum State {
		UP, DOWN, HELD, RELEASED
	}

	private HashMap<String, State> keys = new HashMap<String, State>();
	private HashMap<String, ArrayList<String>> bindings = new HashMap<String, ArrayList<String>>();
	private int amount;

	public Input() {
		instance = this;
		multiplexer.addProcessor(this);

		this.keys.put("MouseWheel", State.RELEASED);
	}

	public void updateMousePosition() {
		Vector3 mouse = Camera.ui.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

		this.uiMouse.x = mouse.x;
		this.uiMouse.y = mouse.y;

		mouse = Camera.instance.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

		this.worldMouse.x = mouse.x;
		this.worldMouse.y = mouse.y;
	}

	public void bind(String name, String key) {
		this.keys.put(key, State.RELEASED);

		ArrayList<String> array;

		if (this.bindings.containsKey(name)) {
			array = this.bindings.get(name);
		} else {
			array = new ArrayList<String>();
			this.bindings.put(name, array);
		}

		array.add(key);
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
		if (blocked) {
			return false;
		}

		for (String id : this.bindings.get(key)) {
			State state = this.keys.get(id);

			if (state == State.DOWN || state == State.HELD) {
				return true;
			}
		}

		return false;
	}

	public boolean wasPressed(String key) {
		if (blocked) {
			return false;
		}

		for (String id : this.bindings.get(key)) {
			if (this.keys.get(id) == State.DOWN) {
				return true;
			}
		}

		return false;
	}

	public boolean wasReleased(String key) {
		if (blocked) {
			return false;
		}

		for (String id : this.bindings.get(key)) {
			if (this.keys.get(id) == State.UP) {
				return true;
			}
		}

		return false;
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