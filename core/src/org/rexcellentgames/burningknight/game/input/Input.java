package org.rexcellentgames.burningknight.game.input;

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
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Input implements InputProcessor, ControllerListener {
  public static InputMultiplexer multiplexer = new InputMultiplexer();
  public static Input instance;

  static {
    Gdx.input.setInputProcessor(multiplexer);
  }

  public Point uiMouse = new Point();
  public Point worldMouse = new Point();
  public boolean blocked = false;
  public Controller active;
  public Vector2 mouse = new Vector2(Display.GAME_WIDTH / 2, Display.GAME_HEIGHT / 2);
  public Vector2 target = new Vector2(Display.GAME_WIDTH / 2, Display.GAME_HEIGHT / 2);
  public boolean circle = true;
  private boolean controllerChanged;
  private HashMap<String, State> keys = new HashMap<>();
  private HashMap<String, ArrayList<String>> bindings = new HashMap<>();
  private HashMap<String, Float> axes = new HashMap<>();
  private int amount;

  public Input() {
    instance = this;
    multiplexer.addProcessor(this);
    this.keys.put("MouseWheel", State.RELEASED);

    JsonReader reader = new JsonReader();
    JsonValue root = reader.parse(Gdx.files.internal("keys.json"));

    for (JsonValue value: root) {
      for (String name: value.asStringArray()) {
        bind(value.name, name);
      }
    }

    if (active == null && Controllers.getControllers().size > 0) {
      this.connected(Controllers.getControllers().get(0));
    }
  }

  @Override
  public void connected(Controller controller) {
    String name = controller.getName().toLowerCase();

    if (!name.contains("gamepad") && !name.contains("joy") && !name.contains("stick") && !name.contains("controller")) {
      Log.info("Controller " + controller.getName() + " was ignored");
      return;
    }

    Log.info("Controller " + controller.getName() + " connected!");
    controllerChanged = true;

    if (active == null) {
      active = controller;
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

  public HashMap<String, State> getKeys() {
    return this.keys;
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

  public void bind(String name, String... keys) {
    for (String key: keys) {
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
    for (Map.Entry<String, State> pair: this.keys.entrySet()) {
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
    }    if (!key.equals("pause") && Dungeon.game.getState().isPaused()) {
      return false;
    }

    for (String id: this.bindings.get(key)) {
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

    for (String id: this.bindings.get(key)) {
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

    for (String id: this.bindings.get(key)) {
      if (this.keys.get(id) == State.UP) {
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
    this.keys.put(id, State.DOWN);

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