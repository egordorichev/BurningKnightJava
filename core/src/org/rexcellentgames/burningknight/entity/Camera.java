package org.rexcellentgames.burningknight.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Settings;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.creature.player.Spawn;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.util.MathUtils;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;

public class Camera extends Entity {
	public static boolean did = false;

	public static Camera instance;
	public static OrthographicCamera ui;
	public static OrthographicCamera nil;
	public static OrthographicCamera game;
	public static OrthographicCamera viewportCamera;
	public static Viewport viewport;
	public static Entity target;
	private static float shake;
	private static float pushA;
	private static float pushAm;

	public static void shake(float amount) {
		shake = Math.max(amount * Settings.screenshake, shake);
	}

	private static Tween.Task last;

	public static void push(float a, float am) {
		pushA = a;
		pushAm = 0;

		if (last != null) {
			Tween.remove(last);
		}

		last = Tween.to(new Tween.Task(am * Settings.screenshake, 0.05f) {
			@Override
			public float getValue() {
				return pushAm;
			}

			@Override
			public void setValue(float value) {
				pushAm = value;
			}
		});
	}

	public Camera() {
		instance = this;

		// https://code-disaster.com/2016/02/subpixel-perfect-smooth-scrolling.html

		int w = Display.GAME_WIDTH;
		int h = Display.GAME_HEIGHT;

		ui = new OrthographicCamera(Display.UI_WIDTH, Display.UI_HEIGHT);
		ui.position.set(Display.UI_WIDTH / 2, Display.UI_HEIGHT / 2, 0);
		ui.update();

		nil = new OrthographicCamera(w, h);
		nil.position.set(Display.GAME_WIDTH / 2, Display.GAME_HEIGHT / 2, 0);
		nil.update();

		alwaysActive = true;
		game = new OrthographicCamera(w, h);
		game.position.set(game.viewportWidth / 2, game.viewportHeight / 2, 0);
		game.update();

		camPosition = new Vector2(game.position.x, game.position.y);

		viewportCamera = new OrthographicCamera(Display.GAME_WIDTH, Display.GAME_HEIGHT);
		viewportCamera.update();

		viewport = new ScreenViewport(viewportCamera);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public static void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void init() {
		super.init();

		depth = 80;
		mousePosition.set(Input.instance.uiMouse.x, Input.instance.uiMouse.y);
	}

	private static Vector2 camPosition;
	private static Room lastRoom;
	private static Vector2 mousePosition = new Vector2();

	@Override
	public void update(float dt) {
		if (last != null && last.done) {
			last = null;
		}

		pushAm = Math.max(0, pushAm - dt * 50);
		shake = Math.max(0, shake - dt * 10);

		if (target != null) {
			int x = (int) (target.x + target.w / 2);
			int y = (int) (target.y + target.h / 2);

			if (target instanceof Player && !((Player) target).toDeath) {
				if (!Dungeon.game.getState().isPaused()) mousePosition = mousePosition.lerp(Input.instance.uiMouse, dt * 3);
			} else {
				y += target.h;
			}

			if (!Dungeon.game.getState().isPaused()) {
				camPosition.lerp(new Vector2(x, y), dt * speed);
			}

			if (Dungeon.depth == -3) {
				Room room = Player.instance.room;

				if (room == null) {
					room = lastRoom;
				} else {
					lastRoom = room;
				}

				if (room != null) {
					game.position.x = MathUtils.clamp(Spawn.instance.room.left * 16 + 16 + Display.GAME_WIDTH / 2,
						Spawn.instance.room.right * 16 - Display.GAME_WIDTH / 2, camPosition.x);
					game.position.y = MathUtils.clamp(Spawn.instance.room.top * 16 + 16 + Display.GAME_HEIGHT / 2 + 16,
						Spawn.instance.room.bottom * 16 - Display.GAME_HEIGHT / 2 - 16, camPosition.y);
				}
			} else {
				float cx = (mousePosition.x - Display.UI_WIDTH / 2) / ((Player.seeMore ? 1.5f : 5f) / game.zoom * Display.UI_SCALE);
				float cy = (mousePosition.y - Display.UI_HEIGHT / 2) / ((Player.seeMore ? 1.5f : 3f) / game.zoom * Display.UI_SCALE);

				game.position.x = camPosition.x + cx;
				game.position.y = camPosition.y + cy;
			}

			game.update();
		}
	}

	private static float mx;
	private static float my;

	public static void applyShake() {
		mx = 0;
		my = 0;

		if (pushAm > 0) {
			mx += (float) Math.cos(pushA) * pushAm;
			my += (float) Math.sin(pushA) * pushAm;
		}

		if (shake > 0) {
			mx += Random.newFloat(-shake / 2, shake / 2);
			my += Random.newFloat(-shake / 2, shake / 2);
		}

		game.position.add(mx, my, 0);

		game.update();
	}

	public static void removeShake() {
		game.position.add(-mx, -my, 0);
		game.update();
	}

	public static void follow(Entity entity) {
		follow(entity, true);
	}

	private static float speed;

	public static void follow(Entity entity, boolean jump) {
		target = entity;
		speed = entity instanceof Player ? (5f) : 4;

		if (target == null) {
			return;
		}

		if (jump) {
			int x = (int) ((Input.instance.uiMouse.x - Display.GAME_WIDTH / 2) / 2 + target.x + 8);
			int y = (int) ((Input.instance.uiMouse.y - Display.GAME_HEIGHT / 2) / 2 + target.y + 8);
			game.position.set(x, y, 0);
			game.update();
			camPosition.set(x, y);
		}
	}
}