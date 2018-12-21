package org.rexcellentgames.burningknight.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
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

public class Input implements InputProcessor {
	public static InputMultiplexer multiplexer = new InputMultiplexer();
	public static Input instance;

	public Point uiMouse = new Point();
	public Point worldMouse = new Point();
	public boolean blocked = false;
	public Vector2 mouse = new Vector2(Display.GAME_WIDTH / 2, Display.GAME_HEIGHT / 2);
	public Vector2 target = new Vector2(Display.GAME_WIDTH / 2, Display.GAME_HEIGHT / 2);

	private boolean controllerChanged;

	public static boolean anyPressed;

	public static UiKey listener;

	static {
		Gdx.input.setInputProcessor(multiplexer);
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

	public static final String KEYS = "key_bind.json";

	public Input() {
		instance = this;
		multiplexer.addProcessor(this);
		this.keys.put("MouseWheel", State.RELEASED);

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

	public float getAxis(String name) {
		return 0;
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

	public boolean hasControllerChanged() {
		return controllerChanged;
	}

	public void update() {
		anyPressed = false;

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

	public void setAmount(int amount) {
		this.amount = amount;
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
}