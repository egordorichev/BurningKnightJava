package org.rexcellentgames.burningknight.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.rexcellentgames.burningknight.*;
import org.rexcellentgames.burningknight.entity.creature.mob.boss.BurningKnight;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.creature.player.Spawn;
import org.rexcellentgames.burningknight.entity.level.rooms.FloatingRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.util.MathUtils;
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
	private static float st;
	public static boolean noMove;

	public void resetShake() {
		shake = 0;
		pushA = 0;
		pushAm = 0;
		st = 0;
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
		if (instance != null) {
			instance.done = true;
			instance = this;
			return;
		}

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
	private static float t;
	private static Vector2 offset = new Vector2();
	private static Vector2 velocity = new Vector2();
	private static boolean ignoreMouse;

	@Override
	public void update(float dt) {
		if (last != null && last.done) {
			last = null;
		}

		if (Version.debug) {
			if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.NUMPAD_0)) {
				if (target == null) {
					follow(Player.instance, false);
				} else {
					follow(null);
				}
			}

			if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.NUMPAD_7)) {
				ignoreMouse = !ignoreMouse;
			}

			if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.NUMPAD_5)) {
				offset.x = 0;
				offset.y = 0;
			}

			float s = dt * 230;

			if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.NUMPAD_4)) {
				velocity.x -= s;
			}

			if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.NUMPAD_6)) {
				velocity.x += s;
			}

			if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.NUMPAD_8)) {
				velocity.y += s;
			}

			if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.NUMPAD_2)) {
				velocity.y -= s;
			}

			offset.x += velocity.x * dt;
			offset.y += velocity.y * dt;

			velocity.x -= velocity.x * dt * 3;
			velocity.y -= velocity.y * dt * 3;
		}

		if (Dungeon.game.getState() != null && !Dungeon.game.getState().isPaused()) {
			t += dt;
		}

		st = Math.max(0, st - dt * 1f);

		pushAm = Math.max(0, pushAm - dt * 20);
		shake = Math.max(0, shake - dt * 10);

		if (target != null && !noMove) {
			int x = (int) (target.x + target.w / 2);
			int y = (int) (target.y + target.h / 2);

			if (target instanceof Player && !((Player) target).toDeath) {
				if (!Dungeon.game.getState().isPaused() && !ignoreMouse) {
					mousePosition.x = Input.instance.worldMouse.x;
					mousePosition.y = Input.instance.worldMouse.y;
				}
			} else {
				y += target.h;
			}

			if (!Dungeon.game.getState().isPaused()) {
				camPosition = camPosition.lerp(new Vector2(x, y), dt * speed);

				if (target instanceof Player) {
					if (!Player.instance.isDead() && BurningKnight.instance != null && !BurningKnight.instance.getState().equals("unactive") && !BurningKnight.instance.getState().equals("defeated")) {
						camPosition = camPosition.lerp(new Vector2(BurningKnight.instance.x + BurningKnight.instance.w / 2, BurningKnight.instance.y + BurningKnight.instance.h / 2), dt * speed * 0.1f);
					}

					camPosition = camPosition.lerp(new Vector2(mousePosition.x, mousePosition.y), dt * speed * 0.25f);
					Player p = (Player) target;

					if (p.room != null && p.room.lastNumEnemies > 0 && !(p.room instanceof FloatingRoom)) {
						camPosition = camPosition.lerp(new Vector2(p.room.getCenter().x * 16 + 8, p.room.getCenter().y * 16 + 8), dt * speed * 0.5f);
					}
				}
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
				game.position.x = camPosition.x + offset.x;
				game.position.y = camPosition.y + offset.y;
			}

			game.update();
		}
	}

	public static float ma;

	public static void applyShake() {
		if (!noMove) {
			float mx;
			float my;
			float shake = st * st;
			float tt = t * 13;

			if (shake > 0.1f) {
				mx = Noise.instance.noise(tt) * shake;
				my = (Noise.instance.noise(tt + 1) * shake);
				ma = (Noise.instance.noise(tt + 2) * shake * 0.5f);
			} else {
				mx = 0;
				my = 0;
				ma = 0;
			}

			if (Dungeon.blood > 0) {
				ma += (Noise.instance.noise(t * 3 + 3) * Dungeon.blood * 6);
			}

			if (pushAm > 0) {
				float v = pushAm * pushAm * 0.3f;
				mx += (float) Math.cos(pushA - Math.PI) * v;
				my += (float) Math.sin(pushA - Math.PI) * v;
			}

			game.position.add(mx, my, 0);
		}

		game.update();
	}

	public static void shake(float amount) {
		st = Math.min(Settings.screenshake * 4, st + amount * Settings.screenshake * 0.5f);
	}

	public static void removeShake() {
		game.position.x = camPosition.x + offset.x;
		game.position.y = camPosition.y + offset.y;
		game.update();
	}

	public static void follow(Entity entity) {
		follow(entity, true);
	}

	private static float speed;

	public static void follow(Entity entity, boolean jump) {
		target = entity;
		speed = entity instanceof Player ? (4.5f) : 4;

		if (target == null) {
			return;
		}

		if (jump) {
			float a = 1;
			float b = 1 - a;
			int x = (int) (((target.x + target.w / 2) * a + (Input.instance.worldMouse.x) * b));
			int y = (int) (((target.y + target.h / 2) * a + (Input.instance.worldMouse.y) * b));
			camPosition.set(x, y);
			game.position.set(x, y, 0);
			game.update();
		}
	}

	{
		alwaysActive = true;
		alwaysRender = true;
	}
}