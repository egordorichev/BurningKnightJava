package org.rexellentgames.dungeon.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.rexellentgames.dungeon.Display;

public class Camera extends Entity {
	public static Camera instance;

	private com.badlogic.gdx.graphics.Camera camera;
	private Viewport viewport;

	@Override
	public void init() {
		instance = this;

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		this.camera = new OrthographicCamera(Display.GAME_WIDTH, Display.GAME_HEIGHT * (h / w));
		this.camera.position.set(256, 256, 0);
		this.camera.update();

		this.viewport = new ScalingViewport(Scaling.fit, Display.GAME_WIDTH, Display.GAME_HEIGHT, this.camera);
	}

	public void resize(int width, int height) {
		this.viewport.update(width, height);
	}

	@Override
	public void update(float dt) {
		this.camera.update();
	}

	public com.badlogic.gdx.graphics.Camera getCamera() {
		return this.camera;
	}
}