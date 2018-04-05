package org.rexellentgames.dungeon.entity.item;

import org.rexellentgames.dungeon.entity.item.consumable.food.Bread;
import org.rexellentgames.dungeon.entity.item.consumable.food.Chicken;
import org.rexellentgames.dungeon.entity.item.consumable.plant.Cabbage;
import org.rexellentgames.dungeon.entity.item.consumable.potion.*;
import org.rexellentgames.dungeon.entity.item.consumable.seed.CabbageSeed;
import org.rexellentgames.dungeon.entity.item.consumable.seed.LightroomSeed;
import org.rexellentgames.dungeon.entity.item.consumable.seed.PotionGrassSeed;
import org.rexellentgames.dungeon.entity.item.consumable.spell.SpellOfDamage;
import org.rexellentgames.dungeon.entity.item.consumable.spell.SpellOfTeleportation;
import org.rexellentgames.dungeon.entity.item.weapon.*;
import org.rexellentgames.dungeon.entity.item.weapon.magic.FireBook;

import java.util.HashMap;

public class ItemRegistry {
	public static HashMap<String, Class<? extends Item>> items = new HashMap<String, Class<? extends Item>>();

	static {
		register("dagger", Dagger.class);
		register("iron_sword", IronSword.class);
		register("another_sword", AnotherSword.class);
		register("the_sword", TheSword.class);
		register("gold", Gold.class);
		register("healing_potion", HealingPotion.class);
		register("sun_potion", SunPotion.class);
		register("fire_potion", FirePotion.class);
		register("invis_potion", InvisibilityPotion.class);
		register("speed_potion", SpeedPotion.class);
		register("regen_potion", RegenerationPotion.class);
		register("poison_potion", PoisonPotion.class);
		register("defense_potion", DefensePotion.class);
		register("tp_spell", SpellOfTeleportation.class);
		register("damage_spell", SpellOfDamage.class);
		register("bread", Bread.class);
		register("chicken", Chicken.class);
		register("bomb", Bomb.class);
		register("cabbage_seed", CabbageSeed.class);
		register("cabbage", Cabbage.class);
		register("lightroom_seed", LightroomSeed.class);
		register("potiongrass_seed", PotionGrassSeed.class);
		register("fire_book", FireBook.class);
	}

	public static void register(String name, Class<? extends Item> item) {
		items.put(name, item);
	}
}