package org.rexcellentgames.burningknight.entity.level.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.File;
import java.io.IOException;

public class SaveManager {
	public enum Type {
		PLAYER,
		GAME,
		LEVEL,
		GLOBAL
	}

	public static final String SAVE_DIR = ".bk/";
	public static int slot = 0;

	public static String getDir() {
		return getDir(slot);
	}

	public static String getDir(int slot) {
		return SAVE_DIR + "slot-" + slot + "/";
	}

	public static String getSavePath(Type type) {
		return getSavePath(type, false);
	}

	public static String getSavePath(Type type, boolean old) {
		switch (type) {
			case LEVEL: return getDir() + "level" + (old ? Dungeon.lastDepth : Dungeon.depth) + ".save";
			case PLAYER: return getDir() + "player.save";
			case GAME: default: return getDir() + "game.save";
			case GLOBAL: return SAVE_DIR + "progress.save";
		}
	}

	public static String getSavePath(Type type, int slot) {
		switch (type) {
			case LEVEL: return getDir(slot) + "level" + Dungeon.depth + ".save";
			case PLAYER: return getDir(slot) + "player.save";
			case GAME: default: return getDir(slot) + "game.save";
			case GLOBAL: return SAVE_DIR + "progress.save";
		}
	}

	public static void save(Type type, boolean old) {
		FileHandle save = Gdx.files.external(getSavePath(type, old));
		Log.info("Saving " + type + " " + Dungeon.lastDepth);

		try {
			FileWriter stream = new FileWriter(save.file().getAbsolutePath());

			switch (type) {
				case LEVEL: LevelSave.save(stream); break;
				case PLAYER: PlayerSave.save(stream); break;
				case GAME: GameSave.save(stream, old); break;
				case GLOBAL: GlobalSave.save(stream); break;
			}

			stream.close();
		} catch (Exception e) {
			Dungeon.reportException(e);
		}
	}

	public static void load(Type type) throws IOException {
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
			save(type, false);
		} else {
			Log.info("Loading " + type + " " + Dungeon.depth);

			FileReader stream = new FileReader(save.file().getAbsolutePath());

			switch (type) {
				case LEVEL: LevelSave.load(stream); break;
				case PLAYER: PlayerSave.load(stream); break;
				case GAME: GameSave.load(stream); break;
				case GLOBAL: GlobalSave.load(stream); break;
			}

			stream.close();
		}
	}

	public static void delete() {
		File file = Gdx.files.external(getDir()).file();

		if (file == null) {
			return;
		}

		File[] files = file.listFiles();

		if (files == null) {
			file.delete();
			return;
		}

		for (File f : files) {
			f.delete();
		}

		file.delete();
	}

	public static void generate(Type type) {
		Log.info("Generating " + type + " " + Dungeon.depth);
		Dungeon.lastDepth = Dungeon.depth;

		switch (type) {
			case LEVEL: LevelSave.generate(); break;
			case PLAYER: PlayerSave.generate(); break;
			case GAME: GameSave.generate(); break;
			case GLOBAL: GlobalSave.generate(); break;
		}
	}
}