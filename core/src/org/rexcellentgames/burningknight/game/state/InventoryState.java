package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.inventory.Inventory;
import org.rexcellentgames.burningknight.entity.creature.inventory.UiInventory;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.game.Ui;

public class InventoryState extends State {
	// fixme: game should keep track if it saved in this state or in-game

	private Inventory inventory;
	private UiInventory ui;
	private static TextureRegion trashBg = Graphics.getTexture("ui-bg_trash");
	private static Texture texture = trashBg.getTexture();

	@Override
	public void init() {
		super.init();

		Dungeon.setBackground2(Color.valueOf("#555555"));

		inventory = Player.instance.getInventory();
		ui = Player.instance.ui;
		ui.createSlots();
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		ui.update(dt);
	}

	@Override
	public void render() {
		super.render();
		float a = Dungeon.time * 360;

		float w = Display.GAME_WIDTH;
		float h = Display.GAME_HEIGHT;

		float[] vert = new float[] {
			0, 0, 1, 0, 0,
			w, 0, 1, 1, 0,
			w, h, 1, 1, 1,
			0, h, 1, 0, 1
		};

		Graphics.batch.setProjectionMatrix(Camera.ui.combined);
		Graphics.batch.draw(texture, vert, 0, 20);
	}

	@Override
	public void renderUi() {
		super.renderUi();
		ui.render();
		Ui.ui.renderCursor();
	}
}