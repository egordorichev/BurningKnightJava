package org.rexcellentgames.burningknight.entity.item

import org.rexcellentgames.burningknight.entity.creature.npc.Upgrade
import org.rexcellentgames.burningknight.entity.item.accessory.equippable.*
import org.rexcellentgames.burningknight.entity.item.accessory.hat.*
import org.rexcellentgames.burningknight.entity.item.autouse.Backpack
import org.rexcellentgames.burningknight.entity.item.autouse.ManaHeart
import org.rexcellentgames.burningknight.entity.item.autouse.Map
import org.rexcellentgames.burningknight.entity.item.autouse.MapGreenprints
import org.rexcellentgames.burningknight.entity.item.consumable.food.Apple
import org.rexcellentgames.burningknight.entity.item.consumable.food.Bread
import org.rexcellentgames.burningknight.entity.item.consumable.food.ManaInABottle
import org.rexcellentgames.burningknight.entity.item.consumable.scroll.ScrollOfUpgrade
import org.rexcellentgames.burningknight.entity.item.key.KeyA
import org.rexcellentgames.burningknight.entity.item.key.KeyB
import org.rexcellentgames.burningknight.entity.item.key.KeyC
import org.rexcellentgames.burningknight.entity.item.permanent.*
import org.rexcellentgames.burningknight.entity.item.pet.orbital.*
import org.rexcellentgames.burningknight.entity.item.reference.*
import org.rexcellentgames.burningknight.entity.item.tool.LavaBucket
import org.rexcellentgames.burningknight.entity.item.tool.Matches
import org.rexcellentgames.burningknight.entity.item.tool.WaterBucket
import org.rexcellentgames.burningknight.entity.item.weapon.axe.Axe
import org.rexcellentgames.burningknight.entity.item.weapon.axe.MeatboyAxe
import org.rexcellentgames.burningknight.entity.item.weapon.bow.Bow
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.Dagger
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.ManaKnife
import org.rexcellentgames.burningknight.entity.item.weapon.gun.*
import org.rexcellentgames.burningknight.entity.item.weapon.gun.shotgun.BronzeShotgun
import org.rexcellentgames.burningknight.entity.item.weapon.laser.LaserGun
import org.rexcellentgames.burningknight.entity.item.weapon.magic.*
import org.rexcellentgames.burningknight.entity.item.weapon.magic.book.*
import org.rexcellentgames.burningknight.entity.item.weapon.rocketlauncher.RocketLauncher
import org.rexcellentgames.burningknight.entity.item.weapon.spear.Spear
import org.rexcellentgames.burningknight.entity.item.weapon.sword.*
import org.rexcellentgames.burningknight.entity.item.weapon.throwing.ConfettiGrenade
import org.rexcellentgames.burningknight.entity.item.weapon.throwing.ThrowingDagger
import org.rexcellentgames.burningknight.entity.item.weapon.throwing.ToxicFlask
import org.rexcellentgames.burningknight.game.Achievements

object ItemRegistry {
	class Pair(val type: Class<out Item>, val chance: Float, val warrior: Float, val mage: Float, val ranged: Float,
						 val quality: Quality, val unlock: String? = null) : Comparable<Pair> {
		override fun compareTo(other: Pair): Int {
			return this.cost.compareTo(other.cost)
		}

		constructor(type: Class<out Item>, chance: Float, warrior: Float, mage: Float, ranged: Float,
								quality: Quality, pool: Upgrade.Type, cost: Int) : this(type, chance, warrior, mage, ranged, quality, null) {

			this.pool = pool
			this.cost = cost
		}

		var cost: Int = 0
		var busy: Boolean = false
		var shown: Boolean = false
		var pool: Upgrade.Type = Upgrade.Type.NONE

		fun unlocked(): Boolean {
			if (unlock != null) {
				return Achievements.unlocked(unlock)
			} else if (pool != Upgrade.Type.NONE) {
				return Achievements.unlocked("SHOP_" + type.simpleName.toUpperCase())
			}

			return true
		}
	}

	// todo: hats

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

	// todo: depend price on quality

