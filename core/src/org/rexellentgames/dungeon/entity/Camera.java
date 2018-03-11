package org.rexellentgames.dungeon.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.Log;

public class Camera extends Entity {
	public static Camera instance;
	public static OrthographicCamera ui;

	private OrthographicCamera camera;
	private Viewport viewport;
	private Entity target;

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
			int x = (int) ((Input.instance.uiMouse.x - Display.GAME_WIDTH / 2) / 2 + this.target.x + 8);
			int y = (int) ((Input.instance.uiMouse.y - Display.GAME_HEIGHT / 2) / 2 + this.target.y + 8);

			this.camera.position.lerp(new Vector3(x + 8, y + 8, 0), dt * 1f);
			this.camera.position.x = (float) (Math.floor(this.camera.position.x * 16) / 16);
			this.camera.position.y = (float) (Math.floor(this.camera.position.y * 16) / 16);
			this.camera.update();
		}
	}

	public OrthographicCamera getCamera() {
		return this.camera;
	}

	public void follow(Entity entity) {
		this.target = entity;
		int x = (int) ((Input.instance.uiMouse.x - Display.GAME_WIDTH / 2) / 2 + this.target.x + 8);
		int y = (int) ((Input.instance.uiMouse.y - Display.GAME_HEIGHT / 2) / 2 + this.target.y + 8);
		this.camera.position.set(x, y, 0);
		this.camera.update();
	}

	public Viewport getViewport() {
		return this.viewport;
	}
}