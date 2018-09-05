package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.mob.boss.BurningKnight;
import org.rexcellentgames.burningknight.entity.creature.npc.Shopkeeper;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.AnswerButton;
import org.rexcellentgames.burningknight.entity.level.entities.Entrance;
import org.rexcellentgames.burningknight.entity.level.entities.Exit;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Chest;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Mimic;
import org.rexcellentgames.burningknight.entity.level.rooms.HandmadeRoom;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.entity.level.save.PlayerSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.ui.UiBanner;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.PathFinder;

import java.io.IOException;

public class LoadState extends State {
	private boolean ready = false;
	private float alp;
	private String s;

	@Override
	public void init() {
		this.s = "Doing secret stuff...";
		Dungeon.grayscale = 0;

		switch (Dungeon.loadType) {
			case GO_UP: this.s = Locale.get("ascending"); break;
			case GO_DOWN: this.s = Locale.get("descending"); break;
			case LOADING: this.s = Locale.get("loading"); break;
		}

		if (Ui.ui != null) {
			Ui.ui.healthbars.clear();
		}

		Dungeon.setBackground(new Color(0, 0, 0, 1));

		Dungeon.darkR = Dungeon.MAX_R;
		Dungeon.dark = 1;
	}

	private boolean second;

	@Override
	public void update(float dt) {
		if (this.ready) {
			Dungeon.darkR = 0;
			Dungeon.game.setState(new InGameState());
		}
	}

	private boolean third;

	private void runThread() {

		HandmadeRoom.init();

		Dungeon.speed = 1f;
		Dungeon.dark = 1;
		Dungeon.darkR = Dungeon.MAX_R;
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

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				Level lvl = Level.forDepth(Dungeon.depth - 1);

				try {
					SaveManager.load(SaveManager.Type.GAME);
				} catch (IOException e) {
					Log.error("Failed to load game!");
					Dungeon.newGame();
					return;
				} catch (RuntimeException e) {
					Log.report(e);
					Thread.currentThread().interrupt();
					return;
				}

				try {
					SaveManager.load(SaveManager.Type.LEVEL);
				} catch (IOException e) {
					Log.error("Failed to load level, generating new...");

					if (Dungeon.level != null) {
						Log.error("Removing old level");
						Dungeon.area.remove(Dungeon.level);
						Dungeon.level = null;
					}

					Player.all.clear();
					Mob.all.clear();
					ItemHolder.getAll().clear();
					Chest.all.clear();
					Mimic.all.clear();
					Player.ladder = null;
					Level.GENERATED = false;
					Shopkeeper.instance = null;

					Dungeon.loadType = Entrance.LoadType.GO_DOWN;

					SaveManager.generate(SaveManager.Type.LEVEL);
					SaveManager.save(SaveManager.Type.LEVEL, false);
				} catch (RuntimeException e) {
					Log.report(e);
					Thread.currentThread().interrupt();
					return;
				}

				Dungeon.area.add(new Camera());

				try {
					SaveManager.load(SaveManager.Type.PLAYER);
				} catch (IOException e) {
					Log.error("Failed to load player!");
					Dungeon.newGame();
					return;
				} catch (RuntimeException e) {
					Log.report(e);
					Thread.currentThread().interrupt();
					return;
				}

				Dungeon.level.loadPassable();
				Dungeon.level.addPhysics();

				Audio.play(Dungeon.level.getMusic());

				if (!Dungeon.level.same(lvl)) {
					Audio.reset();
				}

				if (Player.instance == null) {
					Log.error("No player!");
					Dungeon.newGame();
					return;
				}

				PathFinder.setMapSize(Level.getWidth(), Level.getHeight());

				Log.info("Loading done!");

				UiBanner banner = new UiBanner();
				banner.text = Dungeon.level.formatDepth();
				Dungeon.ui.add(banner);

				if (BurningKnight.instance != null) {
					BurningKnight.instance.become("unactive");
				}

				Dungeon.buildDiscordBadge();
				third = true;
			}
		});

		thread.setPriority(3);
		thread.run();
	}

	@Override
	public void renderUi() {
		this.alp += ((this.third ? 0 : 1) - this.alp) * Gdx.graphics.getDeltaTime() * 5;

		if (this.alp >= 0.95f && !second) {
			this.second = true;
			runThread();
		}

		if (this.alp <= 0.05f && this.third) {
			this.ready = true;
		}

		Graphics.startShape();
		Graphics.shape.setColor(0, 0, 0, 1);
		Graphics.shape.rect(0, 0, Display.GAME_WIDTH, Display.GAME_HEIGHT);
		Graphics.endShape();

		Graphics.medium.setColor(1, 1, 1, this.alp);
		Graphics.print(this.s, Graphics.medium, (Display.GAME_HEIGHT - 16) / 2 - 8);
		Graphics.medium.setColor(1, 1, 1, 1);
	}
}