  val items = mapOf(
	  // Permanent upgrades
	  "extra_heart" to Pair(ExtraHeart::class.java, 0f, 1f, 1f, 1f, Quality.IRON, Upgrade.Type.PERMANENT, 10),
	  "totally_shop" to Pair(TotallyShop::class.java, 0f, 1f, 1f, 1f, Quality.IRON, Upgrade.Type.PERMANENT, 5),
	  "start_with_health_potion" to Pair(StartWithHealthPotion::class.java, 0f, 1f, 1f, 1f, Quality.IRON, Upgrade.Type.PERMANENT, 3),
	  "starting_armor" to Pair(StartingArmor::class.java, 0f, 1f, 1f, 1f, Quality.IRON, Upgrade.Type.PERMANENT, 1),
	  "more_gold" to Pair(MoreGold::class.java, 0f, 1f, 1f, 1f, Quality.IRON, Upgrade.Type.PERMANENT, 15),
	  "extra_upgrade" to Pair(ExtraUpgrade::class.java, 0f, 1f, 1f, 1f, Quality.IRON, Upgrade.Type.PERMANENT, 8),
	  "better_chest_chance" to Pair(BetterChestChance::class.java, 0f, 1f, 1f, 1f, Quality.IRON, Upgrade.Type.PERMANENT, 12),

		// Decor
	  "top_hat" to Pair(RaveHat::class.java, 0f, 1f, 1f, 1f, Quality.IRON, Upgrade.Type.DECOR, 3),
	  "cowboy_hat" to Pair(CoboiHat::class.java, 0f, 1f, 1f, 1f, Quality.IRON, Upgrade.Type.DECOR, 3),
	  "dunce_hat" to Pair(DunceHat::class.java, 0f, 1f, 1f, 1f, Quality.IRON, Upgrade.Type.DECOR, 3),
	  "fungi_hat" to Pair(FungiHat::class.java, 0f, 1f, 1f, 1f, Quality.IRON, Upgrade.Type.DECOR, 3),
	  "gold_hat" to Pair(FungiHat::class.java, 0f, 1f, 1f, 1f, Quality.IRON, Upgrade.Type.DECOR, 3),
	  "moai_hat" to Pair(MoaiHat::class.java, 0f, 1f, 1f, 1f, Quality.IRON, Upgrade.Type.DECOR, 3),
	  "ruby_hat" to Pair(RubyHat::class.java, 0f, 1f, 1f, 1f, Quality.IRON, Upgrade.Type.DECOR, 3),
	  "fungi_hat" to Pair(FungiHat::class.java, 0f, 1f, 1f, 1f, Quality.IRON, Upgrade.Type.DECOR, 3),

		// Items
		"confetti_gun" to Pair(ConfettiGun::class.java, 1f, 0.3f, 0.3f, 1f, Quality.IRON, Upgrade.Type.WEAPON, 3),
		"bomb_in_bomb" to Pair(BombInABomb::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Upgrade.Type.ACCESSORY, 3),
		"guitar" to Pair(Guitar::class.java, 0f, 1f, 0.3f, 0.1f, Quality.GOLDEN),
		"bk_sword" to Pair(BKSword::class.java, 0f, 1f, 0.3f, 0.1f, Quality.GOLDEN),
		"dagger" to Pair(Dagger::class.java, 1f, 1f, 0.3f, 0.1f, Quality.WOODEN),
		"sword" to Pair(Sword::class.java, 1f, 1f, 0.3f, 0.1f, Quality.WOODEN),
		"butcher" to Pair(Butcher::class.java, 1f, 1f, 0.3f, 0.1f, Quality.WOODEN),
		"morning" to Pair(MorningStar::class.java, 1f, 1f, 0.3f, 0.1f, Quality.WOODEN),
		"axe" to Pair(Axe::class.java, 1f, 1f, 0.1f, 1f, Quality.WOODEN),
		"the_sword" to Pair(TheSword::class.java, 0f, 1f, 0f, 0f, Quality.WOODEN),
		"gold" to Pair(Gold::class.java, 0f, 1f, 1f, 1f, Quality.WOODEN),
		"key_a" to Pair(KeyA::class.java, 0f, 1f, 1f, 1f, Quality.WOODEN),
		"key_b" to Pair(KeyB::class.java, 0f, 1f, 1f, 1f, Quality.WOODEN),
		"key" to Pair(KeyC::class.java, 0f, 1f, 1f, 1f, Quality.WOODEN),
		"bow" to Pair(Bow::class.java, 1f, 0.3f, 0.1f, 1f, Quality.WOODEN),
		"launcher" to Pair(RocketLauncher::class.java, 1f, 0.3f, 0.1f, 1f, Quality.WOODEN),
		"meatboy" to Pair(MeetBoy::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Achievements.UNLOCK_MEATBOY),
		"dendy" to Pair(Dendy::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Achievements.UNLOCK_DENDY),
		"poison_ring" to Pair(PoisonRing::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
		"metal_ring" to Pair(MetalRing::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
		"fire_ring" to Pair(FireRing::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
		"thorn_ring" to Pair(ThornRing::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
		"fortune_ring" to Pair(FortuneRing::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
		"ice_ring" to Pair(IceRing::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
		// "blue_boomerang" to Pair(BlueBoomerang::class.java, 1f, 0.7f, 0.1f, 1f, Quality.WOODEN),
		"magic_mushroom" to Pair(MagicMushroom::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Upgrade.Type.CONSUMABLE, 5),
		"isaac_head" to Pair(IsaacHead::class.java, 1f, 0.3f, 0.1f, 1f, Quality.IRON, Achievements.UNLOCK_ISAAC_HEAD),
		"fire_flower" to Pair(FireFlower::class.java, 1f, 1f, 1f, 1f, Quality.GOLDEN, Upgrade.Type.ACCESSORY, 1),
		"backpack" to Pair(Backpack::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN, Achievements.UNLOCK_BACKPACK),
		"blood_ring" to Pair(BloodRing::class.java, 1f, 1f, 1f, 1f, Quality.IRON),
		"claymore" to Pair(Claymore::class.java, 1f, 1f, 0.3f, 0.1f, Quality.WOODEN),
		"goo" to Pair(GooOrbital::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN, Upgrade.Type.PET, 1),
		// "bumbo" to Pair(Bumbo::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN, Upgrade.Type.PET, 3),
		"gold_ring" to Pair(GoldRing::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Achievements.UNLOCK_GOLD_RING),
		"vampire_ring" to Pair(VampireRing::class.java, 1f, 1f, 1f, 1f, Quality.IRON),
		"vvvvv" to Pair(VVVVV::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN, Achievements.UNLOCK_VVVVV),
		"jelly" to Pair(JellyOrbital::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN, Upgrade.Type.PET, 2),
		"obsidian_boots" to Pair(ObsidianBoots::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN, Upgrade.Type.ACCESSORY, 1),
		"campfire_in_a_bottle" to Pair(CampfireInABottle::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN, Upgrade.Type.ACCESSORY, 1),
		"antidote" to Pair(Antidote::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN, Upgrade.Type.ACCESSORY, 1),
		"fire_extinguisher" to Pair(FireExtinguisher::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
		"broken_orbital" to Pair(BrokenOrbital::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN, Upgrade.Type.PET, 1),
		"vampire_orbital" to Pair(VampireOrbital::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Upgrade.Type.PET, 5),
		"lightsaber" to Pair(Lightsaber::class.java, 1f, 1f, 0.3f, 0.11f, Quality.WOODEN),
		"chicken_sword" to Pair(ChickenSword::class.java, 1f, 1f, 0.3f, 0.1f, Quality.GOLDEN),
		"blue_shovel" to Pair(BlueShovel::class.java, 1f, 1f, 0.3f, 0.1f, Quality.GOLDEN),
		"magic_shield" to Pair(MagicShield::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Upgrade.Type.ACCESSORY, 7),
		"cobalt_shield" to Pair(CobaltShield::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN, Upgrade.Type.ACCESSORY, 3),
		"cross" to Pair(Cross::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Upgrade.Type.ACCESSORY, 10),
		"spectacles" to Pair(Spectacles::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Achievements.UNLOCK_SPECTACLES),
		"diamond" to Pair(Diamond::class.java, 1f, 1f, 1f, 1f, Quality.GOLDEN, Achievements.UNLOCK_DIAMOND),
		"penetration_rune" to Pair(PenetrationRune::class.java, 1f, 1f, 1f, 1f, Quality.IRON),
		"map" to Pair(Map::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Upgrade.Type.CONSUMABLE, 5),
		"map_greenprints" to Pair(MapGreenprints::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN, Upgrade.Type.CONSUMABLE, 3),
		"meatboy_axe" to Pair(MeatboyAxe::class.java, 1f, 0.3f, 0.3f, 1f, Quality.GOLDEN, Achievements.UNLOCK_MEATBOY_AXE),
		"star" to Pair(Star::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Achievements.UNLOCK_STAR),
		"blue_watch" to Pair(BlueWatch::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
		"stopwatch" to Pair(StopWatch::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Achievements.UNLOCK_STOP_WATCH),
		"the_eye" to Pair(TheEye::class.java, 1f, 1f, 1f, 1f, Quality.GOLDEN),
		"lucky_cube" to Pair(LuckyCube::class.java, 1f, 1f, 1f, 1f, Quality.IRON),
		"fortune_armor" to Pair(FortuneArmor::class.java, 1f, 1f, 1f, 1f, Quality.IRON),
		"stop_and_play" to Pair(StopAndPlay::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
		"spear" to Pair(Spear::class.java, 1f, 1f, 0.1f, 0.3f, Quality.WOODEN),
		// "yoyo_a" to Pair(YoyoA::class.java, 1f, 1f, 0.1f, 0.8f, Quality.WOODEN),
		// "yoyo_b" to Pair(YoyoB::class.java, 1f, 1f, 0.1f, 0.8f, Quality.IRON),
		// "yoyo_c" to Pair(YoyoC::class.java, 1f, 1f, 0.1f, 0.8f, Quality.GOLDEN),
		"nano_orbital" to Pair(NanoOrbital::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN, Upgrade.Type.PET, 1),
		"ammo_orbital" to Pair(AmmoOrbital::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Achievements.UNLOCK_AMMO_ORBITAL),
		// "bomb_orbital" to Pair(BombOrbital::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Upgrade.Type.PET, 5),
		"lootpick" to Pair(Lootpick::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN, Achievements.UNLOCK_LOOTPICK),
		"sword_orbital" to Pair(SwordOrbital::class.java, 1f, 1f, 1f, 1f, Quality.GOLDEN, Achievements.UNLOCK_SWORD_ORBITAL),
		"dew_vial" to Pair(DewVial::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Achievements.UNLOCK_DEW_VIAL),
		"challenge_rune" to Pair(ChallengeRune::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
		"luck_rune" to Pair(LuckRune::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
		"stoneheart_rune" to Pair(StoneHeartRune::class.java, 1f, 1f, 1f, 1f, Quality.GOLDEN),
		"rage_rune" to Pair(RageRune::class.java, 1f, 1f, 1f, 1f, Quality.GOLDEN),
		"gravity_booster" to Pair(GravityBooster::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
		"wings" to Pair(Wings::class.java, 1f, 1f, 1f, 1f, Quality.GOLDEN, Achievements.UNLOCK_WINGS),
		"throwing" to Pair(ThrowingDagger::class.java, 1f, 0.3f, 0.1f, 1f, Quality.WOODEN),
		"compass" to Pair(Compass::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN, Upgrade.Type.ACCESSORY, 2),
		"fire_bombs" to Pair(FireBombs::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
		"ice_bombs" to Pair(IceBombs::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
		"poison_bombs" to Pair(PoisonBombs::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
		"protective_band" to Pair(ProtectiveBand::class.java, 0f, 1f, 1f, 1f, Quality.WOODEN),
		"shovel" to Pair(Shovel::class.java, 1f, 1f, 0.3f, 0.1f, Quality.WOODEN, Upgrade.Type.WEAPON, 3),
		"pickaxe" to Pair(Pickaxe::class.java, 1f, 1f, 0.3f, 0.1f, Quality.WOODEN),
		"missile_wand" to Pair(MagicMissileWand::class.java, 1f, 0.3f, 1f, 0.3f, Quality.WOODEN),
		"laser_aim" to Pair(LaserAim::class.java, 1f, 0f, 0.3f, 1f, Quality.IRON),
		"poison_wand" to Pair(PoisonWand::class.java, 1f, 0.3f, 1f, 0.3f, Quality.WOODEN),
		"fire_wand" to Pair(FireWand::class.java, 1f, 0.3f, 1f, 0.3f, Quality.WOODEN),
		"ice_wand" to Pair(IceWand::class.java, 1f, 0.3f, 1f, 0.3f, Quality.WOODEN),
		"waterbolt" to Pair(Waterbolt::class.java, 0.8f, 0.3f, 1f, 0.3f, Quality.IRON, Achievements.UNLOCK_WATER_BOLT),
		"firebolt" to Pair(Firebolt::class.java, 0.8f, 0.3f, 1f, 0.3f, Quality.IRON),
		"mana_ring" to Pair(ManaRing::class.java, 1f, 0f, 1f, 0f, Quality.WOODEN),
		"mana_up" to Pair(StarOnAString::class.java, 1f, 0f, 1f, 0f, Quality.WOODEN),
		"mana_bottle" to Pair(ManaBottle::class.java, 1f, 0f, 1f, 0f, Quality.WOODEN),
		"mana_heart" to Pair(ManaHeart::class.java, 1f, 0f, 1f, 0f, Quality.WOODEN),
		// "blue_bomb" to Pair(BlueBomb::class.java, 1f, 0f, 1f, 0f, Quality.WOODEN),
		"mana_knife" to Pair(ManaKnife::class.java, 1f, 0f, 1f, 0f, Quality.WOODEN, Upgrade.Type.WEAPON, 4),
		"halo" to Pair(Halo::class.java, 1f, 1f, 1f, 1f, Quality.GOLDEN, Achievements.UNLOCK_HALO),
		"blue_book" to Pair(BlueBook::class.java, 1f, 0f, 1f, 0f, Quality.WOODEN, Upgrade.Type.ACCESSORY, 3),
		// "red_book" to Pair(RedBook::class.java, 1f, 0f, 1f, 0f, Quality.WOODEN, Upgrade.Type.ACCESSORY, 3),
		"yellow_book" to Pair(YellowBook::class.java, 1f, 0f, 1f, 0f, Quality.IRON, Upgrade.Type.ACCESSORY, 3),
		"green_book" to Pair(GreenBook::class.java, 1f, 0f, 1f, 0f, Quality.GOLDEN, Upgrade.Type.ACCESSORY, 3),
		"arcane_shield" to Pair(ManaShield::class.java, 1f, 0.3f, 1f, 0.3f, Quality.WOODEN),
		"flying_star" to Pair(FlyingStar::class.java, 1f, 0f, 1f, 0f, Quality.WOODEN, Upgrade.Type.PET, 3),
		"diamond_sword" to Pair(DiamondSword::class.java, 1f, 1f, 0.3f, 0.1f, Quality.GOLDEN, Achievements.UNLOCK_DIAMOND_SWORD),
		"blood_crown" to Pair(BloodCrown::class.java, 1f, 1f, 1f, 1f, Quality.GOLDEN, Achievements.UNLOCK_BLOOD_CROWN),
		"mana_boots" to Pair(ManaBoots::class.java, 1f, 0f, 1f, 0f, Quality.IRON),
		"damage_emblem" to Pair(DamageEmblem::class.java, 1f, 1f, 1f, 1f, Quality.GOLDEN),
		"defense_emblem" to Pair(DefenseEmblem::class.java, 1f, 1f, 1f, 1f, Quality.GOLDEN),
		"arcane_battery" to Pair(ArcaneBattery::class.java, 1f, 0f, 1f, 0f, Quality.WOODEN),
		// "blue_coin" to Pair(BlueCoin::class.java, 1f, 0f, 1f, 0f, Quality.WOODEN),
		// "blue_heart" to Pair(BlueHeart::class.java, 1f, 0f, 1f, 0f, Quality.WOODEN),
		"black_heart" to Pair(BlackHeart::class.java, 1f, 1f, 1f, 1f, Quality.GOLDEN, Achievements.UNLOCK_BLACK_HEART),
		"clock_heart" to Pair(ClockHeart::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Achievements.UNLOCK_CLOCK_HEART),
		"aim_book" to Pair(HomingBook::class.java, 1f, 0.1f, 1f, 0.3f, Quality.IRON, Upgrade.Type.WEAPON, 3),
		"triple_book" to Pair(TripleShotBook::class.java, 1f, 0.1f, 1f, 0.3f, Quality.IRON, Upgrade.Type.WEAPON, 2),
		"wall_book" to Pair(MagicWallBook::class.java, 1f, 0.1f, 1f, 0.3f, Quality.IRON, Upgrade.Type.WEAPON, 1),
		"slow_book" to Pair(SlowBook::class.java, 1f, 0.1f, 1f, 0.3f, Quality.GOLDEN),
		"fast_book" to Pair(FastBook::class.java, 1f, 0.1f, 1f, 0.3f, Quality.WOODEN),
		"crazy_book" to Pair(CrazyBook::class.java, 1f, 1f, 1f, 0.3f, Quality.IRON),
		"crash_book" to Pair(SuperCrazyBook::class.java, 0.3f, 0.1f, 1f, 0.1f, Quality.GOLDEN, Achievements.UNLOCK_CRASH_BOOK),
		"demage_emblem" to Pair(DemageEmblem::class.java, 1f, 1f, 1f, 1f, Quality.IRON),
		"apple" to Pair(Apple::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN, Upgrade.Type.CONSUMABLE, 1),
		"mana_bottle" to Pair(ManaInABottle::class.java, 1f, 0f, 1f, 0f, Quality.WOODEN),
		"bread" to Pair(Bread::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN, Upgrade.Type.CONSUMABLE, 2),
		"mana_emblem" to Pair(ManaEmblem::class.java, 1f, 0f, 1f, 0f, Quality.WOODEN),
		"bronze_shotgun" to Pair(BronzeShotgun::class.java, 1f, 0.3f, 0.3f, 1f, Quality.IRON),
		"back_gun" to Pair(BackGun::class.java, 1f, 10.3f, 0.1f, 1f, Quality.IRON),
		"kotling_gun" to Pair(KotlingGun::class.java, 0.1f, 0.3f, 0.1f, 1f, Quality.WOODEN, Achievements.UNLOCK_KOTLING_GUN),
		"machine_gun" to Pair(MachineGun::class.java, 1f, 0.3f, 0.1f, 1f, Quality.IRON),
		"triple_machine_gun" to Pair(TripleMachineGun::class.java, 1f, 0.3f, 0.1f, 1f, Quality.GOLDEN),
		"gun" to Pair(Revolver::class.java, 1f, 0.3f, 0.1f, 1f, Quality.WOODEN),

		"laser_gun" to Pair(LaserGun::class.java, 0f, 0.3f, 0.1f, 1f, Quality.WOODEN),

		"burst" to Pair(BurstGun::class.java, 1f, 0.3f, 0.1f, 1f, Quality.WOODEN),
		"noppy" to Pair(NoppyGun::class.java, 1f, 0.3f, 0.1f, 1f, Quality.WOODEN),
		"puncher" to Pair(PuncherGun::class.java, 1f, 0.3f, 0.1f, 1f, Quality.WOODEN),
		"snow_gun" to Pair(SnowGun::class.java, 1f, 0.3f, 0.1f, 1f, Quality.IRON),
		"aim" to Pair(Aim::class.java, 1f, 0f, 0.3f, 1f, Quality.IRON),
		"cursed_aim" to Pair(CursedAim::class.java, 1f, 0f, 0.3f, 1f, Quality.IRON),
		"zoom" to Pair(Zoom::class.java, 1f, 0.3f, 0.3f, 1f, Quality.WOODEN),
		"rifle" to Pair(Rifle::class.java, 1f, 0.3f, 0.1f, 1f, Quality.IRON),
		"chopper" to Pair(Chopper::class.java, 1f, 0.3f, 0.1f, 1f, Quality.GOLDEN),
		"pistol" to Pair(Pistol::class.java, 1f, 0.3f, 0.1f, 1f, Quality.WOODEN),
		// "headshot" to Pair(HeadshotGun::class.java, 1f, 0.3f, 0.1f, 1f, Quality.IRON),
		"hammer" to Pair(Hammer::class.java, 1f, 0.3f, 0.1f, 1f, Quality.IRON, Upgrade.Type.WEAPON, 5),
		"money_printer" to Pair(MoneyPrinter::class.java, 1f, 0.3f, 0.1f, 1f, Quality.IRON, Achievements.UNLOCK_MONEY_PRINTER),
		"reload_rune" to Pair(ReloadRune::class.java, 1f, 0.3f, 0.1f, 1f, Quality.GOLDEN),
		"izu" to Pair(Izu::class.java, 1f, 0.3f, 0f, 1f, Quality.GOLDEN, Upgrade.Type.WEAPON, 7),
		"old_manual" to Pair(OldManual::class.java, 1f, 0f, 0f, 1f, Quality.WOODEN),
		"ammo_holder" to Pair(AmmoHolder::class.java, 1f, 0f, 0f, 1f, Quality.GOLDEN),
		"lucky_bullet" to Pair(LuckyBullet::class.java, 1f, 0f, 0f, 1f, Quality.GOLDEN),
		"smart_bullet" to Pair(SmartBullet::class.java, 1f, 0f, 0f, 1f, Quality.IRON),
		// "rng_bullet" to Pair(RngBullet::class.java, 1f, 0f, 0f, 1f, Quality.IRON),
		"time_bullet" to Pair(TimeBullet::class.java, 1f, 0f, 0f, 1f, Quality.GOLDEN),
		"big_bullet" to Pair(BigBullet::class.java, 1f, 0f, 0f, 1f, Quality.WOODEN),
		"red_balloon" to Pair(RedBalloon::class.java, 1f, 1f, 1f, 1f, Quality.IRON),
		"sale" to Pair(ShopSale::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Achievements.UNLOCK_SALE),
		"toxic_flask" to Pair(ToxicFlask::class.java, 1f, 1f, 0.3f, 1f, Quality.WOODEN),
		"mimic_totem" to Pair(MimicTotem::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Achievements.UNLOCK_MIMIC_TOTEM),
		"mimic_summoner" to Pair(MimicSummoner::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN, Achievements.UNLOCK_MIMIC_SUMMONER),
		"fire_boots" to Pair(FireBoots::class.java, 1f, 1f, 1f, 1f, Quality.IRON),
		"ice_boots" to Pair(IceBoots::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
		"upgrade" to Pair(ScrollOfUpgrade::class.java, 0f, 1f, 1f, 1f, Quality.GOLDEN, Achievements.UPGRADE),
		"ice_skates" to Pair(IceSkates::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
		"scissors" to Pair(Scissors::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
		"elemental_ring" to Pair(ElementalRing::class.java, 1f, 1f, 1f, 1f, Quality.IRON),
		"lava_bucket" to Pair(LavaBucket::class.java, 0f, 1f, 1f, 1f, Quality.WOODEN),
		"water_bucket" to Pair(WaterBucket::class.java, 0f, 1f, 1f, 1f, Quality.WOODEN),
		"tech_eye" to Pair(TechEye::class.java, 2f, 1f, 1f, 1f, Quality.IRON),
		"eco_wand" to Pair(EcoWand::class.java, 1f, 0f, 1f, 0.1f, Quality.WOODEN),
		"flippers" to Pair(Flippers::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
		"flame" to Pair(FlameThrower::class.java, 1f, 0.1f, 0.3f, 1f, Quality.GOLDEN),
		"bomb" to Pair(Bomb::class.java, 0f, 1f, 1f, 1f, Quality.WOODEN),
		"matches" to Pair(Matches::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
		"confetti_grenade" to Pair(ConfettiGrenade::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN, Upgrade.Type.WEAPON, 3),
		"coboi_hat" to Pair(CoboiHat::class.java, 0f, 1f, 1f, 1f, Quality.WOODEN, Upgrade.Type.DECOR, 10),
		"dunce_hat" to Pair(DunceHat::class.java, 0f, 1f, 1f, 1f, Quality.WOODEN, Upgrade.Type.DECOR, 5),
		"fungi_hat" to Pair(FungiHat::class.java, 0f, 1f, 1f, 1f, Quality.WOODEN, Upgrade.Type.DECOR, 8),
		"gold_hat" to Pair(GoldHat::class.java, 0f, 1f, 1f, 1f, Quality.WOODEN, Upgrade.Type.DECOR, 50),
		"moai_hat" to Pair(MoaiHat::class.java, 0f, 1f, 1f, 1f, Quality.WOODEN, Upgrade.Type.DECOR, 20),
		"rave_hat" to Pair(RaveHat::class.java, 0f, 1f, 1f, 1f, Quality.WOODEN, Upgrade.Type.DECOR, 16),
		"ruby_hat" to Pair(RubyHat::class.java, 0f, 1f, 1f, 1f, Quality.WOODEN, Upgrade.Type.DECOR, 100),
		"shroom_hat" to Pair(ShroomHat::class.java, 0f, 1f, 1f, 1f, Quality.WOODEN, Upgrade.Type.DECOR, 13),
		"skull_hat" to Pair(SkullHat::class.java, 0f, 1f, 1f, 1f, Quality.WOODEN, Upgrade.Type.DECOR, 20),
		"valkyre_hat" to Pair(ValkyreHat::class.java, 0f, 1f, 1f, 1f, Quality.WOODEN, Upgrade.Type.DECOR, 18)
	).toList().sortedBy { (_, value) -> value }.toMap()

	val modItems = mutableMapOf<String, Item>()
}