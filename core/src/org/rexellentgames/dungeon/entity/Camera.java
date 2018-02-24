package org.rexellentgames.dungeon.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.*;
import org.rexellentgames.dungeon.Display;

public class Camera extends Entity {
	public static Camera instance;

	private com.badlogic.gdx.graphics.Camera camera;
	private Viewport viewport;
	private Entity target;

	@Override
	public void init() {
		instance = this;

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
			this.camera.position.lerp(new Vector3(this.target.x + 8, this.target.y + 8, 0), dt * 1f);
			this.camera.position.x = (float) (Math.floor(this.camera.position.x * 16) / 16);
			this.camera.position.y = (float) (Math.floor(this.camera.position.y * 16) / 16);
			this.camera.update();
		}
	}

	public com.badlogic.gdx.graphics.Camera getCamera() {
		return this.camera;
	}

	public void follow(Entity entity) {
		this.target = entity;
		this.camera.position.set(entity.x + 8, entity.y + 8, 0);
		this.camera.update();
	}

	public void apply() {
		this.viewport.apply();
	}

	public Viewport getViewport() {
		return this.viewport;
	}
}