package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.inventory.Inventory;
import org.rexcellentgames.burningknight.entity.creature.inventory.UiInventory;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.save.GameSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.ui.UiButton;

public class InventoryState extends State {
	private Inventory inventory;
	private UiInventory ui;
	public static int depth;
	public static Texture texture = new Texture(Gdx.files.internal("bricks.png"));

	@Override
	public void init() {
		super.init();

		Dungeon.dark = 1;
		Dungeon.white = 0;
		Dungeon.darkR = Dungeon.MAX_R;

		Dungeon.setBackground2(Color.valueOf("#555555"));

		inventory = Player.instance.getInventory();

		if (Player.instance.ui == null) {
			depth = Dungeon.depth + 1;
			UiInventory inventory = new UiInventory(Player.instance.getInventory());
			Dungeon.ui.add(inventory);
		}

		ui = Player.instance.ui;
		ui.createSlots();

		Dungeon.area.destroy();

		Dungeon.ui.add(new UiButton("go", Display.UI_WIDTH / 2, (int) (ui.slots[0].y + 14 + 32)) {
			@Override
			public void onClick() {
				super.onClick();

				for (int i = 8; i < inventory.getSize(); i++) {
					inventory.setSlot(i, null);
				}

				GameSave.inventory = false;
				SaveManager.saveGame();
				SaveManager.saveGames();
				Dungeon.goToLevel(InventoryState.depth);
			}
		});
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		ui.update(dt);
	}

	public static ShaderProgram shader;

	static {
		shader = new ShaderProgram(Gdx.files.internal("shaders/default.vert").readString(), Gdx.files.internal("shaders/tunnel.frag").readString());
		if (!shader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());
	}

	@Override
	public void render() {
		super.render();

		Graphics.batch.setProjectionMatrix(Camera.ui.combined);
		Graphics.batch.end();
		shader.begin();
		shader.setUniformf("sign", 1);
		shader.setUniformf("time", Dungeon.time * 0.1f);
		shader.end();
		Graphics.batch.setShader(shader);
		Graphics.batch.begin();

		float s = 1f;

		for (int x = 0; x < Display.UI_WIDTH; x += texture.getWidth() * s) {
			for (int y = 0; y < Display.UI_WIDTH; y += texture.getWidth() * s) {
				Graphics.batch.draw(texture, x, y, texture.getWidth() * s, texture.getHeight() * s);
			}
		}

		Graphics.batch.end();
		Graphics.batch.setShader(null);
		Graphics.batch.begin();
	}

	@Override
	public void renderUi() {
		ui.render();
		Dungeon.ui.render();
		Ui.ui.renderCursor();
	}
}