package org.rexellentgames.dungeon.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Settings;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.MathUtils;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.Tween;

public class Camera extends Entity {
	public static Camera instance;
	public static OrthographicCamera ui;
	public static OrthographicCamera nil;
	public static OrthographicCamera game;
	public static Viewport viewport;
	public static Entity target;
	private static float shake;
	private static float pushA;
	private static float pushAm;

	public static void shake(float amount) {
		shake = amount * Settings.screenshake;
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

		ui = new OrthographicCamera(Display.GAME_WIDTH, Display.GAME_HEIGHT);
		ui.position.set(Display.GAME_WIDTH / 2, Display.GAME_HEIGHT / 2, 0);
		ui.update();

		nil = new OrthographicCamera(Display.GAME_WIDTH, Display.GAME_HEIGHT);
		nil.position.set(Display.GAME_WIDTH / 2, Display.GAME_HEIGHT / 2, 0);
		nil.update();

		alwaysActive = true;
		game = new OrthographicCamera(Display.GAME_WIDTH, Display.GAME_HEIGHT);
		game.position.set(game.viewportWidth / 2, game.viewportHeight / 2, 0);
		game.zoom = 0.8f;
		game.update();

		this.camPosition = new Vector2(game.position.x, game.position.y);

		viewport = new ScalingViewport(Scaling.fit, Display.GAME_WIDTH, Display.GAME_HEIGHT, game);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public static void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void init() {
		super.init();

		depth = 80;
	}

	private static Vector2 camPosition;

	@Override
	public void update(float dt) {
		if (last != null && last.done) {
			last = null;
		}

		pushAm = Math.max(0, pushAm - dt * 50);
		shake = Math.max(0, shake - dt * 10);

		if (target != null) {
			int x = (int) (target.x + 8);
			int y = (int) (target.y + 8);
			float z = game.zoom;

			if (target instanceof Player && !((Player) target).toDeath) {
				x += (Input.instance.uiMouse.x - Display.GAME_WIDTH / 2) / (1.5f / game.zoom);
				y += (Input.instance.uiMouse.y - Display.GAME_HEIGHT / 2) / (1.5f / game.zoom);
			} else {
				y += target.h;
			}

			camPosition.lerp(new Vector2(x + 8, y + 8), dt * 1f);

			float s = 4;

			game.position.x = MathUtils.clamp(Display.GAME_WIDTH / 2 * z + 16,
				Level.getWidth() * 16 - Display.GAME_WIDTH / 2 * z - 16, (float) (Math.round(camPosition.x * s) / s));
			game.position.y = MathUtils.clamp(Display.GAME_HEIGHT / 2 * z + 16,
				Level.getHeight() * 16 - Display.GAME_HEIGHT / 2 * z - 16, (float) (Math.round(camPosition.y * s) / s));

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

	public static void follow(Entity entity, boolean jump) {
		target = entity;

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