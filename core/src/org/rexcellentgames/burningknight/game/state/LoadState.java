package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Version;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.inventory.UiInventory;
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
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.entity.level.save.PlayerSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.ui.UiBanner;
import org.rexcellentgames.burningknight.ui.UiButton;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.MathUtils;
import org.rexcellentgames.burningknight.util.PathFinder;

import java.util.ArrayList;
import java.util.Arrays;

public class LoadState extends State {
	private boolean ready = false;
	public static boolean fromSelect;

	private static ArrayList<String> jokes = new ArrayList<>(Arrays.asList(
		"Please stand by",
		"Press X",
		"Generating trouble",
		"Looking cool",
		"Get em!",
		"Terraforming mars",
		"Dodge this!",
		"I think that knight wanted to say something",
		"...",
		"Are we there yet?",
		"Generating secrets",
		"Hiding secrets",
		"I'm hungry",
		"/!\\ /!\\ /!\\",
		"Saving is important",
		"Cooling down",
		"Heating up",
		"Adding some drama",
		"Melee weapons can reflect bullets",
		"You better get digging",
		"QWERTY",
		"F2",
		"Generating generators",
		"Waking up",
		"Deleting the saves",
		"Preparing to start",
		"Looking for an excuse",
		"asdasdasd",
		"Automatically synchronizing cardinal grammeters",
		"Reducing sinusoidal repleneration",
		"Fromaging the bituminous spandrels",
		"Join our discord!",
		"Reticulating splines",
		"Calculating Math.PI",
		"Inventing the wheel",
		"Adding some oil",
		"Recruiting robot hamsters",
		"Generating buttons",
		"Installing deinstallers",
		"Thinking",
		"I like pizza",
		"That fight tho",
		"Be careful",
		"Almost RIP",
		"@egordorichev",
		"Dog food",
		"Always wear dry socks",
		"Its your lucky day",
		"Press F to pay respect",
		"Let's do this",
		"Let's go",
		"YOOOOOO",
		"Go go go",
		"Делаем вид что это что-то значит",
		"Loading terrain",
		"Building terrain",
		"Googling",
		"Help me",
		"SOS",
		"Are we lost?",
		"Spooooky",
		"OOO",
		":O",
		"On fire",
		"It's magic time",
		"That joke tho",
		"Settings things on fire",
		"Preparing to explode",
		"Installing linux",
		"Erasing data",
		"Generating a joke"
	));

	@Override
	public void init() {
		joke = jokes.get(new java.util.Random().nextInt(jokes.size()));

		UiInventory.justUsed = 0;
		generating = false;
		Dungeon.darkR = Dungeon.MAX_R;
		Dungeon.dark = 1;

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
			Dungeon.game.setState(new InGameState());
			return;
		}

		if (Input.instance.wasPressed("F")) {
			InGameState.horn();
		}

		if (Input.instance.wasPressed("pause")) {
			InGameState.triggerPause = true;
		}

		progress += Version.debug ? dt * 1f : dt * 0.3f;

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

				InGameState.toPlay = Dungeon.level.getMusic();
				InGameState.resetMusic = Dungeon.depth == 1 || !Dungeon.level.same(lvl);

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
	private float progress;
	public static boolean generating = false;
	private String joke;

	@Override
	public void renderUi() {
		renderPortal();

		if (!second) {
			al += Gdx.graphics.getDeltaTime() * 3;

			if (al >= 1f) {
				al = 1;

				this.second = true;
				runThread();
			}
		}

		if ((!generating || progress >= 1.0f) && this.third) {
			al -= Gdx.graphics.getDeltaTime() * 3;

			if (al <= 0) {
				al = 0;

				this.ready = true;
			}
		}

		if (error) {
			Graphics.print(this.errorString, Graphics.medium, Display.UI_WIDTH / 2 - ew, (Display.UI_HEIGHT - 16) / 2 - 8);
			Dungeon.ui.render();
		} else if (generating) {
			int i = (int) MathUtils.clamp(0, 100, Math.round(progress * 100));
			Graphics.medium.setColor(1, 1, 1, al);
			Graphics.print("Generating... " + i + "%",
				Graphics.medium, (Display.UI_HEIGHT - 16) / 2 - 8);
			Graphics.medium.setColor(1, 1, 1, 1);
			Graphics.small.setColor(1, 1, 1, al);
			Graphics.print(joke,
				Graphics.small, (Display.UI_HEIGHT - 16) / 2 - 16);
			Graphics.small.setColor(1, 1, 1, 1);
		}

		Ui.ui.renderCursor();
	}

	private float al;
}