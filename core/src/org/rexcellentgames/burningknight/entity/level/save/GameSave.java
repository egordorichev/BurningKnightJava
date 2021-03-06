package org.rexcellentgames.burningknight.entity.level.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.mob.boss.BurningKnight;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

public class GameSave {
	public static boolean defeatedBK;
	public static int killCount;
	public static float time;
	public static boolean inventory;
	public static boolean playedAlpha;
	public static int runId;

	public static void save(FileWriter writer, boolean old) {
		try {
			writer.writeByte((byte) (old ? Dungeon.lastDepth : Dungeon.depth));
			writer.writeByte((byte) (Player.instance == null ? Player.getTypeId(Player.toSet): Player.getTypeId(Player.instance.type)));

			writer.writeBoolean(defeatedBK);
			writer.writeInt32(killCount);
			writer.writeFloat(time);
			writer.writeBoolean(inventory);
			writer.writeBoolean(playedAlpha);
			writer.writeInt32(runId);

			writer.writeString(Random.getSeed());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class Info {
		public Player.Type type;
		public boolean free;
		public boolean error;
		public byte depth;

		public String second;
	}

	public static Info peek(int slot) {
		FileHandle save = Gdx.files.external(SaveManager.getSavePath(SaveManager.Type.GAME, slot));
		Info info = new Info();

		if (!save.exists()) {
			info.free = true;
			return info;
		}

		FileHandle sv = Gdx.files.external(SaveManager.getSavePath(SaveManager.Type.PLAYER, slot));

		if (!sv.exists()) {
			info.free = true;
			return info;
		}

		try {
			FileReader stream = new FileReader(save.file().getAbsolutePath());

			byte version = stream.readByte();

			info.depth = stream.readByte();
			info.type = Player.Type.values()[stream.readByte()];

			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
			info.error = true;
		}

		return info;
	}

	public static void load(FileReader reader) throws IOException {
		byte d = reader.readByte();
		Player.toSet = Player.Type.values()[reader.readByte()];

		defeatedBK = reader.readBoolean();
		killCount = reader.readInt32();
		time = reader.readFloat();
		inventory = reader.readBoolean();
		playedAlpha = reader.readBoolean();
		runId = reader.readInt32();

		Random.setSeed(reader.readString());
	}

	public static void generate() {
		BurningKnight.instance = null;
		Player.instance = null;

		killCount = 0;
		time = 0;
		defeatedBK = false;
		inventory = false;
		playedAlpha = false;
	}
}