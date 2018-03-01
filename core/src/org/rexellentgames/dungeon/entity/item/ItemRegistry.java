package org.rexellentgames.dungeon.entity.item;

import org.rexellentgames.dungeon.entity.item.consumable.potion.HealingPotion;
import org.rexellentgames.dungeon.entity.item.weapon.*;

import java.util.HashMap;

public class ItemRegistry {
	public static HashMap<String, Class<? extends Item>> items = new HashMap<String, Class<? extends Item>>();

	static {
		register("dagger", Dagger.class);
		register("iron_sword", IronSword.class);
		register("the_sword", TheSword.class);
		register("gold", Gold.class);
		register("healing_potion", HealingPotion.class);
	}

	public static void register(String name, Class<? extends Item> item) {
		items.put(name, item);
	}
}