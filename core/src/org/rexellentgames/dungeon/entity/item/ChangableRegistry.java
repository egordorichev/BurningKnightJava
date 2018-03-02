package org.rexellentgames.dungeon.entity.item;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.item.consumable.potion.HealingPotion;
import org.rexellentgames.dungeon.entity.item.consumable.potion.Potion;
import org.rexellentgames.dungeon.entity.item.consumable.potion.SunPotion;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;
import java.util.*;

public class ChangableRegistry {
	public static HashMap<String, Type> types = new HashMap<String, Type>();
	public static HashMap<Type, Boolean> identified = new HashMap<Type, Boolean>();

	public enum Type {
		RED(32),
		BLUE(33);

		private int sprite;

		Type(int sprite) {
			this.sprite = sprite;
		}

		public int getSprite() {
			return this.sprite;
		}
	}

	public static void load(FileReader reader) throws IOException {
		int size = reader.readInt32();

		for (int i = 0; i < size; i++) {
			try {
				String name = reader.readString();
				Type type = Type.valueOf(reader.readString());

				types.put(name, type);
				identified.put(type, reader.readBoolean());
			} catch (Exception e) {
				Dungeon.reportException(e);
			}
		}
	}

	public static void save(FileWriter writer) throws IOException {
		writer.writeInt32(types.size());

		for (Map.Entry<String, Type> entry : types.entrySet()) {
			writer.writeString(entry.getKey());
			writer.writeString(entry.getValue().toString());
			writer.writeBoolean(identified.get(entry.getValue()));
		}
	}

	public static void generate() {
		ArrayList<Class<? extends Potion>> potions = new ArrayList<Class<? extends Potion>>(Arrays.asList(
			HealingPotion.class, SunPotion.class
		));

		for (Type type : Type.values()) {
			int i = Random.newInt(potions.size());
			types.put(potions.get(i).getSimpleName(), type);
			identified.put(type, false);
			potions.remove(i);
		}
	}
}