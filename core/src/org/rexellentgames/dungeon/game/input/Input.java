package org.rexellentgames.dungeon.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.Creature;
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

	public static void set(int id) {
		instance = inputs.get(id);
	}

	static {
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void connected(Controller controller) {

	}

	@Override
	public void disconnected(Controller controller) {

	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		this.keys.put("Controller" + buttonCode, State.DOWN);
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		this.keys.put("Controller" + buttonCode, State.UP);
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		if (value < 0) {
			this.keys.put("ControllerAxisMinus" + axisCode, State.DOWN);
		} else {
			this.keys.put("ControllerAxisPlus" + axisCode, State.DOWN);
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
	private int amount;

	public HashMap<String, State> getKeys() {
		return this.keys;
	}

	public Input(int id) {
		instance = this;
		multiplexer.addProcessor(this);
		inputs.put(id, this);
		this.keys.put("MouseWheel", State.RELEASED);

		bind("left", "Left", "A");
		bind("right", "Right", "D");
		bind("up", "Up", "W");
		bind("down", "Down", "S");

		bind("pickup", "Q");
		bind("drop_item", "E");

		bind("mouse0", "Mouse0");
		bind("mouse1", "Mouse1");
		bind("scroll", "MouseWheel");

		bind("action", "X");

		bind("1", "1");
		bind("2", "2");
		bind("3", "3");
		bind("4", "4");
		bind("5", "5");
		bind("6", "6");

		bind("c", "C");
		bind("z", "Z");
	}

	public void updateMousePosition() {
		Vector3 mouse = Camera.ui.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

		this.uiMouse.x = mouse.x;
		this.uiMouse.y = mouse.y;

		mouse = Camera.instance.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

		this.worldMouse.x = mouse.x;
		this.worldMouse.y = mouse.y;
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