package org.rexcellentgames.burningknight.game.state;

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
import org.rexcellentgames.burningknight.entity.level.entities.Entrance;
import org.rexcellentgames.burningknight.entity.level.entities.Exit;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Chest;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Mimic;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.entity.level.save.PlayerSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.ui.UiBanner;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.PathFinder;
import org.rexcellentgames.burningknight.util.Tween;

import java.io.IOException;

public class LoadState extends State {
	private boolean ready = false;
	private float a;
	private String s;

	@Override
	public void init() {
		this.s = "Doing secret stuff...";
		Dungeon.grayscale = 0;

		if (Ui.ui != null) {
			Ui.ui.healthbars.clear();
		}

		Dungeon.speed = 1f;
		Dungeon.darkR = Dungeon.MAX_R;
		Dungeon.ui.destroy();
		Dungeon.area.destroy();
		Exit.instance = null;

		switch (Dungeon.loadType) {
			case GO_UP: this.s = Locale.get("ascending"); break;
			case GO_DOWN: this.s = Locale.get("descending"); break;
			case LOADING: this.s = Locale.get("loading"); break;
		}

		final float t = 0.3f;

		Tween.to(new Tween.Task(1f, t) {
			@Override
			public float getValue() {
				return a;
			}

			@Override
			public void setValue(float value) {
				a = value;
			}

			@Override
			public boolean runWhenPaused() {
				return true;
			}
		});

		Player.ladder = null;
		Level.GENERATED = false;
		Shopkeeper.instance = null;

		World.init();

		Player.all.clear();
		Mob.all.clear();
		ItemHolder.all.clear();
		Chest.all.clear();
		Mimic.all.clear();

		PlayerSave.all.clear();
		LevelSave.all.clear();

		new Thread(new Runnable() {
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
					ItemHolder.all.clear();
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

				Tween.to(new Tween.Task(0f, t) {
					@Override
					public float getValue() {
						return a;
					}

					@Override
					public void setValue(float value) {
						a = value;
					}

					@Override
					public void onEnd() {
						ready = true;
					}

					@Override
					public boolean runWhenPaused() {
						return true;
					}
				});
			}
		}).run();
	}

	@Override
	public void update(float dt) {
		if (this.ready && (this.ready)) {
			Dungeon.game.setState(new InGameState());
		}
	}

	@Override
	public void renderUi() {
		Graphics.medium.setColor(1, 1, 1, this.a);
		Graphics.print(this.s, Graphics.medium, 120 - 16);
		Graphics.medium.setColor(1, 1, 1, 1);
	}
}