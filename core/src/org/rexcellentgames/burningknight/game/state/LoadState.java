package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.graphics.Color;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.npc.Shopkeeper;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.AnswerButton;
import org.rexcellentgames.burningknight.entity.level.entities.Exit;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Chest;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Mimic;
import org.rexcellentgames.burningknight.entity.level.rooms.HandmadeRoom;
import org.rexcellentgames.burningknight.entity.level.save.GameSave;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.entity.level.save.PlayerSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.ui.UiBanner;
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.PathFinder;

public class LoadState extends State {
	private boolean ready = false;
	private String s;
	public static boolean fromSelect;

	@Override
	public void init() {
		Audio.stop();

		Dungeon.darkR = Dungeon.MAX_R;
		Dungeon.dark = 1;

		this.s = "Generating...";
		Dungeon.grayscale = 0;

		if (Ui.ui != null) {
			Ui.ui.healthbars.clear();
		}

		Dungeon.setBackground(new Color(0, 0, 0, 1));
	}

	private boolean second;

	@Override
	public void update(float dt) {
		if (this.ready) {
			// Dungeon.darkR = 0;
			Dungeon.game.setState(GameSave.inventory ? new InventoryState() : new InGameState());
			return;
		}

		if (runM) {
			runM = false;
			runMain();
		}
	}

	private boolean third;

	private void showError(String err) {
		Log.error(err);
		error = true;
		errorString = err;
		Graphics.layout.setText(Graphics.medium, errorString);
		ew = Graphics.layout.width / 2;

		Dungeon.ui.add(new UiButton("start_new_game", Display.UI_WIDTH / 2, Display.UI_HEIGHT / 3) {
			@Override
			public void onClick() {
				Dungeon.newGame();
			}
		});
	}

	private void runThread() {
		HandmadeRoom.init();

		Dungeon.speed = 1f;
		Dungeon.ui.destroy();
		Dungeon.area.destroy();
		Exit.instance = null;

		Player.ladder = null;
		Level.GENERATED = false;
		Shopkeeper.instance = null;

		World.init();

		Player.all.clear();
		Mob.all.clear();
		ItemHolder.getAll().clear();

		Chest.all.clear();
		Mimic.all.clear();
		AnswerButton.all.clear();

		PlayerSave.all.clear();
		LevelSave.all.clear();

		if (fromSelect) {
			fromSelect = false;

			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					PlayerSave.generate();
					Dungeon.area.remove(Player.instance);

					SaveManager.save(SaveManager.Type.PLAYER, false);
					Dungeon.lastDepth = Dungeon.depth;
					Dungeon.depth = ItemSelectState.depth;

					runM = true;
				}
			});

			thread.setPriority(1);
			thread.run();
		} else {
			runMain();
		}
	}

	private boolean runM;

	private void runMain() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Level lvl = Level.forDepth(Dungeon.depth - 1);

				boolean error;

				try {
					error = !SaveManager.load(SaveManager.Type.GAME);
				} catch (Exception e) {
					Log.report(e);
					error = true;
				}

				if (error) {
					showError("Failed to load game!");
					return;
				}

				try {
					error = !SaveManager.load(SaveManager.Type.LEVEL);
				} catch (Exception e) {
					Log.report(e);
					error = true;
				}

				if (error) {
					showError("Failed to load level!");
					return;
				}

				Dungeon.area.add(new Camera());

				try {
					error = !SaveManager.load(SaveManager.Type.PLAYER);
				} catch (Exception e) {
					Log.report(e);
					error = true;
				}

				if (error) {
					showError("Failed to load player!");
					return;
				}

				Dungeon.level.loadPassable();
				Dungeon.level.addPhysics();

				Audio.play(Dungeon.level.getMusic());

				if (!Dungeon.level.same(lvl)) {
					Audio.reset();
				}

				if (Player.instance == null) {
					showError("Failed to load player!");
					Dungeon.newGame();
					return;
				}

				PathFinder.setMapSize(Level.getWidth(), Level.getHeight());

				Log.info("Loading done!");

				UiBanner banner = new UiBanner();
				banner.text = Dungeon.level.formatDepth();
				Dungeon.ui.add(banner);

				Dungeon.buildDiscordBadge();
				third = true;
			}
		});

		thread.setPriority(1);
		thread.run();
	}

	private boolean error;
	private String errorString;
	private float ew;

	@Override
	public void renderUi() {
		renderPortal();

		if (!second) {
			this.second = true;
			runThread();
		}

		if (this.third) {
			this.ready = true;
		}

		if (error) {
			Graphics.print(this.errorString, Graphics.medium, Display.UI_WIDTH / 2 - ew, (Display.UI_HEIGHT - 16) / 2 - 8);
			Dungeon.ui.render();
		} else {
			// Graphics.print(this.s, Graphics.medium, (Display.UI_HEIGHT - 16) / 2 - 8);
		}

		Ui.ui.renderCursor();
	}
}