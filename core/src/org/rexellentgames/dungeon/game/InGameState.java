package org.rexellentgames.dungeon.game;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.RegularLevel;

public class InGameState extends State {
	private static final float TIME_STEP = 1 / 45.f;

	private Area area;
	private Level level;
	private Box2DDebugRenderer debug;
	private float accumulator = 0;
	private Player player;

	@Override
	public void init() {
		this.area = new Area(this);
		this.world = new World(new Vector2(0, 0), true);
		this.debug = new Box2DDebugRenderer();

		this.light = new RayHandler(this.world);
		this.light.setBlurNum(1);
		this.light.setAmbientLight(0f);

		this.area.add(new Camera());
		this.level = (Level) this.area.add(new RegularLevel());

		this.player = (Player) this.area.add(new Player());
		//player.getBody().setTransform(300, 300, 0);

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
	}

	@Override
	public void render() {
		this.area.render();

		Viewport viewport = Camera.instance.getViewport();

		this.light.useCustomViewport(viewport.getScreenX(), viewport.getScreenY(),
			viewport.getScreenWidth(), viewport.getScreenHeight());

		this.light.setCombinedMatrix((OrthographicCamera) Camera.instance.getCamera());
		this.light.updateAndRender();
		this.debug.render(this.world, Camera.instance.getCamera().combined);
	}
}