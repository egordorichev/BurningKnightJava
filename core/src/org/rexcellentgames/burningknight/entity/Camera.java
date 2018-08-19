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
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
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

		// https://code-disaster.com/2016/02/subpixel-perfect-smooth-scrolling.html

		int w = Display.GAME_WIDTH;
		int h = Display.GAME_HEIGHT;

		ui = new OrthographicCamera(w, h);
		ui.position.set(Display.GAME_WIDTH / 2, Display.GAME_HEIGHT / 2, 0);
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
				x += (Input.instance.uiMouse.x - Display.GAME_WIDTH / 2) / (Player.seeMore ? 1f : 1.5f / game.zoom);
				y += (Input.instance.uiMouse.y - Display.GAME_HEIGHT / 2) / (Player.seeMore ? 1f : 1.5f / game.zoom);
			} else {
				y += target.h;
			}

			float dx = camPosition.x - this.x + 8;
			float dy = camPosition.y - this.y + 8;

			if (Math.sqrt(dx * dx + dy * dy) > 2f) {
				camPosition.lerp(new Vector2(x + 8, y + 8), dt * speed);
			}

			if (Dungeon.depth == -1) {
				Room room = Dungeon.level.getRooms().get(0);

				game.position.x = MathUtils.clamp(room.left * 16 + Display.GAME_WIDTH / 2 + 16,
					room.right * 16 - Display.GAME_WIDTH / 2, camPosition.x);
				game.position.y = MathUtils.clamp(room.top * 16 + Display.GAME_HEIGHT / 2 + 16,
					room.bottom * 16 - Display.GAME_HEIGHT / 2 - 16, camPosition.y);

				if (Player.instance.y > room.bottom * 16 - 24 && !did) {
					did = true;

					Tween.to(new Tween.Task(0, 0.2f) {
						@Override
						public float getValue() {
							return Dungeon.dark;
						}

						@Override
						public void setValue(float value) {
							Dungeon.dark = value;
						}

						@Override
						public void onEnd() {
							Player.instance.generate();
							GlobalSave.put("last_class", Player.instance.getType().id);
							Dungeon.goToLevel(0);
						}
					});
				}
			} else {
				game.position.x = MathUtils.clamp(Display.GAME_WIDTH / 2 * z + 16,
					Level.getWidth() * 16 - Display.GAME_WIDTH / 2 * z - 16, camPosition.x);
				game.position.y = MathUtils.clamp(Display.GAME_HEIGHT / 2 * z + 16,
					Level.getHeight() * 16 - Display.GAME_HEIGHT / 2 * z - 16, camPosition.y);
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
		speed = entity instanceof Player ? 1 : 4;

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