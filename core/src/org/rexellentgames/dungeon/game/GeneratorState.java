package org.rexellentgames.dungeon.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.RegularLevel;

public class GeneratorState extends State {
	private static final float TIME_STEP = 1 / 45.f;

	private Area area;
	private Level level;
	private float accumulator;

	@Override
	public void init() {
		this.area = new Area(this);
		this.world = new World(new Vector2(0, 0), true);

		this.area.add(new Camera());
		this.level = (Level) this.area.add(new RegularLevel());

		new Thread(new Runnable() {
			@Override
			public void run() {
				level.generateUntilGood();
			}
		}).run();
	}

	@Override
	public void destroy() {
		super.destroy();

		this.level.save();
		this.world.dispose();
	}

	@Override
	public void update(float dt) {
		this.area.update(dt);
		this.doPhysicsStep(dt);
	}

	@Override
	public void render() {
		this.area.render();
	}

	private void doPhysicsStep(float deltaTime) {
		float frameTime = Math.min(deltaTime, 0.25f);
		this.accumulator += frameTime;

		while (accumulator >= TIME_STEP) {
			this.world.step(TIME_STEP, 6, 2);
			this.accumulator -= TIME_STEP;
		}
	}
}