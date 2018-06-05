package org.rexellentgames.dungeon.entity.level.save;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

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

			for (int i = 0; i < all.size(); i++) {
				SaveableEntity entity = all.get(i);

				writer.writeString(entity.getClass().getName());
				entity.save(writer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void load(FileReader reader) {
		try {
			Dungeon.level = Level.forDepth(Dungeon.depth);
			Dungeon.area.add(Dungeon.level);
			Dungeon.level.load(reader);

			int count = reader.readInt32();

			for (int i = 0; i < count; i++) {
				String t = reader.readString();
				Class<?> clazz = Class.forName(t);
				SaveableEntity entity = (SaveableEntity) clazz.newInstance();

				Dungeon.area.add(entity);
				all.add(entity);

				entity.load(reader);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void generate() {
		Dungeon.level = Level.forDepth(Dungeon.depth);
		Dungeon.area.add(Dungeon.level);
		Dungeon.level.generate();
	}
}