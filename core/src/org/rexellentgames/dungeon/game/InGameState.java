package org.rexellentgames.dungeon.game;

import box2dLight.RayHandler;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.rexellentgames.dungeon.Collisions;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.buff.Buff;
import org.rexellentgames.dungeon.entity.creature.inventory.UiInventory;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.RegularLevel;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.Tween;

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

		Collisions collisions = new Collisions();

		this.world.setContactListener(collisions);
		this.world.setContactFilter(collisions);

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

		((RegularLevel) this.level).loadPassable();

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
		Tween.update(dt);
		this.area.update(dt);
	}

	private void renderGame() {
		this.area.render();
		Viewport viewport = Camera.instance.getViewport();
		this.light.useCustomViewport(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
		this.light.setCombinedMatrix(Camera.instance.getCamera());

		Graphics.batch.end();
		this.light.updateAndRender();
		Graphics.batch.begin();

		// De
		// this.debug.render(this.world, Camera.instance.getCamera().combined);
	}

	private void renderUi() {
		// Inventory
		this.inventory.renderUi();

		// Top ui
		int sz = (int) Math.ceil(57 * ((float) (Player.instance.getHp()) / (float) (Player.instance.getHpMax())));
		Graphics.render(Graphics.ui, 0, 1, Display.GAME_HEIGHT - 33, 6, 2);
		Graphics.batch.draw(Graphics.ui, 27, Display.GAME_HEIGHT - 15, sz, 8, 112,
			0, sz, 8, false, false);

		for (int i = 0; i < Player.instance.getBuffs().size(); i++) {
			Buff buff = Player.instance.getBuffs().get(i);

			int sprite = buff.getSprite();
			int xx = sprite % 32 * 8;
			int yy = (int) (Math.floor(sprite % 32) * 8);

			Graphics.batch.draw(Graphics.buffs, 6 + i * 9, Display.GAME_HEIGHT - 44, 8, 8, xx, yy, 8, 8, false, false);
		}
	}

	@Override
	public void render() {
		Graphics.shape.setProjectionMatrix(Camera.instance.getCamera().combined);

		this.renderGame();
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);
		this.renderUi();

		// Cursor
		float s = (float) (Math.cos(Dungeon.time * 2) * 2) + 16;

		Graphics.render(Graphics.ui, 6, Input.instance.uiMouse.x - 8, Input.instance.uiMouse.y - 8, 1, 1,
			Dungeon.time * 60, s / 2, s / 2, false, false, s, s);

		this.inventory.renderCurrentSlot();
	}
}