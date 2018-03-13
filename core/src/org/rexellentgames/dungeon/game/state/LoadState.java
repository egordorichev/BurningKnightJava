package org.rexellentgames.dungeon.game.state;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.UiLog;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.*;
import org.rexellentgames.dungeon.entity.level.entities.Entrance;
import org.rexellentgames.dungeon.entity.level.entities.Exit;
import org.rexellentgames.dungeon.entity.level.levels.HallLevel;
import org.rexellentgames.dungeon.entity.level.levels.HellLevel;
import org.rexellentgames.dungeon.entity.level.levels.LibraryLevel;
import org.rexellentgames.dungeon.entity.level.levels.PrisonLevel;
import org.rexellentgames.dungeon.entity.level.levels.SkyLevel;
import org.rexellentgames.dungeon.entity.level.levels.StorageLevel;
import org.rexellentgames.dungeon.game.Area;
import org.rexellentgames.dungeon.game.Game;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.PathFinder;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class LoadState extends State {
	private boolean ready = false;

	@Override
	public void init() {
		if (Dungeon.area != null) {
			Dungeon.area.destroy();
		}

		if (Dungeon.ui != null) {
			Dungeon.ui.destroy();
		}

		Entrance.ID = -1;
		Exit.ID = -1;
		Player.REGISTERED = false;
		Level.GENERATED = false;

		Dungeon.world = new World(new Vector2(0, 0), true);

		Dungeon.area.add(new Camera());
		Dungeon.ui.add(new UiLog());

		if (Player.instance == null) {
			this.readDepth();
		}

		if (!Network.SERVER) {
			if (Dungeon.light == null) {
				Dungeon.light = new RayHandler(Dungeon.world);
				Dungeon.light.setBlurNum(10);
			}

			Dungeon.light.setAmbientLight(Dungeon.depth > 0 ? 0f : 0.7f);
		}

		switch (Dungeon.depth) {
			case -1: Dungeon.level = new SkyLevel(); break;
			case 0: case 1: case 2: case 3: default: Dungeon.level = new HallLevel(); break;
			// todo: case 4: boss level
			case 5: case 6: case 7: case 8: Dungeon.level = new StorageLevel(); break;
			// todo: case 9: boss level
			case 10: case 11: case 12: case 13: Dungeon.level = new PrisonLevel(); break;
			// todo: case 14: boss level
			case 15: case 16: case 17: case 18: Dungeon.level = new LibraryLevel(); break;
			// todo: case 19: boss level
			case 20: case 21: case 22: case 23: Dungeon.level = new HellLevel(); break;
			// todo: case 24: THE FINAL BOSS LEVEL
		}

		Log.info("Dungeon depth " + Dungeon.depth + ", level is instance of " + Dungeon.level.getClass().getSimpleName());

		Dungeon.area.add(Dungeon.level);

		if (Network.SERVER || Network.NONE) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					Dungeon.level.load(Level.DataType.PLAYER);
					Dungeon.level.load(Level.DataType.LEVEL);

					Dungeon.level.loadPassable();
					Dungeon.level.addPhysics();
					Dungeon.level.tile();

					PathFinder.setMapSize(Level.getWIDTH(), Level.getHEIGHT());

					UiLog.instance.print("[orange]Welcome to level " + (Dungeon.depth + 1) + "!");
					Log.info("Loading done, last exit id = " + (Exit.ID) + ", last entrance id = " + (Entrance.ID));

					ready = true;
				}
			}).run();
		}
	}

	public static void writeDepth() {
		FileHandle save = Gdx.files.external(".ldg/depth.save");

		try {
			FileWriter writer = new FileWriter(save.file().getPath());
			writer.writeInt32(Dungeon.depth);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void readDepth() {
		FileHandle save = Gdx.files.external(".ldg/depth.save");

		if (!save.exists()) {
			File file = save.file();
			file.getParentFile().mkdirs();

			try {
				file.createNewFile();
				return;
			} catch (IOException e) {
				Dungeon.reportException(e);
			}

			Dungeon.depth = 0;
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

		Dungeon.depth = 0;
	}

	@Override
	public void update(float dt) {
		if (this.ready) {
			Game.instance.setState(new InGameState());
			Camera.instance.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
	}

	@Override
	public void render() {
		Graphics.medium.draw(Graphics.batch, "Loading...", 10, 10);
	}
}