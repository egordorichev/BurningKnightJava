package org.rexellentgames.dungeon.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.RegularLevel;
import org.rexellentgames.dungeon.util.Log;

public class InGameState extends State {
	private static final float TIME_STEP = 1 / 45.f;

	private Area area;
	private Box2DDebugRenderer debug;
	private float accumulator = 0;

	@Override
	public void init() {
		this.area = new Area(this);
		this.world = new World(new Vector2(0, 0), true);
		this.debug = new Box2DDebugRenderer();

		this.area.add(new Camera());
		final Level level = (Level) this.area.add(new RegularLevel());

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!level.generate()) {
					Log.error("Failed to generate the level!");
				}
			}
		}).run();
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
	}

	@Override
	public void render() {
		this.area.render();
		this.debug.render(this.world, Camera.instance.getCamera().combined);
	}
}