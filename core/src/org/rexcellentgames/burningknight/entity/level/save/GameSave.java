package org.rexcellentgames.burningknight.entity.level.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.ChangableRegistry;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

public class GameSave {
	public static void save(FileWriter writer, boolean old) {
		try {
			writer.writeByte(Player.instance == null ? Player.toSet.id : Player.instance.type.id);
			writer.writeByte((byte) (old ? Dungeon.depth : Dungeon.lastDepth));
			writer.writeString(Dungeon.level == null ? "The beginning" : Dungeon.level.formatDepth());

			ChangableRegistry.save(writer);

			for (int i = 0; i < Level.depths.length; i++) {
				writer.writeByte(Level.depths[i]);
				writer.writeBoolean(Level.boss[i]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class Info {
		public Player.Type type;
		public boolean free;
		public boolean error;
		public byte depth;

		public float firstW;
		public float secondW;
		public String first;
		public String second;
	}

	public static Info peek(int slot) {
		FileHandle save = Gdx.files.external(SaveManager.getSavePath(SaveManager.Type.GAME, slot));
		Info info = new Info();

		if (!save.exists()) {
			info.free = true;
			return info;
		}

		try {
			FileReader stream = new FileReader(save.file().getAbsolutePath());

			info.type = Player.Type.values()[stream.readByte()];
			info.depth = stream.readByte();
			info.second = stream.readString();

			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
			info.error = true;
		}

		return info;
	}

	public static void load(FileReader reader) throws IOException {
		Player.toSet = Player.Type.values()[reader.readByte()];
		byte d = reader.readByte();
		String name = reader.readString();

		if (Dungeon.notLoaded) {
			Dungeon.notLoaded = false;
			// Dungeon.depth = d;
		}

		ChangableRegistry.load(reader);

		for (int i = 0; i < Level.depths.length; i++) {
			Level.depths[i] = reader.readByte();
			Level.boss[i] = reader.readBoolean();
		}
	}

	public static void generate() {
		generateDepths();
		ChangableRegistry.generate();
	}

	private static final int areas = 5;

	private static void generateDepths() {
		int[] weights = new int[areas];
		boolean[] bosses = new boolean[areas * 4 + 1];

		for (int i = 0; i < areas; i++) {
			weights[i] = 4;
		}

		for (int i = 0; i < areas * 2; i++) {
			int f = Random.newInt(areas);

			if (weights[f] > 3) {
				for (int j = areas - 1; j >= 0; j--) {
					if (weights[j] < 5 && Random.chance(((float) areas - j) / areas * 100)) {
						weights[f] --;
						weights[j] ++;

						break;
					}
				}
			}
		}

		int depth = 0;

		for (int i = 0; i < areas; i++) {
			boolean boss = false;

			for (int j = 0; j < weights[i]; j++) {
				depth++;

				if (!boss && j > 0 && (Random.chance(45f) || j == weights[i] - 1)) {
					boss = true;
					bosses[depth] = true;

					System.out.print("B");
				} else {
					bosses[depth] = false;
					System.out.print("#");
				}
			}

			System.out.println();
		}

		for (int i = 0; i < areas; i++) {
			Level.depths[i] = (byte) weights[i];
		}

		System.arraycopy(bosses, 0, Level.boss, 0, areas * 4 + 1);
	}
}