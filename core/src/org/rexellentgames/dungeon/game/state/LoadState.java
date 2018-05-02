package org.rexellentgames.dungeon.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.UiLog;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.mob.BurningKnight;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Lamp;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.entities.Exit;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.CastleEntranceRoom;
import org.rexellentgames.dungeon.game.Game;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.net.Packets;
import org.rexellentgames.dungeon.physics.World;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.PathFinder;
import org.rexellentgames.dungeon.util.Tween;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LoadState extends State {
	private boolean ready = false;
	private float a;
	private String s;

	public static void writeDepth() {
		FileHandle save = Gdx.files.external(".burningknight/depth.save");

		try {
			FileWriter writer = new FileWriter(save.file().getPath());
			writer.writeInt32(Dungeon.depth);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init() {
		this.s = "Doing secret stuff...";

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

		if (BurningKnight.instance != null) {
			BurningKnight.instance.onRageEnd();
		}

		Player.REGISTERED = false;
		Level.GENERATED = false;

		World.init();

		Dungeon.area.add(new Camera());
		Dungeon.ui.add(new UiLog());

		Player.all.clear();
		Mob.all.clear();

		if (Player.instance == null) {
			this.readDepth();
		}

		Dungeon.level = Level.forDepth(Dungeon.depth);

		Log.info("Dungeon depth " + Dungeon.depth + ", level is instance of " + Dungeon.level.getClass().getSimpleName());

		Dungeon.area.add(Dungeon.level);

		if (Network.SERVER || Network.NONE) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Dungeon.level.load(Level.DataType.PLAYER);
						Dungeon.level.load(Level.DataType.LEVEL);

						Dungeon.level.loadPassable();
						Dungeon.level.addPhysics();
					} catch (RuntimeException e) {
						Log.report(e);

						// Dungeon.game.setState(new LoadState());
						Thread.currentThread().interrupt();

						return;
					}

					Dungeon.level.loadDropped();

					if (Player.instance == null) {
						Log.error("No player!");
						Dungeon.newGame();
						return;
					}

					PathFinder.setMapSize(Level.getWidth(), Level.getHeight());

					UiLog.instance.print("[orange]Welcome to level " + (Dungeon.depth + 1) + "!");
					Log.info("Loading done!");

					if (Network.SERVER) {
						Network.server.getServerHandler().sendToAll(Packets.makeLevel(Dungeon.level.getData(),
							Dungeon.level.getVariants(), Dungeon.depth, Level.getWidth(), Level.getHeight()));
					}

					Camera.instance.follow(Player.instance);
					Player.instance.tryToFall();

					if (BurningKnight.instance != null) {
						if (BurningKnight.instance.isInRage()) {
							BurningKnight.instance.onRageStart();
						} else {
							BurningKnight.instance.checkForRage();
						}

						BurningKnight.instance.become("unactive");
					}

					if (Lamp.instance != null) {
						Lamp.instance.val = 100f;
					}

					ready = true;
				}
			}).run();
		}
	}

	private void readDepth() {
		FileHandle save = Gdx.files.external(".burningknight/depth.save");

		if (!save.exists()) {
			File file = save.file();
			file.getParentFile().mkdirs();

			try {
				file.createNewFile();
				return;
			} catch (IOException e) {
				Dungeon.reportException(e);
			}

			Dungeon.depth = -1;
			return;
		}

		try {
			FileReader reader = new FileReader(save.file().getPath());
			Dungeon.depth = reader.readInt32();
			reader.close();
			return;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Dungeon.depth = -1;
	}

	@Override
	public void update(float dt) {
		if (this.ready && this.a == 0) {
			boolean c = false; // Level.GENERATED && Dungeon.depth == 0;

			Game.instance.setState(c ? new ComicsState() : new InGameState());
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

		// Dungeon.level.render();
	}
}