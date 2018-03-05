package org.rexellentgames.dungeon.game;

import com.badlogic.gdx.Gdx;
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
import org.rexellentgames.dungeon.ui.Bar;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Tween;

public class InGameState extends State {
	private static final boolean DRAW_DEBUG = false;
	private static final float TIME_STEP = 1 / 45.f;

	private Box2DDebugRenderer debug;
	private float accumulator = 0;
	private UiInventory inventory;
	private Console console;
	private Bar health;
	private Bar mana;
	private Bar exp;
	private int lastLevel;
	private int w;

	@Override
	public void init() {
		this.debug = new Box2DDebugRenderer();
		Collisions collisions = new Collisions();

		Dungeon.world.setContactListener(collisions);
		Dungeon.world.setContactFilter(collisions);

		Camera.instance.follow(Player.instance);

		this.setupUi();
		this.console = new Console();
	}

	@Override
	public void destroy() {
		super.destroy();

		this.console.destroy();

		if (Dungeon.reset) {
			Gdx.files.external(".ldg/").deleteDirectory();
			Dungeon.reset = false;
		} else {
			Dungeon.level.save(Level.DataType.PLAYER);
			Dungeon.level.save(Level.DataType.LEVEL);
		}

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
		Dungeon.ui.update(dt);
		UiLog.instance.update(dt);

		this.health.setValue(Player.instance.getHp());
		this.mana.setValue(Player.instance.getMana());
		this.exp.setValue(Player.instance.getExperienceForLevel());

		this.health.setMax(Player.instance.getHpMax());
		this.mana.setMax(Player.instance.getManaMax());
		this.exp.setMax(Player.instance.getExperienceMaxForLevel());
	}

	private void renderGame() {
		Dungeon.area.render();

		Viewport viewport = Camera.instance.getViewport();
		Dungeon.light.useCustomViewport(viewport.getScreenX(), viewport.getScreenY(), viewport.getScreenWidth(), viewport.getScreenHeight());
		Dungeon.light.setCombinedMatrix(Camera.instance.getCamera());

		if (Dungeon.depth > 1) {
			Graphics.batch.end();
			Dungeon.light.updateAndRender();
			Graphics.batch.begin();
		}

		// Debug
		if (DRAW_DEBUG) {
			this.debug.render(Dungeon.world, Camera.instance.getCamera().combined);
		}
	}

	private void renderUi() {
		Graphics.render(Graphics.ui, 0, 0, Display.GAME_HEIGHT - 32, 5, 2);
		Dungeon.ui.render();

		if (Player.instance.getLevel() != this.lastLevel) {
			this.lastLevel = Player.instance.getLevel();
			Graphics.layout.setText(Graphics.medium, String.valueOf(this.lastLevel));
			this.w = (int) Graphics.layout.width;
		}

		Graphics.medium.draw(Graphics.batch, String.valueOf(Player.instance.getLevel()), 3 + (16 - this.w) / 2, Display.GAME_HEIGHT - 8);

		Buff[] buffs = Player.instance.getBuffs().toArray(new Buff[] {});

		for (int i = 0; i < buffs.length; i++) {
			Buff buff = buffs[i];

			int sprite = buff.getSprite();
			int xx = sprite % 32 * 8;
			int yy = (int) (Math.floor(sprite / 32) * 8);

			Graphics.batch.draw(Graphics.buffs, 2 + i * 9, Display.GAME_HEIGHT - 36, xx, yy, 8, 8);
		}

		if (this.health.hovered) { this.health.renderInfo(); }
		if (this.mana.hovered) { this.mana.renderInfo(); }
		if (this.exp.hovered) { this.exp.renderInfo(); }

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

	private void setupUi() {
		this.inventory = new UiInventory(Player.instance.getInventory());
		this.health = new Bar();

		this.health.w = 49;
		this.health.h = 8;
		this.health.x = 25;
		this.health.y = Display.GAME_HEIGHT - 12;
		this.health.setTexture(112, 0);

		this.mana = new Bar();

		this.mana.w = 37;
		this.mana.h = 6;
		this.mana.x = 23;
		this.mana.y = Display.GAME_HEIGHT - 23;
		this.mana.setTexture(112, 16);

		this.exp = new Bar();

		this.exp.w = 16;
		this.exp.h = 19;
		this.exp.x = 2;
		this.exp.y = Display.GAME_HEIGHT - 22;
		this.exp.vertical = true;
		this.exp.setTexture(80, 0);

		Dungeon.ui.add(this.inventory);
		Dungeon.ui.add(this.health);
		Dungeon.ui.add(this.mana);
		Dungeon.ui.add(this.exp);
	}
}