package org.rexellentgames.dungeon.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.net.Packets;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Input implements InputProcessor, ControllerListener {
	public static InputMultiplexer multiplexer = new InputMultiplexer();
	public static Input instance;
	public static HashMap<Integer, Input> inputs = new HashMap<Integer, Input>();

	public Point uiMouse = new Point();
	public Point worldMouse = new Point();
	public boolean blocked = false;
	public Controller active;
	public Vector2 mouse = new Vector2();
	public boolean circle = true;

	private boolean controllerChanged;

	public static void set(int id) {
		instance = inputs.get(id);
	}

	static {
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void connected(Controller controller) {
		Log.info("Controller " + controller.getName() + " connected!");
		Log.error("Ignored");
		controllerChanged = true;

		if (active == null) {
		//	active = controller;
		}
	}

	@Override
	public void disconnected(Controller controller) {
		Log.info("Controller " + controller.getName() + " diconnected!");
		controllerChanged = true;

		if (active == controller) {
			active = null;
		}
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		if (controller != active) {
			return false;
		}

		this.keys.put("Controller" + buttonCode, State.DOWN);
		Log.info(buttonCode + "");
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		if (controller != active) {
			return false;
		}

		this.keys.put("Controller" + buttonCode, State.UP);
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		if (controller != active) {
			return false;
		}

		int second = axisCode - 1;

		if (axisCode % 2 == 0) {
			second = axisCode + 1;
		}

		float dx = controller.getAxis(second);
		float d = (float) Math.sqrt(dx * dx + value * value);

		if (d >= 0.3f) {
			axes.put("Axis" + second, dx);
			axes.put("Axis" + axisCode, value);
		} else {
			axes.put("Axis" + second, 0f);
			axes.put("Axis" + axisCode, 0f);
		}

		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		return false;
	}

	@Override
	public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
		return false;
	}

	@Override
	public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
		return false;
	}

	@Override
	public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
		return false;
	}

	public enum State {
		UP(0), DOWN(1), HELD(2), RELEASED(3);
		private int value;

		public int getValue() {
			return this.value;
		}

		State(int value) {
			this.value = value;
		}
	}

	private HashMap<String, State> keys = new HashMap<String, State>();
	private HashMap<String, ArrayList<String>> bindings = new HashMap<String, ArrayList<String>>();
	private HashMap<String, Float> axes = new HashMap<>();
	private int amount;

	public HashMap<String, State> getKeys() {
		return this.keys;
	}

	public Input(int id) {
		instance = this;
		multiplexer.addProcessor(this);
		inputs.put(id, this);
		this.keys.put("MouseWheel", State.RELEASED);

		JsonReader reader = new JsonReader();
		JsonValue root = reader.parse(Gdx.files.internal("keys.json"));

		for (JsonValue value : root) {
			for (String name : value.asStringArray()) {
				bind(value.name, name);
			}
		}

		if (active == null && Controllers.getControllers().size > 0) {
			this.connected(Controllers.getControllers().get(0));
		}
	}

	public float getAxis(String name) {
		ArrayList<String> keys = this.bindings.get(name);

		if (keys == null || keys.size() == 0) {
			return 0;
		}

		String n = keys.get(0);

		if (!axes.containsKey(n)) {
			return 0;
		}

		return axes.get(n);
	}

	public void updateMousePosition() {
		Vector3 m = Camera.ui.unproject(new Vector3(mouse.x, mouse.y, 0),
			Camera.instance.viewport.getScreenX(), Camera.instance.viewport.getScreenY(),
			Camera.instance.viewport.getScreenWidth(), Camera.instance.viewport.getScreenHeight());

		this.uiMouse.x = m.x;
		this.uiMouse.y = m.y;

		m = Camera.instance.getCamera().unproject(new Vector3(mouse.x, mouse.y, 0),
			Camera.instance.viewport.getScreenX(), Camera.instance.viewport.getScreenY(),
			Camera.instance.viewport.getScreenWidth(), Camera.instance.viewport.getScreenHeight());

		this.worldMouse.x = m.x;
		this.worldMouse.y = m.y;
	}

	public void bind(String name, String ... keys) {
		for (String key : keys) {
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
	}

	public boolean hasControllerChanged() {
		return controllerChanged;
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

		controllerChanged = false;
	}

	public boolean isDown(String key) {
		if (blocked) {
			return false;
		}


		if (!key.equals("pause") && Dungeon.game.getState().isPaused()) {
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
		String id = com.badlogic.gdx.Input.Keys.toString(keycode);
		this.keys.put(id, State.DOWN);

		if (Network.client != null && Player.instance != null) {
			Network.client.getClientHandler().send(Packets.makeInputChanged(State.DOWN, id, 0));
			Player.instance.registerState();
		}

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		String id = com.badlogic.gdx.Input.Keys.toString(keycode);
		this.keys.put(id, State.UP);

		if (Network.client != null && Player.instance != null) {
			Network.client.getClientHandler().send(Packets.makeInputChanged(State.UP, id, 0));
			Player.instance.registerState();
		}

		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		this.keys.put("Mouse" + button, State.DOWN);

		if (Network.client != null && Player.instance != null) {
			Network.client.getClientHandler().send(Packets.makeInputChanged(State.DOWN, "Mouse" + button, 0));
			Player.instance.registerState();
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		this.keys.put("Mouse" + button, State.UP);

		if (Network.client != null && Player.instance != null) {
			Network.client.getClientHandler().send(Packets.makeInputChanged(State.UP, "Mouse" + button, 0));
			Player.instance.registerState();
		}

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		mouse.x = screenX;
		mouse.y = screenY;

		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		this.keys.put("MouseWheel", State.DOWN);
		this.amount = amount;

		if (Network.client != null && Player.instance != null) {
			Network.client.getClientHandler().send(Packets.makeInputChanged(State.UP, "MouseWheel", amount));
			Player.instance.registerState();
		}

		return false;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}