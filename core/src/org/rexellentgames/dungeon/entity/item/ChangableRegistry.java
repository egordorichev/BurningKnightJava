package org.rexellentgames.dungeon.entity.item;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.item.consumable.potion.*;
import org.rexellentgames.dungeon.entity.item.consumable.spell.*;
import org.rexellentgames.dungeon.entity.item.weapon.magic.DefenseBook;
import org.rexellentgames.dungeon.entity.item.weapon.magic.FireBook;
import org.rexellentgames.dungeon.entity.item.weapon.magic.MagicWeapon;
import org.rexellentgames.dungeon.entity.item.weapon.magic.NoteBook;
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
		RED("item (potion A)"),
		BLUE("item (potion B)"),
		ORANGE("item (potion C)"),
		GREEN("item (potion D)"),
		YELLOW("item (potion E)"),
		CORAL("item (potion F)"),
		PINK("item (potion G)"),
		BROWN("item (potion H)"),

		JERA("item (scroll A)"),
		THURISAZ("item (scroll B)"),
		FEHU("item (scroll C)"),
		RAIDO("item (scroll D)"),
		MANNAZ("item (scroll E)"),
		/*TEIWAZ(69),
		SOWULO(70),
		HAGALAZ(71)*/

		DEMON("item (wand A)"),
		HOLY("item (wand B)"),
		CIRLE("item (wand C)");

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
		ArrayList<Class<? extends Potion>> potions = new ArrayList<Class<? extends Potion>>(Arrays.asList(
			HealingPotion.class, SunPotion.class, FirePotion.class, InvisibilityPotion.class, SpeedPotion.class,
			RegenerationPotion.class, PoisonPotion.class, DefensePotion.class
		));

		ArrayList<Type> potionTypes = new ArrayList<Type>(Arrays.asList(
			Type.RED, Type.BLUE, Type.ORANGE, Type.GREEN, Type.YELLOW, Type.CORAL, Type.PINK, Type.BROWN
		));

		for (Type type : potionTypes) {
			int i = Random.newInt(potions.size());
			types.put(potions.get(i).getSimpleName(), type);
			identified.put(type, false);
			potions.remove(i);
		}

		ArrayList<Class<? extends Spell>> spells = new ArrayList<Class<? extends Spell>>(Arrays.asList(
			SpellOfTeleportation.class, SpellOfDamage.class, GhostLeaver.class,
			WormHole.class, ManaSpell.class
		));

		// todo: finish this list
		ArrayList<Type> spellTypes = new ArrayList<Type>(Arrays.asList(
			Type.JERA, Type.THURISAZ, Type.FEHU, Type.RAIDO,
			Type.MANNAZ // , Type.TEIWAZ, Type.SOWULO, Type.HAGALAZ
		));

		for (Type type : spellTypes) {
			int i = Random.newInt(spells.size());
			types.put(spells.get(i).getSimpleName(), type);
			identified.put(type, false);
			spells.remove(i);
		}


		ArrayList<Class<? extends MagicWeapon>> wands = new ArrayList<>(Arrays.asList(
			FireBook.class, DefenseBook.class, NoteBook.class
		));

		// todo: finish this list
		ArrayList<Type> wandTypes = new ArrayList<Type>(Arrays.asList(
			Type.CIRLE, Type.HOLY, Type.DEMON
		));

		for (Type type : wandTypes) {
			int i = Random.newInt(wands.size());
			types.put(wands.get(i).getSimpleName(), type);
			identified.put(type, false);
			wands.remove(i);
		}
	}
}