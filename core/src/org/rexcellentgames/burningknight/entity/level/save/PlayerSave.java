package org.rexcellentgames.burningknight.entity.level.save;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerSave {
	public static ArrayList<SaveableEntity> all = new ArrayList<>();

	public static void remove(SaveableEntity entity) {
		all.remove(entity);
	}

	public static void add(SaveableEntity entity) {
		all.add(entity);
	}

	public static void save(FileWriter writer) {
		try {
			writer.writeInt32(all.size());

			for (SaveableEntity entity : all) {
				writer.writeString(entity.getClass().getName());
				entity.save(writer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void load(FileReader reader) throws IOException {
		try {
			all.clear();
			int count = reader.readInt32();

			for (int i = 0; i < count; i++) {
				String t = reader.readString();
				Class<?> clazz = Class.forName(t);
				SaveableEntity entity = (SaveableEntity) clazz.newInstance();

				Dungeon.area.add(entity);

				all.add(entity);

				entity.load(reader);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void generate() {
		Player player = new Player();
		Dungeon.area.add(player);

		all.add(player);
		player.generate();
	}
}