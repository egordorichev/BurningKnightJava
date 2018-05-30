package org.rexellentgames.dungeon.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
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

import java.util.ArrayList;

public class Camera extends Entity {
	public static Camera instance;
	public static OrthographicCamera ui;

	private OrthographicCamera camera;
	public Viewport viewport;
	private Entity target;
	private float shake;
	private float pushA;
	private float pushAm;
	public ArrayList<Rectangle> clamp = new ArrayList<>();

	public void shake(float amount) {
		this.shake = amount * Settings.screenshake;
	}

	private Tween.Task last;

	public void push(float a, float am) {
		this.pushA = a;
		this.pushAm = 0;

		if (this.last != null) {
			Tween.remove(this.last);
		}

		this.last = Tween.to(new Tween.Task(am * Settings.screenshake, 0.05f) {
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

		this.alwaysActive = true;
		this.camera = new OrthographicCamera(Display.GAME_WIDTH, Display.GAME_HEIGHT);
		this.camera.position.set(this.camera.viewportWidth / 2, this.camera.viewportHeight / 2, 0);
		this.camera.zoom = 0.8f;
		this.camera.update();
		this.viewport = new ScalingViewport(Scaling.fit, Display.GAME_WIDTH, Display.GAME_HEIGHT, this.camera);
		this.viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public void resize(int width, int height) {
		this.viewport.update(width, height);
	}

	@Override
	public void update(float dt) {
		if (this.last != null && this.last.done) {
			this.last = null;
		}

		ui.position.set(Display.GAME_WIDTH / 2, Display.GAME_HEIGHT / 2, 0);
		ui.update();

		this.pushAm = Math.max(0, this.pushAm - dt * 50);
		this.shake = Math.max(0, this.shake - dt * 10);

		if (this.target != null) {
			int x = (int) (this.target.x + 8);
			int y = (int) (this.target.y + 8);
			float z = this.camera.zoom;

			if (this.target instanceof Player && !((Player) this.target).toDeath) {
				x += (Input.instance.uiMouse.x - Display.GAME_WIDTH / 2) / (1.5f / this.camera.zoom);
				y += (Input.instance.uiMouse.y - Display.GAME_HEIGHT / 2) / (1.5f / this.camera.zoom);

				x += ((Player) this.target).vel.x / 2;
				y += ((Player) this.target).vel.y / 2;
			}

			this.camera.position.lerp(new Vector3(x + 8, y + 8, 0), dt * 1f);

			this.camera.position.x = MathUtils.clamp(Display.GAME_WIDTH / 2 * z + 16,
				Level.getWidth() * 16 - Display.GAME_WIDTH / 2 * z - 16, this.camera.position.x);
			this.camera.position.y = MathUtils.clamp(Display.GAME_HEIGHT / 2 * z + 16,
				Level.getHeight() * 16 - Display.GAME_HEIGHT / 2 * z - 16, this.camera.position.y);

			/*if (this.clamp.size() > 0) {
				Rectangle rect = this.clamp.get(0);

				// Todo: make sure it's off-screen

				this.camera.position.x =
					this.camera.position.x < rect.x + rect.width / 2
					? rect.x : rect.x + rect.width;

				this.camera.position.y =
					this.camera.position.y < rect.y + rect.height / 2
						? rect.y : rect.y + rect.height;

				float tx = this.target.x + this.target.w / 2;
				float ty = this.target.y + this.target.h / 2;

				if (tx >= rect.x && tx <= rect.x + rect.width &&
					ty >= rect.y && ty <= rect.y + rect.height) {

					this.clamp.remove(0);
				}
			}*/

			this.camera.update();
		}
	}

	private float mx;
	private float my;

	public void applyShake() {
		mx = 0;
		my = 0;

		if (this.pushAm > 0) {
			mx += (float) Math.cos(this.pushA) * this.pushAm;
			my += (float) Math.sin(this.pushA) * this.pushAm;
		}

		if (this.shake > 0) {
			this.mx += Random.newFloat(-this.shake / 2, this.shake / 2);
			this.my += Random.newFloat(-this.shake / 2, this.shake / 2);
		}

		this.camera.position.add(this.mx, this.my, 0);

		this.camera.update();
	}

	public void removeShake() {
		this.camera.position.add(-this.mx, -this.my, 0);
		this.camera.update();
	}

	public OrthographicCamera getCamera() {
		return this.camera;
	}

	public void follow(Entity entity) {
		follow(entity, true);
	}

	public void follow(Entity entity, boolean jump) {
		this.target = entity;

		if (this.target == null) {
			return;
		}

		if (jump) {
			int x = (int) ((Input.instance.uiMouse.x - Display.GAME_WIDTH / 2) / 2 + this.target.x + 8);
			int y = (int) ((Input.instance.uiMouse.y - Display.GAME_HEIGHT / 2) / 2 + this.target.y + 8);
			this.camera.position.set(x, y, 0);
			this.camera.update();
		}
	}
}