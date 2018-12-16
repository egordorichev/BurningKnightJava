package org.rexcellentgames.burningknight.game.state;

import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.inventory.Inventory;
import org.rexcellentgames.burningknight.entity.creature.inventory.UiInventory;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.save.GameSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.ui.UiEntity;
import org.rexcellentgames.burningknight.util.Tween;

public class InventoryState extends State {
	private Inventory inventory;
	private UiInventory ui;
	public static int depth;


	@Override
	public void init() {
		super.init();
		Audio.play("Void");

		Dungeon.buildDiscordBadge();
		Dungeon.dark = 1;

		Dungeon.white = 0;
		Dungeon.darkR = Dungeon.MAX_R;
		Dungeon.ui.destroy();
		Dungeon.area.destroy();

		inventory = Player.instance.getInventory();

		if (Player.instance.ui == null) {
			depth = Dungeon.depth + 1;
			UiInventory inventory = new UiInventory(Player.instance.getInventory());
		}


		ui = Player.instance.ui;
		ui.createSlots();


		go = (UiEntity) Dungeon.ui.add(new UiButton("go", Display.UI_WIDTH / 2, (int) (ui.slots[0].y + 14 + 32)) {
			@Override
			public void onClick() {
				super.onClick();

				for (int i = 8; i < inventory.getSize(); i++) {
					inventory.setSlot(i, null);
				}

				Player.instance.modifyMana(10000);
				GameSave.inventory = false;
				SaveManager.saveGame();
				SaveManager.saveGames();


				Tween.to(new Tween.Task(Display.UI_HEIGHT, 0.2f, Tween.Type.QUAD_IN) {
					@Override
					public float getValue() {
						return uiY;
					}

					@Override
					public void setValue(float value) {
						uiY = value;
					}

					@Override
					public void onEnd() {
						Dungeon.goToLevel(InventoryState.depth);
					}
				});
			}
		});

		uiY = Display.UI_HEIGHT;
		Tween.to(new Tween.Task(0, 0.5f, Tween.Type.BACK_OUT) {
			@Override
			public float getValue() {
				return uiY;
			}

			@Override
			public void setValue(float value) {
				uiY = value;
			}
		});
	}

	private static float uiY;
	private UiEntity go;

	@Override
	public void update(float dt) {
		super.update(dt);
		go.update(dt);
		ui.update(dt);
		Camera.instance.update(dt);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void renderUi() {
		renderPortal();
		Camera.ui.position.y += uiY;
		Camera.ui.update();
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);
		ui.render();
		go.render();
		Camera.ui.position.y -= uiY;
		Camera.ui.update();
		Graphics.batch.setProjectionMatrix(Camera.ui.combined);
		Ui.ui.renderCursor();
	}
}