package org.rexellentgames.dungeon.game;

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.rexellentgames.dungeon.Collisions;
import org.rexellentgames.dungeon.Display;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.UiLog;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.debug.Console;
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
	private int lastLevel;
	private float w;
	private Console console;

	@Override
	public void init() {
		Collisions collisions = new Collisions();

		Dungeon.world.setContactListener(collisions);
		Dungeon.world.setContactFilter(collisions);

		this.debug = new Box2DDebugRenderer();

		Camera.instance.follow(Player.instance);

		this.inventory = new UiInventory(Player.instance.getInventory());
		Dungeon.area.add(this.inventory);

		this.console = new Console();
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
		this.console.update(dt);

		Input.instance.updateMousePosition();
		this.doPhysicsStep(dt);
		Tween.update(dt);
		Dungeon.area.update(dt);
		UiLog.instance.update(dt);
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

		// Log
		UiLog.instance.render();

		// Top ui
		Graphics.render(Graphics.ui, 0, 1, Display.GAME_HEIGHT - 33, 5, 2);

		int sz = (int) Math.ceil(57 * ((float) (Player.instance.getHp()) / (float) (Player.instance.getHpMax())));

		Graphics.batch.draw(Graphics.ui, 27, Display.GAME_HEIGHT - 15, sz, 8, 112,
			0, sz, 8, false, false);

		sz = (int) Math.ceil(19 * ((float) (Player.instance.getExperience()) / (float) (Player.instance.getExperienceMax())));

		if (Player.instance.getLevel() != this.lastLevel) {
			this.lastLevel = Player.instance.getLevel();
			Graphics.layout.setText(Graphics.medium, String.valueOf(this.lastLevel));
			this.w = Graphics.layout.width;
		}

		Graphics.batch.draw(Graphics.ui, 4, Display.GAME_HEIGHT - 25, 16, sz, 80,
			19 - sz, 16, sz, false, false);

		Graphics.medium.draw(Graphics.batch, String.valueOf(Player.instance.getLevel()), 4 + (16 - this.w) / 2, Display.GAME_HEIGHT - 10);

		sz = (int) Math.ceil(37 * ((float) (Player.instance.getMana()) / (float) (Player.instance.getManaMax())));

		Graphics.batch.draw(Graphics.ui, 25, Display.GAME_HEIGHT - 25, sz, 6, 112,
			16, sz, 6, false, false);

		for (int i = 0; i < Player.instance.getBuffs().size(); i++) {
			Buff buff = Player.instance.getBuffs().get(i);

			int sprite = buff.getSprite();
			int xx = sprite % 32 * 8;
			int yy = (int) (Math.floor(sprite % 32) * 8);

			Graphics.batch.draw(Graphics.buffs, 6 + i * 9, Display.GAME_HEIGHT - 44, 8, 8, xx, yy, 8, 8, false, false);
		}

		this.console.render();
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