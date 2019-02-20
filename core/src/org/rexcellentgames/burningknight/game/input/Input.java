package org.rexcellentgames.burningknight.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.libsdl.SDL;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.state.InGameState;
import org.rexcellentgames.burningknight.ui.UiKey;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.geometry.Point;
import uk.co.electronstudio.sdl2gdx.SDL2Controller;
import uk.co.electronstudio.sdl2gdx.SDL2ControllerManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Input implements InputProcessor, ControllerListener {
	public static final String KEYS = "key_binds.json";
	public static InputMultiplexer multiplexer = new InputMultiplexer();
	public static Input instance;
	public static boolean anyPressed;
	public static UiKey listener;

	static {
		Gdx.input.setInputProcessor(multiplexer);
	}

	public Point uiMouse = new Point();
	public Point worldMouse = new Point();
	public boolean blocked = false;
	public Vector2 mouse = new Vector2(Display.GAME_WIDTH / 2, Display.GAME_HEIGHT / 2);
	public Vector2 target = new Vector2(Display.GAME_WIDTH / 2, Display.GAME_HEIGHT / 2);
	private HashMap<String, State> keys = new HashMap<>();
	private HashMap<String, ArrayList<String>> bindings = new HashMap<>();
	private float axes[] = new float[8];
	private int amount;
	private SDL2ControllerManager sdlManager;

	public Input() {
		instance = this;
		multiplexer.addProcessor(this);
		this.keys.put("MouseWheel", State.RELEASED);

		sdlManager = new SDL2ControllerManager();
		sdlManager.addListener(this);

		JsonReader reader = new JsonReader();
		FileHandle handle = Gdx.files.external(SaveManager.SAVE_DIR + KEYS);

		if (!handle.exists()) {
			resetBindings();
			return;
		}

    JsonValue root = reader.parse(handle);

    for (JsonValue value : root) {
			for (String name : value.asStringArray()) {
				bind(value.name, name);
			}
		}
	}

	public HashMap<String, State> getKeys() {
		return this.keys;
	}

	public Vector2 getAxis(String name) {
		if (activeController != null && this.bindings.containsKey(name)) {
			for (String id : this.bindings.get(name)) {
				switch (id) {
					case "left_stick": return new Vector2(axes[SDL.SDL_CONTROLLER_AXIS_LEFTX], axes[SDL.SDL_CONTROLLER_AXIS_LEFTY]);
					case "right_stick": return new Vector2(axes[SDL.SDL_CONTROLLER_AXIS_RIGHTX], axes[SDL.SDL_CONTROLLER_AXIS_RIGHTY]);
					case "left_trigger": return new Vector2(axes[SDL.SDL_CONTROLLER_AXIS_TRIGGERLEFT], 0);
					case "right_trigger": return new Vector2(axes[SDL.SDL_CONTROLLER_AXIS_TRIGGERRIGHT], 0);
				}
			}
		}

		return new Vector2(0, 0);
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

    File file = Gdx.files.external(SaveManager.SAVE_DIR + KEYS).file();

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

	private boolean tried;

	public void update() {
		if (activeController == null && sdlManager.getControllers().size > 0) {
			for (Controller controller : sdlManager.getControllers()) {
				connected(controller);
			}
		}

		if (activeController != null && (!activeController.isConnected() || !activeController.joystick.getAttached())) {
			disconnected(activeController);
		}

		anyPressed = false;

		for (Map.Entry<String, State> pair : this.keys.entrySet()) {
			State state = pair.getValue();

			if (state == State.UP) {
				pair.setValue(State.RELEASED);
			} else if (state == State.DOWN) {
				pair.setValue(pair.getKey().equals("MouseWheel") ? State.RELEASED : State.HELD);
			}
		}
	}

	public void putState(String id, State state) {
		for (String name : this.bindings.get(id)) {
			this.keys.put(toButtonWithId(name), state);
		}
	}

	public boolean isDown(String key) {
		if (blocked) {
			return false;
		}

		/*if (!key.equals("pause") && Dungeon.game.getState().isPaused()) {
			return false;
		}*/

		if (!this.bindings.containsKey(key)) {
			return Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.valueOf(key));
		}

		for (String id : this.bindings.get(key)) {
			String idd = toButtonWithId(id);

			if (idd == null) {
				continue;
			}

			State state = this.keys.get(idd);

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
			if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.valueOf(key))) {
				return true;
			}

			return false;
		}

		for (String id : this.bindings.get(key)) {
			String idd = toButtonWithId(id);

			if (idd == null) {
				continue;
			}

			State state = this.keys.get(toButtonWithId(idd));

		  if (state == State.DOWN) {
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
			String idd = toButtonWithId(id);

			if (idd == null) {
				continue;
			}

			State state = this.keys.get(toButtonWithId(idd));

			if (state == State.UP) {
				return true;
			}
		}

		return false;
	}

	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public boolean keyDown(int keycode) {
		String id = com.badlogic.gdx.Input.Keys.toString(keycode);

		anyPressed = true;
		if (listener != null) {
			if (keycode != com.badlogic.gdx.Input.Keys.ESCAPE) {
				listener.set(id);
			}

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
		if (listener != null) {
			listener.set("Mouse" + button);

			return false;
		}

		this.keys.put("Mouse" + button, State.DOWN);

		anyPressed = true;
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

	private String toButtonWithId(String id) {
    if (id.toLowerCase().startsWith("controller")) {
    	return null;
    }

    return id;
  }
	
	public String getMapping(String key) {
		ArrayList<String> keys = this.bindings.get(key);

		if (keys == null) {
			return "Null";
		}

		String k = keys.get(0);

		if (k.startsWith("Mouse")) {
			k = k.replace("Mouse", "");

			switch (k) {
				case "0": return Locale.get("left_mouse_button");
				case "1": return Locale.get("right_mouse_button");
			}

			return k;
		} else {
			return k;
		}
	}

	public SDL2Controller activeController;

	@Override
	public void connected(Controller controller) {
		String name = controller.getName();

		if (activeController == null || name.equals(GlobalSave.getString("controller", ""))) {
			activeController = (SDL2Controller) controller;
			GlobalSave.put("controller", name);
			Log.error("Controller " + name + " connected and selected");

			for (int i = 0; i < axes.length; i++) {
				axes[i] = 0;
			}
		} else {
			Log.error("Controller " + name + " connected but not active");
		}
	}

	@Override
	public void disconnected(Controller controller) {
		if (activeController == controller) {
			activeController = null;

			if (Dungeon.game.getState() instanceof InGameState && !Dungeon.game.getState().isPaused()) {
				Dungeon.game.getState().setPaused(true);
			}
		}

		Log.error("Controller " + controller.getName() + " disconnected");
	}

	private static String getButtonName(int code) {
		switch (code) {
			case SDL.SDL_CONTROLLER_BUTTON_A: return "button_a";
			case SDL.SDL_CONTROLLER_BUTTON_B: return "button_b";
			case SDL.SDL_CONTROLLER_BUTTON_X: return "button_x";
			case SDL.SDL_CONTROLLER_BUTTON_Y: return "button_y";
			case SDL.SDL_CONTROLLER_BUTTON_BACK: return "button_back";
			case SDL.SDL_CONTROLLER_BUTTON_GUIDE: return "button_guide";
			case SDL.SDL_CONTROLLER_BUTTON_START: return "button_start";
			case SDL.SDL_CONTROLLER_BUTTON_LEFTSTICK: return "button_leftstick";
			case SDL.SDL_CONTROLLER_BUTTON_RIGHTSTICK: return "button_rightstick";
			case SDL.SDL_CONTROLLER_BUTTON_LEFTSHOULDER: return "button_leftshoulder";
			case SDL.SDL_CONTROLLER_BUTTON_RIGHTSHOULDER: return "button_rightshoulder";
			case SDL.SDL_CONTROLLER_BUTTON_DPAD_UP: return "dpad_up";
			case SDL.SDL_CONTROLLER_BUTTON_DPAD_DOWN: return "dpad_down";
			case SDL.SDL_CONTROLLER_BUTTON_DPAD_LEFT: return "dpawn_left";
			case SDL.SDL_CONTROLLER_BUTTON_DPAD_RIGHT: return "dpad_right";
		}

		return "button_max";
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonCode) {
		if (controller == activeController) {
			this.keys.put(getButtonName(buttonCode), State.DOWN);
			return true;
		}

		return false;
	}

	@Override
	public boolean buttonUp(Controller controller, int buttonCode) {
		if (controller == activeController) {
			this.keys.put(getButtonName(buttonCode), State.UP);
			return true;
		}

		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisCode, float value) {
		if (controller == activeController) {
			axes[axisCode] = value;
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

		State(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}
	}
}