package org.rexcellentgames.burningknight.entity.item;

import org.rexcellentgames.burningknight.entity.creature.npc.Upgrade;
import org.rexcellentgames.burningknight.entity.item.accessory.equippable.TestAccessory;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Revolver;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.MagicMissileWand;
import org.rexcellentgames.burningknight.entity.item.weapon.spear.Spear;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.Guitar;
import org.rexcellentgames.burningknight.game.Achievements;

import java.util.HashMap;

public class ItemRegistry {
	public static Pair[] pairs = {
		new Pair("test", TestAccessory.class, 0.5f, 1f, 0.3f, 0.1f, Quality.GOLDEN),
		new Pair("guitar", Guitar.class, 0.5f, 1f, 0.3f, 0.1f, Quality.GOLDEN),
		new Pair("spear", Spear.class, 1f, 1f, 0.1f, 0.3f, Quality.WOODEN),
		new Pair("missile_wand", MagicMissileWand.class, 1f, 0.3f, 1f, 0.3f, Quality.WOODEN),
		new Pair("gun", Revolver.class, 1f, 0.3f, 0.1f, 1f, Quality.WOODEN),
		new Pair("boom", Boom.class, 1f, 0.3f, 0.1f, 1f, Quality.WOODEN)
	};

	public static HashMap<String, Pair> items = new HashMap<>();

	static {
		for (Pair pair : pairs) {
			items.put(pair.id, pair);
		}
	}

	public static class Pair {
		public Class type;
		public float chance;
		public float warrior;
		public float mage;
		public float ranged;
		public Quality quality;
		public String unlock;
		public int cost;
		public Upgrade.Type pool = Upgrade.Type.NONE;
		public String id;

		public Pair(String id, Class type, float chance, float warrior, float mage, float ranged, Quality quality) {
			this(id, type, chance, warrior, mage, ranged, quality, Upgrade.Type.NONE, 0, null);
		}

		public Pair(String id, Class type, float chance, float warrior, float mage, float ranged, Quality quality, Upgrade.Type pool, int cost, String unlock) {
			this.id = id;
			this.type = type;
			this.chance = chance;
			this.warrior = warrior;
			this.mage = mage;
			this.ranged = ranged;
			this.quality = quality;
			this.unlock = unlock;
			this.cost = cost;
			this.pool = pool;
		}

		public boolean busy;
		public boolean shown;

		public boolean unlocked(String key) {
			if (unlock != null) {
				return Achievements.unlocked(unlock);
			} else if (pool != org.rexcellentgames.burningknight.entity.creature.npc.Upgrade.Type.NONE) {
				return Achievements.unlocked("SHOP_" + key.toUpperCase());
			}

			return true;
		}
	}

	public enum Quality {
		WOODEN, IRON, GOLDEN,
		WOODEN_PLUS, IRON_PLUS, ANY;
	}
	
	public static boolean check(Quality a, Quality q) {
		if (q == Quality.ANY || a == Quality.ANY) {
			return true;
		}

		switch (q) {
			case WOODEN: return a == Quality.WOODEN || a == Quality.WOODEN_PLUS;
			case IRON: return a == Quality.IRON || a == Quality.IRON_PLUS || a == Quality.WOODEN_PLUS;
			case GOLDEN: return a == Quality.GOLDEN || a == Quality.WOODEN_PLUS || a == Quality.IRON_PLUS;
			case WOODEN_PLUS: return true;
			case IRON_PLUS: return a == Quality.IRON || a == Quality.IRON_PLUS || a == Quality.GOLDEN;
			default: return false;
		}
	}
}