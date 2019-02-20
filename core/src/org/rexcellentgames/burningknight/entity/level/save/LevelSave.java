package org.rexcellentgames.burningknight.entity.level.save;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.game.state.LoadState;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;
import java.util.ArrayList;

public class LevelSave {
	public static ArrayList<SaveableEntity> all = new ArrayList<>();

	public static void remove(SaveableEntity entity) {
		all.remove(entity);
	}

	public static void add(SaveableEntity entity) {
		all.add(entity);
	}

	public static void save(FileWriter writer) {
		try {
			Dungeon.level.save(writer);

			writer.writeInt32(all.size());

			for (SaveableEntity entity : all) {
				writer.writeString(entity.getClass().getName().replace("org.rexcellentgames.burningknight.", ""));
				entity.save(writer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void load(FileReader reader) throws IOException {
		all.clear();
		Dungeon.level = Level.forDepth(Dungeon.depth);
		Dungeon.area.add(Dungeon.level);
		Dungeon.level.load(reader);

		int count = reader.readInt32();

		for (int i = 0; i < count; i++) {
			String t = reader.readString();
			Class<?> clazz = null;

			try {
				clazz = Class.forName("org.rexcellentgames.burningknight." + t);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			SaveableEntity entity;

			try {
				entity = (SaveableEntity) clazz.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
				continue;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				continue;
			}

			Dungeon.area.add(entity);
			all.add(entity);

			entity.load(reader);
		}
	}

	private static int i;

	public static void generate() {
		try {
			LoadState.generating = true;

			Dungeon.level = Level.forDepth(Dungeon.depth);
			Dungeon.area.add(Dungeon.level);
			Dungeon.level.generate(i);

			i = 0;
		} catch (RuntimeException e) {
			e.printStackTrace();
			generate();
			i++;
		}
	}
}