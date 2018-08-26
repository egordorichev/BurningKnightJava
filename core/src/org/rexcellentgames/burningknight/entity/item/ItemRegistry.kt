package org.rexcellentgames.burningknight.entity.item

import org.rexcellentgames.burningknight.entity.item.accessory.equipable.*
import org.rexcellentgames.burningknight.entity.item.autouse.Backpack
import org.rexcellentgames.burningknight.entity.item.autouse.ManaHeart
import org.rexcellentgames.burningknight.entity.item.autouse.Map
import org.rexcellentgames.burningknight.entity.item.autouse.MapGreenprints
import org.rexcellentgames.burningknight.entity.item.consumable.food.Apple
import org.rexcellentgames.burningknight.entity.item.consumable.food.Bread
import org.rexcellentgames.burningknight.entity.item.consumable.food.ManaInABottle
import org.rexcellentgames.burningknight.entity.item.consumable.potion.PoisonPotion
import org.rexcellentgames.burningknight.entity.item.consumable.scroll.ScrollOfUpgrade
import org.rexcellentgames.burningknight.entity.item.key.KeyA
import org.rexcellentgames.burningknight.entity.item.key.KeyB
import org.rexcellentgames.burningknight.entity.item.key.KeyC
import org.rexcellentgames.burningknight.entity.item.pet.Bumbo
import org.rexcellentgames.burningknight.entity.item.pet.orbital.*
import org.rexcellentgames.burningknight.entity.item.reference.*
import org.rexcellentgames.burningknight.entity.item.tool.LavaBucket
import org.rexcellentgames.burningknight.entity.item.tool.WaterBucket
import org.rexcellentgames.burningknight.entity.item.weapon.HeadshotGun
import org.rexcellentgames.burningknight.entity.item.weapon.axe.*
import org.rexcellentgames.burningknight.entity.item.weapon.bow.BowA
import org.rexcellentgames.burningknight.entity.item.weapon.bow.BowB
import org.rexcellentgames.burningknight.entity.item.weapon.bow.BowC
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.DaggerA
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.DaggerB
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.DaggerC
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.ManaKnife
import org.rexcellentgames.burningknight.entity.item.weapon.gun.*
import org.rexcellentgames.burningknight.entity.item.weapon.gun.shotgun.BronzeShotgun
import org.rexcellentgames.burningknight.entity.item.weapon.magic.*
import org.rexcellentgames.burningknight.entity.item.weapon.magic.book.*
import org.rexcellentgames.burningknight.entity.item.weapon.rocketlauncher.RocketLauncherA
import org.rexcellentgames.burningknight.entity.item.weapon.rocketlauncher.RocketLauncherB
import org.rexcellentgames.burningknight.entity.item.weapon.rocketlauncher.RocketLauncherC
import org.rexcellentgames.burningknight.entity.item.weapon.spear.SpearA
import org.rexcellentgames.burningknight.entity.item.weapon.spear.SpearB
import org.rexcellentgames.burningknight.entity.item.weapon.spear.SpearC
import org.rexcellentgames.burningknight.entity.item.weapon.sword.*
import org.rexcellentgames.burningknight.entity.item.weapon.sword.butcher.ButcherA
import org.rexcellentgames.burningknight.entity.item.weapon.sword.butcher.ButcherB
import org.rexcellentgames.burningknight.entity.item.weapon.sword.butcher.ButcherC
import org.rexcellentgames.burningknight.entity.item.weapon.sword.claymore.ClaymoreA
import org.rexcellentgames.burningknight.entity.item.weapon.sword.claymore.ClaymoreB
import org.rexcellentgames.burningknight.entity.item.weapon.sword.claymore.ClaymoreC
import org.rexcellentgames.burningknight.entity.item.weapon.sword.morning.MorningStarA
import org.rexcellentgames.burningknight.entity.item.weapon.sword.morning.MorningStarB
import org.rexcellentgames.burningknight.entity.item.weapon.sword.morning.MorningStarC
import org.rexcellentgames.burningknight.entity.item.weapon.sword.starwars.LightsaberA
import org.rexcellentgames.burningknight.entity.item.weapon.sword.starwars.LightsaberB
import org.rexcellentgames.burningknight.entity.item.weapon.sword.starwars.LightsaberC
import org.rexcellentgames.burningknight.entity.item.weapon.sword.starwars.LightsaberD
import org.rexcellentgames.burningknight.entity.item.weapon.sword.tool.*
import org.rexcellentgames.burningknight.entity.item.weapon.throwing.ThrowingDaggerA
import org.rexcellentgames.burningknight.entity.item.weapon.throwing.ThrowingDaggerB
import org.rexcellentgames.burningknight.entity.item.weapon.throwing.ThrowingDaggerC
import org.rexcellentgames.burningknight.entity.item.weapon.throwing.ToxicFlask
import org.rexcellentgames.burningknight.entity.item.weapon.yoyo.YoyoA
import org.rexcellentgames.burningknight.entity.item.weapon.yoyo.YoyoB
import org.rexcellentgames.burningknight.entity.item.weapon.yoyo.YoyoC
import org.rexcellentgames.burningknight.game.Achievements

