package org.rexcellentgames.burningknight.entity.item;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.item.consumable.potion.*;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ChangableRegistry {
	public static HashMap<String, Type> types = new HashMap<>();
	public static HashMap<Type, Boolean> identified = new HashMap<>();

	public enum Type {
		RED("item-potion_a"),
		BLUE("item-potion_b"),
		ORANGE("item-potion_c"),
		GREEN("item-potion_d"),
		YELLOW("item-potion_e"),
		CORAL("item-potion_f"),
		PINK("item-potion_g"),
		BROWN("item-potion_h"),

		JERA("item-scroll_a"),
		THURISAZ("item-scroll_b"),
		FEHU("item-scroll_c"),
		RAIDO("item-scroll_d"),
		MANNAZ("item-scroll_e"),
		/*TEIWAZ(69),
		SOWULO(70),
		HAGALAZ(71)*/

		;

		private String sprite;

		Type(String sprite) {
			this.sprite = sprite;
		}

		public String getSprite() {
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
		ArrayList<Class<? extends Potion>> potions = new ArrayList<>(Arrays.asList(
			HealingPotion.class, FirePotion.class, InvisibilityPotion.class,
			RegenerationPotion.class, PoisonPotion.class
		));

		ArrayList<Type> potionTypes = new ArrayList<>(Arrays.asList(
			Type.RED, Type.BLUE, Type.ORANGE, Type.GREEN, Type.YELLOW//, Type.CORAL, Type.PINK, Type.BROWN
		));

		for (Type type : potionTypes) {
			int i = Random.newInt(potions.size());
			types.put(potions.get(i).getSimpleName().replace("org.rexcellentgames.burningknight.", ""), type);
			identified.put(type, false);
			potions.remove(i);
		}

		/*ArrayList<Class<? extends Scroll>> spells = new ArrayList<>(Arrays.asList(
			SpellOfTeleportation.class, SpellOfDamage.class,
			ManaSpell.class
		));

		ArrayList<Type> spellTypes = new ArrayList<>(Arrays.asList(
			Type.JERA, Type.THURISAZ, Type.FEHU//, Type.RAIDO
			// Type.MANNAZ  , Type.TEIWAZ, Type.SOWULO, Type.HAGALAZ
		));

		for (Type type : spellTypes) {
			int i = Random.newInt(spells.size());
			types.put(spells.get(i).getSimpleName().replace("org.rexcellentgames.burningknight.", ""), type);
			identified.put(type, false);
			spells.remove(i);
		}*/
	}
}