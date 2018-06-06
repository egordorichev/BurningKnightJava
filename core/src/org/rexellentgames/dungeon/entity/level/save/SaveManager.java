package org.rexellentgames.dungeon.entity.level.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.File;
import java.io.IOException;

public class SaveManager {
	public enum Type {
		PLAYER,
		GAME,
		LEVEL
	}

	public static int slot = 0;

	public static String getDir() {
		return ".bk/" + slot + "/";
	}

	public static String getSavePath(Type type) {
		return getSavePath(type, false);
	}

	public static String getSavePath(Type type, boolean old) {
		switch (type) {
			case LEVEL: return getDir() + "level" + (old ? Dungeon.lastDepth : Dungeon.depth) + ".save";
			case PLAYER: return getDir() + "player.save";
			case GAME: default: return getDir() + "game.save";
		}
	}

	public static void save(Type type) {
		FileHandle save = Gdx.files.external(getSavePath(type, true));
		Log.info("Saving " + type + " " + Dungeon.lastDepth);

		try {
			FileWriter stream = new FileWriter(save.file().getAbsolutePath());

			switch (type) {
				case LEVEL: LevelSave.save(stream); break;
				case PLAYER: PlayerSave.save(stream); break;
				case GAME: GameSave.save(stream); break;
			}

			stream.close();
		} catch (Exception e) {
			Dungeon.reportException(e);
		}
	}

	public static void load(Type type) {
		FileHandle save = Gdx.files.external(getSavePath(type));

		if (!save.exists()) {
			File file = save.file();
			file.getParentFile().mkdirs();

			try {
				file.createNewFile();
			} catch (IOException e) {
				Dungeon.reportException(e);
			}

			generate(type);
			save(type);
		} else {
			Log.info("Loading " + type + " " + Dungeon.depth);

			try {
				FileReader stream = new FileReader(save.file().getAbsolutePath());

				switch (type) {
					case LEVEL: LevelSave.load(stream); break;
					case PLAYER: PlayerSave.load(stream); break;
					case GAME: GameSave.load(stream); break;
				}

				stream.close();
			} catch (Exception e) {
				Dungeon.reportException(e);
			}
		}
	}

	public static void generate(Type type) {
		Log.info("Generating " + type + " " + Dungeon.depth);
		Dungeon.lastDepth = Dungeon.depth;

		switch (type) {
			case LEVEL: LevelSave.generate(); break;
			case PLAYER: PlayerSave.generate(); break;
			case GAME: GameSave.generate(); break;
		}
	}
}