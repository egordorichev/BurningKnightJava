package org.rexcellentgames.burningknight.entity.level.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Version;
import org.rexcellentgames.burningknight.entity.level.entities.Entrance;
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

	public static final String SAVE_DIR = "burningknight/";
	public static int slot = 0;
	public static final byte version = 2;

	/*
	 * Version change log:
	 * 1: debug test
	 * 2: implemented level saving with optimization
	 */

	static {
		Log.info("Save directory is " + SAVE_DIR);
	}

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
			case LEVEL: return ((old ? Dungeon.lastDepth : Dungeon.depth) <= -1 ? SAVE_DIR : getDir()) + "level" + (old ? Dungeon.lastDepth : Dungeon.depth) + ".sv";
			case PLAYER: return getDir() + "player.sv";
			case GAME: default: return getDir() + "game.sv";
			case GLOBAL: return SAVE_DIR + "global.sv";
		}
	}

	public static String getSavePath(Type type, int slot) {
		switch (type) {
			case LEVEL: return (Dungeon.depth <= -1 ? SAVE_DIR : getDir(slot)) + "level" + Dungeon.depth + ".sv";
			case PLAYER: return getDir(slot) + "player.sv";
			case GAME: default: return getDir(slot) + "game.sv";
			case GLOBAL: return SAVE_DIR + "global.sv";
		}
	}

	public static FileHandle getFileHandle(String path) {
		if (Version.debug) {
			return Gdx.files.external(path);
		} else {
			return Gdx.files.local(path);
		}
	}

	public static void save(Type type, boolean old) {
		FileHandle save = getFileHandle(getSavePath(type, old));
		Log.info("Saving " + type + " " + (old ? Dungeon.lastDepth : Dungeon.depth));

		try {
			FileWriter stream = new FileWriter(save.file().getAbsolutePath());
			stream.writeByte(version);

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
		FileHandle save = getFileHandle(getSavePath(type));

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

			byte v = stream.readByte();

			if (v > version) {
				Log.error("Unknown save version!");
				stream.close();
				generate(type);
				return;
			} else if (v < version) {
				Log.info("Older save version!");
			}

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
		Log.info("Deleting saves!");

		LevelSave.all.clear();
		PlayerSave.all.clear();

		File file = getFileHandle(getDir()).file();

		if (file == null) {
			Log.error("Failed to delete!");
			return;
		}

		File[] files = file.listFiles();

		if (files == null) {
			file.delete();
			Log.error("Failed to detect inner files to delete!");
			return;
		}

		for (File f : files) {
			f.delete();
		}

		file.delete();
	}

	public static void generate(Type type) {
		Dungeon.loadType = Entrance.LoadType.GO_DOWN;

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