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
		LEVEL
	}

	public static int slot = 0;

	public static String getDir() {
		return ".bk/" + slot + "/";
	}

	public static String getDir(int slot) {
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

	public static String getSavePath(Type type, int slot) {
		switch (type) {
			case LEVEL: return getDir(slot) + "level" + Dungeon.depth + ".save";
			case PLAYER: return getDir(slot) + "player.save";
			case GAME: default: return getDir(slot) + "game.save";
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
				case GAME: GameSave.save(stream); break;
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
			}

			stream.close();
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