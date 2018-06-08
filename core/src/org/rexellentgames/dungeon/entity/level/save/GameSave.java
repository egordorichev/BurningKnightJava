package org.rexellentgames.dungeon.entity.level.save;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.item.ChangableRegistry;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;

public class GameSave {
	public static void save(FileWriter writer) {
		try {
			writer.writeByte((byte) Dungeon.lastDepth);
			Log.info("Save dungeon depth to " + Dungeon.lastDepth);
			ChangableRegistry.save(writer);

			for (int i = 0; i < Level.depths.length; i++) {
				writer.writeByte(Level.depths[i]);
				writer.writeBoolean(Level.boss[i]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void load(FileReader reader) {
		try {
			byte d = reader.readByte();

			if (Dungeon.notLoaded) {
				Dungeon.notLoaded = false;
				Dungeon.depth = d;
			}

			Log.info("Set dungeon depth to " + Dungeon.depth);
			ChangableRegistry.load(reader);

			for (int i = 0; i < Level.depths.length; i++) {
				Level.depths[i] = reader.readByte();
				Level.boss[i] = reader.readBoolean();
			}
		} catch (IOException e) {
			e.printStackTrace();
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
			weights[i] = 3;
		}

		for (int i = 0; i < areas * 2; i++) {
			int f = Random.newInt(areas);

			if (weights[f] > 2) {
				for (int j = areas - 1; j >= 0; j--) {
					if (weights[j] < 4 && Random.chance(((float) areas - j) / areas * 100)) {
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

				if (!boss && j != 0 && (Random.chance(45f) || j == weights[i] - 1)) {
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

		for (int i = 0; i < areas * 4 + 1; i++) {
			Level.boss[i] = bosses[i];
		}
	}
}