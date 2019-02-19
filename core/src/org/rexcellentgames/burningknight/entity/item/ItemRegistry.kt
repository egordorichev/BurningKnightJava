package org.rexcellentgames.burningknight.entity.item

import org.rexcellentgames.burningknight.entity.creature.npc.Upgrade
import org.rexcellentgames.burningknight.entity.item.accessory.equippable.TestAccessory
import org.rexcellentgames.burningknight.entity.item.accessory.hat.*
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.Dagger
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Revolver
import org.rexcellentgames.burningknight.entity.item.weapon.magic.MagicMissileWand
import org.rexcellentgames.burningknight.entity.item.weapon.spear.Spear
import org.rexcellentgames.burningknight.entity.item.weapon.sword.Guitar
import org.rexcellentgames.burningknight.game.Achievements

object ItemRegistry {
	val items = mapOf(
		// Decor
		"cowboy_hat" to Pair(CoboiHat::class.java, 0f, 1f, 1f, 1f, Quality.IRON, org.rexcellentgames.burningknight.entity.creature.npc.Upgrade.Type.DECOR, 10),
		"dunce_hat" to Pair(DunceHat::class.java, 0f, 1f, 1f, 1f, Quality.IRON, org.rexcellentgames.burningknight.entity.creature.npc.Upgrade.Type.DECOR, 1),
		"fungi_hat" to Pair(FungiHat::class.java, 0f, 1f, 1f, 1f, Quality.IRON, org.rexcellentgames.burningknight.entity.creature.npc.Upgrade.Type.DECOR, 5),
		"gold_hat" to Pair(GoldHat::class.java, 0f, 1f, 1f, 1f, Quality.IRON, org.rexcellentgames.burningknight.entity.creature.npc.Upgrade.Type.DECOR, 13),
		"moai_hat" to Pair(MoaiHat::class.java, 0f, 1f, 1f, 1f, Quality.IRON, org.rexcellentgames.burningknight.entity.creature.npc.Upgrade.Type.DECOR, 2),
		"ruby_hat" to Pair(RubyHat::class.java, 0f, 1f, 1f, 1f, Quality.IRON, org.rexcellentgames.burningknight.entity.creature.npc.Upgrade.Type.DECOR, 16),
		"shroom_hat" to Pair(ShroomHat::class.java, 0f, 1f, 1f, 1f, Quality.IRON, org.rexcellentgames.burningknight.entity.creature.npc.Upgrade.Type.DECOR, 6),
		"rave_hat" to Pair(RaveHat::class.java, 0f, 1f, 1f, 1f, Quality.IRON, org.rexcellentgames.burningknight.entity.creature.npc.Upgrade.Type.DECOR, 10),
		"skull_hat" to Pair(SkullHat::class.java, 0f, 1f, 1f, 1f, Quality.IRON, org.rexcellentgames.burningknight.entity.creature.npc.Upgrade.Type.DECOR, 4),
		"ushanka_hat" to Pair(UshankaHat::class.java, 0f, 1f, 1f, 1f, Quality.IRON, org.rexcellentgames.burningknight.entity.creature.npc.Upgrade.Type.DECOR, 7),
		"viking_hat" to Pair(VikingHat::class.java, 0f, 1f, 1f, 1f, Quality.IRON, org.rexcellentgames.burningknight.entity.creature.npc.Upgrade.Type.DECOR, 7),
		"valkyre_hat" to Pair(ValkyreHat::class.java, 0f, 1f, 1f, 1f, Quality.IRON, org.rexcellentgames.burningknight.entity.creature.npc.Upgrade.Type.DECOR, 8),
		"knight_hat" to Pair(KnightHat::class.java, 0f, 1f, 1f, 1f, Quality.IRON, org.rexcellentgames.burningknight.entity.creature.npc.Upgrade.Type.DECOR, 5),

		// Items
		"test" to Pair(TestAccessory::class.java, 0.5f, 1f, 0.3f, 0.1f, Quality.GOLDEN),
		"guitar" to Pair(Guitar::class.java, 0.5f, 1f, 0.3f, 0.1f, Quality.GOLDEN),
		"spear" to Pair(Spear::class.java, 1f, 1f, 0.1f, 0.3f, Quality.WOODEN),
		"spear" to Pair(Dagger::class.java, 1f, 1f, 0.1f, 0.3f, Quality.WOODEN),
		"missile_wand" to Pair(MagicMissileWand::class.java, 1f, 0.3f, 1f, 0.3f, Quality.WOODEN),
		"gun" to Pair(Revolver::class.java, 1f, 0.3f, 0.1f, 1f, Quality.WOODEN)
	).toList().sortedBy { (_, value) -> value }.toMap()

	class Pair(val type: Class<out Item>, val chance: Float, val warrior: Float, val mage: Float, val ranged: Float,
						 val quality: Quality, val unlock: String? = null) : Comparable<Pair> {
		override fun compareTo(other: Pair): Int {
			return this.cost.compareTo(other.cost)
		}

		constructor(type: Class<out Item>, chance: Float, warrior: Float, mage: Float, ranged: Float,
		            quality: Quality, pool: org.rexcellentgames.burningknight.entity.creature.npc.Upgrade.Type, cost: Int) : this(type, chance, warrior, mage, ranged, quality, null) {

			this.pool = pool
			this.cost = cost
		}

		var cost: Int = 0
		var busy: Boolean = false
		var shown: Boolean = false
		var pool: org.rexcellentgames.burningknight.entity.creature.npc.Upgrade.Type = org.rexcellentgames.burningknight.entity.creature.npc.Upgrade.Type.NONE

		fun unlocked(key: String): Boolean {
			if (unlock != null) {
				return Achievements.unlocked(unlock)
			} else if (pool != org.rexcellentgames.burningknight.entity.creature.npc.Upgrade.Type.NONE) {
				return Achievements.unlocked("SHOP_" + key.toUpperCase())
			}

			return true
		}
	}

	enum class Quality {
		WOODEN, IRON, GOLDEN,
		WOODEN_PLUS, IRON_PLUS, ANY;

		fun check(q: Quality): Boolean {
			if (q == Quality.ANY || this == Quality.ANY) {
				return true
			}

			return when (q) {
				Quality.WOODEN -> this == Quality.WOODEN || this == Quality.WOODEN_PLUS
				Quality.IRON -> this == Quality.IRON || this == Quality.IRON_PLUS || this == Quality.WOODEN_PLUS
				Quality.GOLDEN -> this == Quality.GOLDEN || this == Quality.WOODEN_PLUS || this == Quality.IRON_PLUS || this == Quality.WOODEN_PLUS
				Quality.WOODEN_PLUS -> true
				Quality.IRON_PLUS -> this == Quality.IRON || this == Quality.IRON_PLUS || this == Quality.GOLDEN
				else -> false
			}
		}
	}
}