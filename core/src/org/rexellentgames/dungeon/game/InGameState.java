package org.rexellentgames.dungeon.game;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.rexellentgames.dungeon.Collisions;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.inventory.UiInventory;
import org.rexellentgames.dungeon.entity.creature.mob.BurningKnight;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.RegularLevel;
import org.rexellentgames.dungeon.game.input.Input;

public class InGameState extends State {
	private static final float TIME_STEP = 1 / 45.f;

	private Level level;
	private Box2DDebugRenderer debug;
	private float accumulator = 0;
	private UiInventory inventory;

	@Override
	public void init() {
		this.area = new Area(this);
		this.world = new World(new Vector2(0, 0), true);
		this.world.setContactListener(new Collisions());

		this.debug = new Box2DDebugRenderer();

		this.light = new RayHandler(this.world);
		this.light.setBlurNum(10);
		this.light.setAmbientLight(0f);

		this.area.add(new Camera());
		this.level = (Level) this.area.add(new RegularLevel());

		new Thread(new Runnable() {
			@Override
			public void run() {
				level.load();
			}
		}).run();

		this.area.add(new BurningKnight());

		this.inventory = new UiInventory(Player.instance.getInventory());
		this.area.add(this.inventory);
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
		Input.instance.updateMousePosition();
		this.doPhysicsStep(dt);
		this.area.update(dt);
	}

	@Override
	public void render() {
		this.area.render();

		Viewport viewport = Camera.instance.getViewport();

		this.light.useCustomViewport(viewport.getScreenX(), viewport.getScreenY(),
			viewport.getScreenWidth(), viewport.getScreenHeight());

		this.light.setCombinedMatrix(Camera.instance.getCamera());

		Graphics.batch.end();
		this.light.updateAndRender();
		Graphics.batch.begin();

		this.debug.render(this.world, Camera.instance.getCamera().combined);

		Graphics.batch.setProjectionMatrix(Camera.ui.combined);
		Graphics.render(Graphics.ui, 0, 1, Display.GAME_HEIGHT - 33, 6, 2);

		Graphics.render(Graphics.ui, 6, Input.instance.uiMouse.x - 8, Input.instance.uiMouse.y - 8);
		this.inventory.renderUi();
	}
}