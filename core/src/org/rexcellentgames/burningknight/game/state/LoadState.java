package org.rexcellentgames.burningknight.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.creature.mob.boss.BurningKnight;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.Exit;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.entity.level.save.PlayerSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.PathFinder;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.ui.UiBanner;

import java.io.IOException;

public class LoadState extends State {
	public static boolean QUICK = !AssetLoadState.START_TO_MENU;

	private boolean ready = false;
	private float a;
	private String s;

	@Override
	public void init() {
		this.s = "Doing secret stuff...";

		if (Ui.ui != null) {
			Ui.ui.healthbars.clear();
		}

		Dungeon.speed = 1f;
		Dungeon.darkR = Dungeon.MAX_R;
		Dungeon.ui.destroy();
		Dungeon.area.destroy();
		Exit.instance = null;

		switch (Dungeon.loadType) {
			case GO_UP: this.s = "Ascending..."; break;
			case GO_DOWN: this.s = "Descending..."; break;
			case FALL_DOWN: this.s = "Falling..."; break;
			case RUNNING: this.s = "Running..."; break;
			case READING: this.s = "Writing the story...";
		}

		Tween.to(new Tween.Task(1f, 0.3f) {
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
				Tween.to(new Tween.Task(0f, 0.3f) {
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
				});
			}
		});

		Player.ladder = null;
		Level.GENERATED = false;

		World.init();

		Dungeon.area.add(new Camera());

		Player.all.clear();
		Mob.all.clear();

		new Thread(() -> {
			PlayerSave.all.clear();
			LevelSave.all.clear();

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
				LevelSave.all.clear();

				if (Dungeon.level != null) {
					Log.error("Removing old level");
					Dungeon.area.remove(Dungeon.level);
					Dungeon.level = null;
				}

				SaveManager.generate(SaveManager.Type.LEVEL);
				SaveManager.save(SaveManager.Type.LEVEL, false);
			} catch (RuntimeException e) {
				Log.report(e);
				Thread.currentThread().interrupt();
				return;
			}

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

			if (Player.instance == null) {
				Log.error("No player!");
				Dungeon.newGame();
				return;
			}

			PathFinder.setMapSize(Level.getWidth(), Level.getHeight());

			Log.info("Loading done!");

			UiBanner banner = new UiBanner();
			banner.text = Dungeon.level.formatDepth();
			banner.extra = "test";
			Dungeon.area.add(banner);
			
			if (BurningKnight.instance != null) {
				BurningKnight.instance.become("unactive");
			}

			ready = true;
		}).run();
	}

	@Override
	public void update(float dt) {
		if (this.ready && (QUICK || this.a == 0)) {
			Dungeon.game.setState(new InGameState());
			Camera.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
	}

	@Override
	public void renderUi() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

		Graphics.medium.setColor(1, 1, 1, this.a);
		Graphics.print(this.s, Graphics.medium, 120);
		Graphics.medium.setColor(1, 1, 1, 1);
	}
}