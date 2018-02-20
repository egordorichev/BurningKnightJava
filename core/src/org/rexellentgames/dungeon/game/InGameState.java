package org.rexellentgames.dungeon.game;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.RegularLevel;
import org.rexellentgames.dungeon.util.Log;

public class InGameState extends State {
	private static final float TIME_STEP = 1 / 45.f;

	private Area area;
	private Level level;
	private float accumulator = 0;
	private PointLight point;

	@Override
	public void init() {
		this.area = new Area(this);
		this.world = new World(new Vector2(0, 0), true);

		this.area.add(new Camera());
		this.level = (Level) this.area.add(new RegularLevel());

		new Thread(new Runnable() {
			@Override
			public void run() {
				level.generate();
				// level.load();
			}
		}).run();

		this.light = new RayHandler(this.world);
		this.light.setBlurNum(1);
		this.light.setAmbientLight(0f);
		this.point = new PointLight(this.light, 128, new Color(1, 1, 0.8f, 0.8f), 512, 300, 300);
	}

	@Override
	public void destroy() {
		super.destroy();

		this.level.save();

		this.world.dispose();
		this.light.dispose();
	}

	private void doPhysicsStep(float deltaTime) {
		float frameTime = Math.min(deltaTime, 0.25f);
		this.accumulator += frameTime;

		while (accumulator >= TIME_STEP) {
			this.world.step(TIME_STEP, 6, 2);
			this.accumulator -= TIME_STEP;
		}
	}

	@Override
	public void update(float dt) {
		this.area.update(dt);
		this.doPhysicsStep(dt);

		this.point.setPosition(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
	}

	@Override
	public void render() {
		this.area.render();
		this.light.setCombinedMatrix(Camera.instance.getCamera().combined);
		Graphics.batch.end();
		// this.light.updateAndRender();
		Graphics.batch.begin();
		this.area.renderUi();
	}
}