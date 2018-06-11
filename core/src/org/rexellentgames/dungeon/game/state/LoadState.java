package org.rexellentgames.dungeon.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.assets.MusicManager;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.mob.BurningKnight;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.entities.Exit;
import org.rexellentgames.dungeon.entity.level.save.LevelSave;
import org.rexellentgames.dungeon.entity.level.save.PlayerSave;
import org.rexellentgames.dungeon.entity.level.save.SaveManager;
import org.rexellentgames.dungeon.game.Ui;
import org.rexellentgames.dungeon.physics.World;
import org.rexellentgames.dungeon.ui.LevelBanner;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.PathFinder;
import org.rexellentgames.dungeon.util.Tween;

public class LoadState extends State {
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
			try {
				PlayerSave.all.clear();
				LevelSave.all.clear();

				SaveManager.load(SaveManager.Type.GAME);
				SaveManager.load(SaveManager.Type.LEVEL);
				SaveManager.load(SaveManager.Type.PLAYER);

				Dungeon.level.loadPassable();
				Dungeon.level.addPhysics();

				MusicManager.play(Dungeon.level.getMusic());
			} catch (RuntimeException e) {
				Log.report(e);
				Thread.currentThread().interrupt();

				return;
			}

			if (Player.instance == null) {
				Log.error("No player!");
				Dungeon.newGame();
				return;
			}

			PathFinder.setMapSize(Level.getWidth(), Level.getHeight());

			Log.info("Loading done!");

			LevelBanner banner = new LevelBanner();

			if (Dungeon.depth == 0) {
				banner.text = "The beginning";
			} else {
				banner.text = Dungeon.level.getName() + " " + Dungeon.level.getDepthAsCoolNum();
			}

			Dungeon.area.add(banner);
			
			if (BurningKnight.instance != null) {
				BurningKnight.instance.become("unactive");
			}

			ready = true;
		}).run();
	}

	@Override
	public void update(float dt) {
		if (this.ready && this.a == 0) {
			Dungeon.game.setState(new InGameState());
			Camera.instance.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
	}

	@Override
	public void renderUi() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

		Graphics.medium.setColor(1, 1, 1, this.a);
		Graphics.print(this.s, Graphics.medium, 120);
		Graphics.medium.setColor(1, 1, 1, 1);
	}
}