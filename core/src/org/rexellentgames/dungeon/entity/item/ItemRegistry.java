package org.rexellentgames.dungeon.entity.item;

import org.rexellentgames.dungeon.entity.item.consumable.food.Bread;
import org.rexellentgames.dungeon.entity.item.consumable.food.Chicken;
import org.rexellentgames.dungeon.entity.item.consumable.plant.Cabbage;
import org.rexellentgames.dungeon.entity.item.consumable.potion.*;
import org.rexellentgames.dungeon.entity.item.consumable.seed.CabbageSeed;
import org.rexellentgames.dungeon.entity.item.consumable.seed.LightroomSeed;
import org.rexellentgames.dungeon.entity.item.consumable.seed.PotionGrassSeed;
import org.rexellentgames.dungeon.entity.item.consumable.spell.ManaSpell;
import org.rexellentgames.dungeon.entity.item.consumable.spell.SpellOfDamage;
import org.rexellentgames.dungeon.entity.item.consumable.spell.SpellOfTeleportation;
import org.rexellentgames.dungeon.entity.item.weapon.*;
import org.rexellentgames.dungeon.entity.item.weapon.magic.DefenseBook;
import org.rexellentgames.dungeon.entity.item.weapon.magic.FireBook;
import org.rexellentgames.dungeon.entity.item.consumable.spell.GhostLeaver;
import org.rexellentgames.dungeon.entity.item.weapon.magic.NoteBook;
import org.rexellentgames.dungeon.entity.item.weapon.ranged.Arrow;
import org.rexellentgames.dungeon.entity.item.weapon.ranged.WoodenBow;
import org.rexellentgames.dungeon.entity.item.weapon.sword.SwordA;
import org.rexellentgames.dungeon.entity.item.weapon.sword.SwordB;
import org.rexellentgames.dungeon.entity.item.weapon.sword.SwordC;
import org.rexellentgames.dungeon.entity.item.weapon.sword.TheSword;
import org.rexellentgames.dungeon.entity.item.weapon.sword.butcher.ButcherA;
import org.rexellentgames.dungeon.entity.item.weapon.sword.butcher.ButcherB;
import org.rexellentgames.dungeon.entity.item.weapon.sword.butcher.ButcherC;
import org.rexellentgames.dungeon.entity.item.weapon.sword.morning.MorningStarA;
import org.rexellentgames.dungeon.entity.item.weapon.sword.morning.MorningStarB;
import org.rexellentgames.dungeon.entity.item.weapon.sword.morning.MorningStarC;

import java.util.HashMap;

public class ItemRegistry {
	public static HashMap<String, Class<? extends Item>> items = new HashMap<String, Class<? extends Item>>();

	static {
		register("dagger", Dagger.class);
		register("sword_a", SwordA.class);
		register("sword_b", SwordB.class);
		register("sword_c", SwordC.class);
		register("butcher_a", ButcherA.class);
		register("butcher_b", ButcherB.class);
		register("butcher_c", ButcherC.class);
		register("morning_a", MorningStarA.class);
		register("morning_b", MorningStarB.class);
		register("morning_c", MorningStarC.class);
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
		register("note_book", NoteBook.class);
		register("arrow", Arrow.class);
		register("wooden_bow", WoodenBow.class);
		register("guitar", Guitar.class);
		register("lamp", Lamp.class);
		register("defense_book", DefenseBook.class);
		register("ghost", GhostLeaver.class);
		register("worm_hole", WormHole.class);
		register("mana_spell", ManaSpell.class);
		register("key", Key.class);
	}

	public static void register(String name, Class<? extends Item> item) {
		items.put(name, item);
	}
}