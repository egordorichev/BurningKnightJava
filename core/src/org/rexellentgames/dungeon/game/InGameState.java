package org.rexellentgames.dungeon.game;

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
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
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.Tween;

public class InGameState extends State {
	private static final float TIME_STEP = 1 / 45.f;

	private Box2DDebugRenderer debug;
	private float accumulator = 0;
	private UiInventory inventory;

	@Override
	public void init() {
		Collisions collisions = new Collisions();

		Dungeon.world.setContactListener(collisions);
		Dungeon.world.setContactFilter(collisions);

		this.debug = new Box2DDebugRenderer();

		Camera.instance.follow(Player.instance);

		this.inventory = new UiInventory(Player.instance.getInventory());
		Dungeon.area.add(this.inventory);
	}

	@Override
	public void destroy() {
		super.destroy();

		Dungeon.level.save(Level.DataType.PLAYER);
		Dungeon.level.save(Level.DataType.LEVEL);

		if (Dungeon.area != null) {
			Dungeon.area.destroy();
		}
	}

	private void doPhysicsStep(float deltaTime) {
		float frameTime = Math.min(deltaTime, 0.25f);
		this.accumulator += frameTime;

		while (accumulator >= TIME_STEP) {
			Dungeon.world.step(TIME_STEP, 6, 2);
			this.accumulator -= TIME_STEP;
		}
	}

	@Override
	public void update(float dt) {
		Input.instance.updateMousePosition();
		this.doPhysicsStep(dt);
		Tween.update(dt);
		Dungeon.area.update(dt);
	}

	private void renderGame() {
		Dungeon.area.render();

		Viewport viewport = Camera.instance.getViewport();
		Dungeon.light.useCustomViewport(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
		Dungeon.light.setCombinedMatrix(Camera.instance.getCamera());

		Graphics.batch.end();
		Dungeon.light.updateAndRender();
		Graphics.batch.begin();

		// Debug
		// this.debug.render(Dungeon.world, Camera.instance.getCamera().combined);
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