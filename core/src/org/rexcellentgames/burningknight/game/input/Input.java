package org.rexcellentgames.burningknight.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.ui.UiKey;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Input implements InputProcessor, ControllerListener {
	public static InputMultiplexer multiplexer = new InputMultiplexer();
	public static Input instance;

	public Point uiMouse = new Point();
	public Point worldMouse = new Point();
	public boolean blocked = false;
	public Controller activeController;
	public Vector2 mouse = new Vector2(Display.GAME_WIDTH / 2, Display.GAME_HEIGHT / 2);
	public Vector2 target = new Vector2(Display.GAME_WIDTH / 2, Display.GAME_HEIGHT / 2);

	private boolean controllerChanged;

	public static UiKey listener;

	static {
		Gdx.input.setInputProcessor(multiplexer);
	}

	private GamepadMap map = new GamepadMap(GamepadMap.Type.Xbox);

	@Override
	public void connected(Controller controller) {
		String name = controller.getName().toLowerCase();

		if ((!name.contains("gamepad") && !name.contains("joy") && !name.contains("stick") && !name.contains("controller")) || name.contains("wacom")) {
			Log.info("Controller " + controller.getName() + " was ignored");
			return;
		}

		Log.info("Controller " + controller.getName() + " connected!");
		controllerChanged = true;

		if (activeController == null) {
			activeController = controller;
			onControllerChange();
		}
	}

	public void onControllerChange() {
		if (activeController == null) {
			return;
		}

		if (activeController.getName().toLowerCase().contains("xbox")) {
			map = new GamepadMap(GamepadMap.Type.Xbox);
		} else if (activeController.getName().equalsIgnoreCase("wireless controller")) {
		  map = new GamepadMap(GamepadMap.Type.PlayStation4);
    } else {
			map = new GamepadMap(GamepadMap.Type.Unknown);
		}
	}

	@Override
	public void disconnected(Controller controller) {
		Log.info("Controller " + controller.getName() + " disconnected!");
		controllerChanged = true;

		if (activeController == controller) {
			activeController = null;
		}
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		if (activeController == null || controller != activeController) {
			return false;
		}

		if (listener != null) {
			listener.set("Controller" + buttonCode);
			return false;
		}

		this.keys.put("Controller" + buttonCode, State.DOWN);
		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		if (activeController == null || controller != activeController) {
			return false;
		}

		this.keys.put("Controller" + buttonCode, State.UP);
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		if (activeController == null || controller != activeController) {
			return false;
		}

		int second = axisCode - 1;

		if (axisCode % 2 == 0) {
			second = axisCode + 1;
		}

		float dx = controller.getAxis(second);
		float d = (float) Math.sqrt(dx * dx + value * value);

		if (d >= 0.3f) {
			axes[second] = dx;
			axes[axisCode] = value;
		} else {
			axes[second] = 0f;
			axes[axisCode] = 0f;
		}

		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povCode, PovDirection value) {
		if (activeController == null || controller != activeController) {
			return false;
		}

		this.keys.put("ControllerDPadRight", value == PovDirection.east || value == PovDirection.northEast || value == PovDirection.southEast ? State.DOWN : State.UP);
		this.keys.put("ControllerDPadLeft", value == PovDirection.west || value == PovDirection.northWest || value == PovDirection.southWest ? State.DOWN : State.UP);
		this.keys.put("ControllerDPadUp", value == PovDirection.north || value == PovDirection.northEast || value == PovDirection.northWest ? State.DOWN : State.UP);
		this.keys.put("ControllerDPadDown", value == PovDirection.south || value == PovDirection.southEast || value == PovDirection.southWest ? State.DOWN : State.UP);

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

	private HashMap<String, State> keys = new HashMap<>();
	private HashMap<String, ArrayList<String>> bindings = new HashMap<>();
	private float axes[] = new float[8];
	private int amount;

	public HashMap<String, State> getKeys() {
		return this.keys;
	}

	public Input() {
		instance = this;
		multiplexer.addProcessor(this);
		this.keys.put("MouseWheel", State.RELEASED);

		JsonReader reader = new JsonReader();

		FileHandle handle = Gdx.files.external(SaveManager.SAVE_DIR + "keys.json");

		if (!handle.exists()) {
			resetBindings();
			return;
		}

		if (activeController == null && Controllers.getControllers().size > 0) {
			this.connected(Controllers.getControllers().get(0));
		}

    JsonValue root = reader.parse(handle);

    for (JsonValue value : root) {
			for (String name : value.asStringArray()) {
				bind(value.name, name);
			}
		}
	}

	public float getAxis(String name) {
		if (activeController == null) {
			return 0;
		}

		ArrayList<String> keys = this.bindings.get(name);

		if (keys == null || keys.size() == 0) {
			return 0;
		}

		String na = keys.get(0);
		int n = map.getId(na);

		if (axes.length <= n || n < 0) {
			return 0;
		}

		if (Math.abs(axes[n]) < 0.01f) {
		  return 0;
    }
		
		return axes[n];
	}
	
	public void addBinding(String name, String key) {
    this.bindings.get(name).add(key);
  }
  
  public void removeBinding(String name, String key) {
    this.bindings.get(name).remove(key);
  }
	
	public void rebind(String name, String oldKey, String newKey) {
    this.bindings.get(name).remove(oldKey);
    this.bindings.get(name).add(0, newKey);
	}

	public String getBinding(String id) {
		List<String> list = this.bindings.get(id);

		if (list != null) {
			return list.get(0);
		}

		return null;
	}

	public void resetBindings() {
		Log.info("Resetting bindings");

	  this.bindings.clear();
	  
    JsonReader reader = new JsonReader();
    JsonValue root = reader.parse(Gdx.files.internal("keys.json"));

    for (JsonValue value : root) {
      for (String name : value.asStringArray()) {
        bind(value.name, name);
      }
    }
    
    saveBindings();
  }
	
	public void saveBindings() {
		Log.info("Saving key bindings");

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    File file = Gdx.files.external(SaveManager.SAVE_DIR + "keys.json").file();
    
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException ignore) {
      }
    }
    
    try {
      PrintWriter writer = new PrintWriter(file);
      writer.write(gson.toJson(this.bindings));
      writer.close();
    } catch(FileNotFoundException ignored) {
    }
  }
	
	public void updateMousePosition() {
		Vector3 m = Camera.ui.unproject(new Vector3(mouse.x, mouse.y, 0),
			Camera.viewport.getScreenX(), Camera.viewport.getScreenY(),
			Camera.viewport.getScreenWidth(), Camera.viewport.getScreenHeight());

		this.uiMouse.x = m.x;
		this.uiMouse.y = m.y;

		m = Camera.game.unproject(new Vector3(mouse.x, mouse.y, 0),
			Camera.viewport.getScreenX(), Camera.viewport.getScreenY(),
			Camera.viewport.getScreenWidth(), Camera.viewport.getScreenHeight());

		this.worldMouse.x = m.x;
		this.worldMouse.y = m.y;

		this.mouse.x += (this.target.x - this.mouse.x) / 2f;
		this.mouse.y += (this.target.y - this.mouse.y) / 2f;
	}

	public void bind(String name, String ... keys) {
		for (String key : keys) {
			this.keys.put(key, State.RELEASED);

			ArrayList<String> array;

			if (this.bindings.containsKey(name)) {
				array = this.bindings.get(name);
			} else {
				array = new ArrayList<>();
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

	public void putState(String id, State state) {
		for (String name : this.bindings.get(id)) {
			this.keys.put(name, state);
		}
	}

	public boolean isDown(String key) {
		if (blocked) {
			return false;
		}

		if (!key.equals("pause") && Dungeon.game.getState().isPaused()) {
			return false;
		}

		if (!this.bindings.containsKey(key)) {
			return false;
		}

		for (String id : this.bindings.get(key)) {
			State state = this.keys.get(toButtonWithId(id));

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

		if (!this.bindings.containsKey(key)) {
			return false;
		}

		for (String id : this.bindings.get(key)) {
		  if (this.keys.get(toButtonWithId(id)) == State.DOWN) {
				return true;
			}
		}

		return false;
	}

	public boolean wasReleased(String key) {
		if (blocked) {
			return false;
		}

		if (!this.bindings.containsKey(key)) {
			return false;
		}

		for (String id : this.bindings.get(key)) {
		  if (this.keys.get(toButtonWithId(id)) == State.UP) {
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

		if (listener != null) {
			listener.set(id);
			return false;
		}

		this.keys.put(id, State.DOWN);

		// Log.info(class);

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		String id = com.badlogic.gdx.Input.Keys.toString(keycode);
		this.keys.put(id, State.UP);

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
		target.x = screenX;
		target.y = screenY;

		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		target.x = screenX;
		target.y = screenY;

		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		this.keys.put("MouseWheel", State.DOWN);
		this.amount = amount;

		return false;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	private String toButtonWithId(String id) {
    if (id.toLowerCase().startsWith("controller") && !id.toLowerCase().contains("dpad")) {
      return "Controller" + map.getId(id);
    }
    
    return id;
  }
}