object ItemRegistry {
	class Pair(val type: Class<out Item>, val chance: Float, val warrior: Float, val mage: Float, val ranged: Float, val quality: Quality, val unlock: String? = null)

	enum class Quality {
		WOODEN, IRON, GOLDEN
	}

	// todo: depend price on quality
	
  val items = mapOf(
	  "bk_sword" to Pair(BKSword::class.java, 0f, 1f, 0.3f, 0.1f, Quality.GOLDEN),

    "dagger_a" to Pair(DaggerA::class.java, 0.3f, 1f, 0.3f, 0.1f, Quality.WOODEN),
    "dagger_b" to Pair(DaggerB::class.java, 0.3f, 1f, 0.3f, 0.1f, Quality.IRON),
    "dagger_c" to Pair(DaggerC::class.java, 0.3f, 1f, 0.3f, 0.1f, Quality.GOLDEN),
    "sword_a" to Pair(SwordA::class.java, 0.3f, 1f, 0.3f, 0.1f, Quality.WOODEN),
    "sword_b" to Pair(SwordB::class.java, 0.3f, 1f, 0.3f, 0.1f, Quality.IRON),
    "sword_c" to Pair(SwordC::class.java, 0.3f, 1f, 0.3f, 0.1f, Quality.GOLDEN),
    "butcher_a" to Pair(ButcherA::class.java, 0.3f, 1f, 0.3f, 0.1f, Quality.WOODEN),
    "butcher_b" to Pair(ButcherB::class.java, 0.3f, 1f, 0.3f, 0.1f, Quality.IRON),
    "butcher_c" to Pair(ButcherC::class.java, 0.3f, 1f, 0.3f, 0.1f, Quality.GOLDEN),
    "morning_a" to Pair(MorningStarA::class.java, 0.3f, 1f, 0.3f, 0.1f, Quality.WOODEN),
    "morning_b" to Pair(MorningStarB::class.java, 0.3f, 1f, 0.3f, 0.1f, Quality.IRON),
    "morning_c" to Pair(MorningStarC::class.java, 0.3f, 1f, 0.3f, 0.1f, Quality.GOLDEN),
    "axe_a" to Pair(AxeA::class.java, 1f, 0.3f, 0.1f, 1f, Quality.WOODEN),
    "axe_b" to Pair(AxeB::class.java, 1f, 0.3f, 0.1f, 1f, Quality.IRON),
    "axe_c" to Pair(AxeC::class.java, 1f, 0.3f, 0.1f, 1f, Quality.GOLDEN),
    "axe_d" to Pair(AxeD::class.java, 1f, 0.3f, 0.1f, 1f, Quality.GOLDEN),
    "the_sword" to Pair(TheSword::class.java, 0f, 1f, 0f, 0f, Quality.WOODEN),
    "gold" to Pair(Gold::class.java, 0f, 1f, 1f, 1f, Quality.WOODEN),
	  "key_a" to Pair(KeyA::class.java, 0f, 1f, 1f, 1f, Quality.WOODEN),
	  "key_b" to Pair(KeyB::class.java, 0f, 1f, 1f, 1f, Quality.WOODEN),
	  "key" to Pair(KeyC::class.java, 0f, 1f, 1f, 1f, Quality.WOODEN),
    "bow_a" to Pair(BowA::class.java, 1f, 0.3f, 0.1f, 1f, Quality.WOODEN),
    "bow_b" to Pair(BowB::class.java, 1f, 0.3f, 0.1f, 1f, Quality.IRON),
    "bow_c" to Pair(BowC::class.java, 1f, 0.3f, 0.1f, 1f, Quality.GOLDEN),
    "compass" to Pair(Compass::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "launcher_a" to Pair(RocketLauncherA::class.java, 1f, 0.3f, 0.1f, 1f, Quality.WOODEN),
    "launcher_b" to Pair(RocketLauncherB::class.java, 1f, 0.3f, 0.1f, 1f, Quality.IRON),
    "launcher_c" to Pair(RocketLauncherC::class.java, 1f, 0.3f, 0.1f, 1f, Quality.GOLDEN),
	  "meetboy" to Pair(MeetBoy::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Achievements.UNLOCK_MEETBOY),
    "dendy" to Pair(Dendy::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Achievements.UNLOCK_DENDY),
	  "poison_potion" to Pair(PoisonPotion::class.java, 0f, 1f, 1f, 1f, Quality.WOODEN),
    "poison_ring" to Pair(PoisonRing::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "metal_ring" to Pair(MetalRing::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "fire_ring" to Pair(FireRing::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "thorn_ring" to Pair(ThornRing::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "fortune_ring" to Pair(FortuneRing::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "ice_ring" to Pair(IceRing::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "blue_boomerang" to Pair(BlueBoomerang::class.java, 1f, 0.7f, 0.1f, 1f, Quality.WOODEN),
    "magic_mushroom" to Pair(MagicMushroom::class.java, 1f, 1f, 1f, 1f, Quality.IRON),
    "isaac_head" to Pair(IsaacHead::class.java, 1f, 0.3f, 0.1f, 1f, Quality.IRON, Achievements.UNLOCK_ISAAC_HEAD),
    "fire_flower" to Pair(FireFlower::class.java, 1f, 1f, 1f, 1f, Quality.GOLDEN),
    "backpack" to Pair(Backpack::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN, Achievements.UNLOCK_BACKPACK),
    "blood_ring" to Pair(BloodRing::class.java, 1f, 1f, 1f, 1f, Quality.IRON),
    "claymore_a" to Pair(ClaymoreA::class.java, 1f, 1f, 0.3f, 0.1f, Quality.WOODEN),
    "claymore_b" to Pair(ClaymoreB::class.java, 1f, 1f, 0.3f, 0.1f, Quality.IRON),
    "claymore_c" to Pair(ClaymoreC::class.java, 1f, 1f, 0.3f, 0.1f, Quality.GOLDEN),
    "goo" to Pair(GooOrbital::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "bumbo" to Pair(Bumbo::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "gold_ring" to Pair(GoldRing::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Achievements.UNLOCK_GOLD_RING),
    "vampire_ring" to Pair(VampireRing::class.java, 1f, 1f, 1f, 1f, Quality.IRON),
    "vvvvv" to Pair(VVVVV::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN, Achievements.UNLOCK_VVVVV),
    "jelly" to Pair(JellyOrbital::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "obsidian_boots" to Pair(ObsidianBoots::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "campfire_in_a_bottle" to Pair(CampfireInABottle::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "antidote" to Pair(Antidote::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "fire_extinguisher" to Pair(FireExtinguisher::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "broken_orbital" to Pair(BrokenOrbital::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "vampire_orbital" to Pair(VampireOrbital::class.java, 1f, 1f, 1f, 1f, Quality.IRON),
    "lightsaber_a" to Pair(LightsaberA::class.java, 1f, 1f, 0.3f, 0.11f, Quality.WOODEN),
    "lightsaber_b" to Pair(LightsaberB::class.java, 1f, 1f, 0.3f, 0.1f, Quality.IRON),
    "lightsaber_c" to Pair(LightsaberC::class.java, 1f, 1f, 0.3f, 0.1f, Quality.GOLDEN),
    "lightsaber_d" to Pair(LightsaberD::class.java, 1f, 1f, 0.3f, 0.1f, Quality.GOLDEN),
    "chicken_sword" to Pair(ChickenSword::class.java, 1f, 1f, 0.3f, 0.1f, Quality.GOLDEN),
    "shovel" to Pair(Shovel::class.java, 1f, 1f, 0.3f, 0.1f, Quality.GOLDEN),
    "magic_shield" to Pair(MagicShield::class.java, 1f, 1f, 1f, 1f, Quality.IRON),
    "cobalt_shield" to Pair(CobaltShield::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "cross" to Pair(Cross::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "spectacles" to Pair(Spectacles::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Achievements.UNLOCK_SPECTACLES),
    "diamond" to Pair(Diamond::class.java, 1f, 1f, 1f, 1f, Quality.GOLDEN, Achievements.UNLOCK_DIAMOND),
    "penetration_rune" to Pair(PenetrationRune::class.java, 1f, 1f, 1f, 1f, Quality.IRON),
    "map" to Pair(Map::class.java, 1f, 1f, 1f, 1f, Quality.IRON),
    "map_greenprints" to Pair(MapGreenprints::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "meetboy_axe" to Pair(MeetboyAxe::class.java, 1f, 0.3f, 0.3f, 1f, Quality.GOLDEN, Achievements.UNLOCK_MEETBOY_AXE),
    "star" to Pair(Star::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Achievements.UNLOCK_STAR),
    "blue_watch" to Pair(BlueWatch::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "stopwatch" to Pair(StopWatch::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Achievements.UNLOCK_STOP_WATCH),
    "the_eye" to Pair(TheEye::class.java, 1f, 1f, 1f, 1f, Quality.GOLDEN),
    "lucky_cube" to Pair(LuckyCube::class.java, 1f, 1f, 1f, 1f, Quality.IRON),
    "fortune_armor" to Pair(FortuneArmor::class.java, 1f, 1f, 1f, 1f, Quality.IRON),
    "stop_and_play" to Pair(StopAndPlay::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "spear_a" to Pair(SpearA::class.java, 0.3f, 1f, 0.1f, 0.3f, Quality.WOODEN),
    "spear_b" to Pair(SpearB::class.java, 0.3f, 1f, 0.1f, 0.3f, Quality.IRON),
    "spear_c" to Pair(SpearC::class.java, 0.3f, 1f, 0.1f, 0.3f, Quality.GOLDEN),
    "yoyo_a" to Pair(YoyoA::class.java, 1f, 1f, 0.1f, 0.8f, Quality.WOODEN),
    "yoyo_b" to Pair(YoyoB::class.java, 1f, 1f, 0.1f, 0.8f, Quality.IRON),
    "yoyo_c" to Pair(YoyoC::class.java, 1f, 1f, 0.1f, 0.8f, Quality.GOLDEN),
    "nano_orbital" to Pair(NanoOrbital::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "ammo_orbital" to Pair(AmmoOrbital::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Achievements.UNLOCK_AMMO_ORBITAL),
    "bomb_orbital" to Pair(BombOrbital::class.java, 1f, 1f, 1f, 1f, Quality.IRON),
    "lootpick" to Pair(Lootpick::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN, Achievements.UNLOCK_LOOTPICK),
    "sword_orbital" to Pair(SwordOrbital::class.java, 1f, 1f, 1f, 1f, Quality.GOLDEN, Achievements.UNLOCK_SWORD_ORBITAL),
    "dew_vial" to Pair(DewVial::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Achievements.UNLOCK_DEW_VIAL),
    "challenge_rune" to Pair(ChallengeRune::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "luck_rune" to Pair(LuckRune::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "stoneheart_rune" to Pair(StoneHeartRune::class.java, 1f, 1f, 1f, 1f, Quality.GOLDEN),
    "rage_rune" to Pair(RageRune::class.java, 1f, 1f, 1f, 1f, Quality.GOLDEN),
    "gravity_booster" to Pair(GravityBooster::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "wings" to Pair(Wings::class.java, 1f, 1f, 1f, 1f, Quality.GOLDEN, Achievements.UNLOCK_WINGS),
    "throwing_a" to Pair(ThrowingDaggerA::class.java, 1f, 0.3f, 0.1f, 1f, Quality.WOODEN),
    "throwing_b" to Pair(ThrowingDaggerB::class.java, 1f, 0.3f, 0.1f, 1f, Quality.IRON),
    "throwing_c" to Pair(ThrowingDaggerC::class.java, 1f, 0.3f, 0.1f, 1f, Quality.GOLDEN),
    "fire_bombs" to Pair(FireBombs::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "ice_bombs" to Pair(IceBombs::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "poison_bombs" to Pair(PoisonBombs::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "protective_band" to Pair(ProtectiveBand::class.java, 0f, 1f, 1f, 1f, Quality.WOODEN),
    "shovel_a" to Pair(ShovelA::class.java, 1f, 1f, 0.3f, 0.1f, Quality.WOODEN),
    "shovel_b" to Pair(ShovelB::class.java, 1f, 1f, 0.3f, 0.1f, Quality.IRON),
    "shovel_c" to Pair(ShovelC::class.java, 1f, 1f, 0.3f, 0.1f, Quality.GOLDEN),
    "pickaxe_a" to Pair(PickaxeA::class.java, 1f, 1f, 0.3f, 0.1f, Quality.WOODEN),
    "pickaxe_b" to Pair(PickaxeB::class.java, 1f, 1f, 0.3f, 0.1f, Quality.WOODEN),
    "pickaxe_c" to Pair(PickaxeC::class.java, 1f, 1f, 0.3f, 0.1f, Quality.GOLDEN),
    "pickaxe_d" to Pair(PickaxeD::class.java, 1f, 1f, 0.3f, 0.1f, Quality.GOLDEN),
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
    "blue_bomb" to Pair(BlueBomb::class.java, 1f, 0f, 1f, 0f, Quality.WOODEN),
    "mana_knife" to Pair(ManaKnife::class.java, 1f, 0f, 1f, 0f, Quality.WOODEN),
    "halo" to Pair(Halo::class.java, 1f, 1f, 1f, 1f, Quality.GOLDEN, Achievements.UNLOCK_HALO),
    "blue_book" to Pair(BlueBook::class.java, 1f, 0f, 1f, 0f, Quality.WOODEN),
    "red_book" to Pair(RedBook::class.java, 1f, 0f, 1f, 0f, Quality.WOODEN),
    "yellow_book" to Pair(YellowBook::class.java, 1f, 0f, 1f, 0f, Quality.IRON),
    "green_book" to Pair(GreenBook::class.java, 1f, 0f, 1f, 0f, Quality.GOLDEN),
    "arcane_shield" to Pair(ManaShield::class.java, 1f, 0.3f, 1f, 0.3f, Quality.WOODEN),
    "flying_star" to Pair(FlyingStar::class.java, 1f, 0f, 1f, 0f, Quality.WOODEN),
    "diamond_sword" to Pair(DiamondSword::class.java, 1f, 1f, 0.3f, 0.1f, Quality.GOLDEN, Achievements.UNLOCK_DIAMOND_SWORD),
    "blood_crown" to Pair(BloodCrown::class.java, 1f, 1f, 1f, 1f, Quality.GOLDEN, Achievements.UNLOCK_BLOOD_CROWN),
    "mana_boots" to Pair(ManaBoots::class.java, 1f, 0f, 1f, 0f, Quality.IRON),
    "damage_emblem" to Pair(DamageEmblem::class.java, 1f, 1f, 1f, 1f, Quality.GOLDEN),
    "defense_emblem" to Pair(DefenseEmblem::class.java, 1f, 1f, 1f, 1f, Quality.GOLDEN),
    "arcane_battery" to Pair(ArcaneBattery::class.java, 1f, 0f, 1f, 0f, Quality.WOODEN),
    "blue_coin" to Pair(BlueCoin::class.java, 1f, 0f, 1f, 0f, Quality.WOODEN),
    "blue_heart" to Pair(BlueHeart::class.java, 1f, 0f, 1f, 0f, Quality.WOODEN),
    "black_heart" to Pair(BlackHeart::class.java, 1f, 1f, 1f, 1f, Quality.GOLDEN, Achievements.UNLOCK_BLACK_HEART),
    "clock_heart" to Pair(ClockHeart::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Achievements.UNLOCK_CLOCK_HEART),
    "aim_book" to Pair(HomingBook::class.java, 1f, 0.1f, 1f, 0.3f, Quality.IRON),
    "triple_book" to Pair(TripleShotBook::class.java, 1f, 0.1f, 1f, 0.3f, Quality.IRON),
    "wall_book" to Pair(MagicWallBook::class.java, 1f, 0.1f, 1f, 0.3f, Quality.IRON),
    "slow_book" to Pair(SlowBook::class.java, 1f, 0.1f, 1f, 0.3f, Quality.GOLDEN),
    "fast_book" to Pair(FastBook::class.java, 1f, 0.1f, 1f, 0.3f, Quality.WOODEN),
    "crazy_book" to Pair(CrazyBook::class.java, 1f, 1f, 1f, 0.3f, Quality.IRON),
    "crash_book" to Pair(SuperCrazyBook::class.java, 0.3f, 0.1f, 1f, 0.1f, Quality.GOLDEN, Achievements.UNLOCK_CRASH_BOOK),
    "demage_emblem" to Pair(DemageEmblem::class.java, 1f, 1f, 1f, 1f, Quality.IRON),
    "apple" to Pair(Apple::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "mana_bottle" to Pair(ManaInABottle::class.java, 1f, 0f, 1f, 0f, Quality.WOODEN),
    "bread" to Pair(Bread::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
    "mana_emblem" to Pair(ManaEmblem::class.java, 1f, 0f, 1f, 0f, Quality.WOODEN),
    "bronze_shotgun" to Pair(BronzeShotgun::class.java, 1f, 0.3f, 0.3f, 1f, Quality.IRON),
    "back_gun" to Pair(BackGun::class.java, 1f, 10.3f, 0.1f, 1f, Quality.IRON),
    "kotling_gun" to Pair(KotlingGun::class.java, 0.1f, 0.3f, 0.1f, 1f, Quality.WOODEN, Achievements.UNLOCK_KOTLING_GUN),
    "machine_gun" to Pair(MachineGun::class.java, 1f, 0.3f, 0.1f, 1f, Quality.WOODEN),
    "revolver" to Pair(Revolver::class.java, 1f, 0.3f, 0.1f, 1f, Quality.WOODEN),
    "triple_machine_gun" to Pair(TripleMachineGun::class.java, 1f, 0.3f, 0.3f, 1f, Quality.GOLDEN),
	  "aim" to Pair(Aim::class.java, 1f, 0f, 0.3f, 1f, Quality.IRON),
	  "cursed_aim" to Pair(CursedAim::class.java, 1f, 0f, 0.3f, 1f, Quality.IRON),
	  "zoom" to Pair(Zoom::class.java, 1f,  0.3f, 0.3f, 1f, Quality.WOODEN),
	  "riffle" to Pair(Riffle::class.java, 1f, 0.3f, 0.1f, 1f, Quality.IRON),
	  "chopper" to Pair(Chopper::class.java, 1f, 0.3f, 0.1f, 1f, Quality.GOLDEN),
	  "pistol" to Pair(Pistol::class.java, 1f, 0.3f, 0.1f, 1f, Quality.WOODEN),
	  "headshot" to Pair(HeadshotGun::class.java, 1f, 0.3f, 0.1f, 1f, Quality.IRON),
	  "snowgun" to Pair(SnowGun::class.java, 1f, 0.3f, 0.1f, 1f, Quality.IRON),
	  "hammer" to Pair(Hammer::class.java, 1f, 0.3f, 0.1f, 1f, Quality.IRON),
	  "money_printer" to Pair(MoneyPrinter::class.java, 1f, 0.3f, 0.1f, 1f, Quality.IRON, Achievements.UNLOCK_MONEY_PRINTER),
	  "reload_rune" to Pair(ReloadRune::class.java, 1f, 0.3f, 0.1f, 1f, Quality.GOLDEN),
		"izu" to Pair(Izu::class.java, 1f, 0.3f, 0f, 1f, Quality.GOLDEN),
	  "old_manual" to Pair(OldManual::class.java, 1f, 0f, 0f, 1f, Quality.WOODEN),
	  "ammo_holder" to Pair(AmmoHolder::class.java, 1f, 0f, 0f, 1f, Quality.GOLDEN),
	  "lucky_bullet" to Pair(LuckyBullet::class.java, 1f, 0f, 0f, 1f, Quality.GOLDEN),
	  "smart_bullet" to Pair(SmartBullet::class.java, 1f, 0f, 0f, 1f, Quality.IRON),
	  "rng_bullet" to Pair(RngBullet::class.java, 1f, 0f, 0f, 1f, Quality.IRON),
	  "time_bullet" to Pair(TimeBullet::class.java, 1f, 0f, 0f, 1f, Quality.GOLDEN),
	  "big_bullet" to Pair(BigBullet::class.java, 1f, 0f, 0f, 1f, Quality.WOODEN),
	  "red_balloon" to Pair(RedBalloon::class.java, 1f, 1f, 1f, 1f, Quality.IRON),
	  "sale" to Pair(ShopSale::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Achievements.UNLOCK_SALE),
	  "toxic_flask" to Pair(ToxicFlask::class.java, 1f, 1f, 0.3f, 1f, Quality.WOODEN),
	  "mimic_totem" to Pair(MimicTotem::class.java, 1f, 1f, 1f, 1f, Quality.IRON, Achievements.UNLOCK_MIMIC_TOTEM),
	  "mimic_summoner" to Pair(MimicSummoner::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN, Achievements.UNLOCK_MIMIC_SUMMONER),
	  "fire_boots" to Pair(FireBoots::class.java, 1f, 1f, 1f, 1f, Quality.IRON),
	  "upgrade" to Pair(ScrollOfUpgrade::class.java, 0f, 1f, 1f, 1f, Quality.GOLDEN, Achievements.UPGRADE),
	  "ice_skates" to Pair(IceSkates::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
	  "scissors" to Pair(Scissors::class.java, 1f, 1f, 1f, 1f, Quality.WOODEN),
	  "elemental_ring" to Pair(ElementalRing::class.java, 1f, 1f, 1f, 1f, Quality.IRON),
	  "lava_bucket" to Pair(LavaBucket::class.java, 0.2f, 1f, 1f, 1f, Quality.WOODEN),
	  "water_bucket" to Pair(WaterBucket::class.java, 0.2f, 1f, 1f, 1f, Quality.WOODEN),
	  "tech_eye" to Pair(TechEye::class.java, 2f, 1f, 1f, 1f, Quality.IRON),
	  "eco_wand" to Pair(EcoWand::class.java, 1f, 0f, 1f, 0.1f, Quality.WOODEN)
  )

  val modItems = mutableMapOf<String, Item>()
}