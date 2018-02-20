package org.rexellentgames.dungeon.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Camera extends Entity {
	public static Camera instance;

	private com.badlogic.gdx.graphics.Camera camera;

	@Override
	public void init() {
		instance = this;

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		this.camera = new OrthographicCamera(512, 512 * (h / w));

		this.camera.position.set(this.camera.viewportWidth / 2f, this.camera.viewportHeight / 2f, 0);
		this.camera.update();
	}

	@Override
	public void update(float dt) {
		this.camera.update();
	}

	public com.badlogic.gdx.graphics.Camera getCamera() {
		return this.camera;
	}
}