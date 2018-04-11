package org.rexellentgames.dungeon.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.game.state.ComicsState;
import org.rexellentgames.dungeon.util.MathUtils;
import org.rexellentgames.dungeon.util.Random;

import java.util.ArrayList;

public class Camera extends Entity {
	public static Camera instance;
	public static OrthographicCamera ui;

	private OrthographicCamera camera;
	public Viewport viewport;
	private Entity target;
	private float shake;
	private float vx;
	private float vy;
	public ArrayList<Integer> clamp = new ArrayList<>();

	public void shake(float amount) {
		this.shake = amount;
	}

	@Override
	public void init() {
		instance = this;

		ui = new OrthographicCamera(Display.GAME_WIDTH, Display.GAME_HEIGHT);
		ui.position.set(Display.GAME_WIDTH / 2, Display.GAME_HEIGHT / 2, 0);
		ui.update();

		this.alwaysActive = true;
		this.camera = new OrthographicCamera(Display.GAME_WIDTH, Display.GAME_HEIGHT);
		this.camera.position.set(this.camera.viewportWidth / 2, this.camera.viewportHeight / 2, 0);
		this.camera.update();
		this.viewport = new ScalingViewport(Scaling.fit, Display.GAME_WIDTH, Display.GAME_HEIGHT, this.camera);
	}

	public void resize(int width, int height) {
		this.viewport.update(width, height);
	}

	@Override
	public void update(float dt) {
		if (this.target != null) {
			int x = (int) ((Input.instance.uiMouse.x - Display.GAME_WIDTH / 2) / (2 / this.camera.zoom) + this.target.x + 8);
			int y = (int) ((Input.instance.uiMouse.y - Display.GAME_HEIGHT / 2) / (2 / this.camera.zoom) + this.target.y + 8);
			float z = this.camera.zoom;

			float m = this.clamp.size() == 0 ? Integer.MAX_VALUE : this.clamp.get(0);

			this.camera.position.lerp(new Vector3(x + 8, y + 8, 0), dt * 1f);
			this.camera.position.x = MathUtils.clamp(Display.GAME_WIDTH / 2 * z + 16,
				Math.min(m - Display.GAME_WIDTH / 2, Level.getWidth() * 16 - Display.GAME_WIDTH / 2 * z - 16), this.camera.position.x);
			this.camera.position.y = MathUtils.clamp(Display.GAME_HEIGHT / 2 * z + 16, Level.getHeight() * 16 - Display.GAME_HEIGHT / 2 * z - 16, this.camera.position.y);
			this.camera.update();

			if (!(Dungeon.game.getState() instanceof ComicsState) && this.target.x + this.target.w / 2 > m) {
				this.clamp.remove(0);
			}
		}
	}

	public void applyShake() {
		if (this.shake <= 0) {
			return;
		}

		this.shake -= Gdx.graphics.getDeltaTime() * 10;

		this.vx = Random.newFloat(-this.shake / 2, this.shake / 2);
		this.vy = Random.newFloat(-this.shake / 2, this.shake / 2);
		this.camera.position.add(this.vx, this.vy, 0);
		this.camera.update();
	}

	public void removeShake() {
		this.camera.position.add(-this.vx, -this.vy, 0);
		this.camera.update();
		this.vx = 0;
		this.vy = 0;
	}

	public OrthographicCamera getCamera() {
		return this.camera;
	}

	public void follow(Entity entity) {
		this.target = entity;

		if (this.target == null) {
			return;
		}

		int x = (int) ((Input.instance.uiMouse.x - Display.GAME_WIDTH / 2) / 2 + this.target.x + 8);
		int y = (int) ((Input.instance.uiMouse.y - Display.GAME_HEIGHT / 2) / 2 + this.target.y + 8);
		this.camera.position.set(x, y, 0);
		this.camera.update();
	}
}