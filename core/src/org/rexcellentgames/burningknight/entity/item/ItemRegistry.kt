package org.rexcellentgames.burningknight.entity.item

import org.rexcellentgames.burningknight.entity.item.accessory.equipable.*
import org.rexcellentgames.burningknight.entity.item.accessory.hat.*
import org.rexcellentgames.burningknight.entity.item.autouse.*
import org.rexcellentgames.burningknight.entity.item.autouse.Map
import org.rexcellentgames.burningknight.entity.item.consumable.food.Apple
import org.rexcellentgames.burningknight.entity.item.consumable.food.Bread
import org.rexcellentgames.burningknight.entity.item.consumable.food.ManaInABottle
import org.rexcellentgames.burningknight.entity.item.consumable.potion.*
import org.rexcellentgames.burningknight.entity.item.consumable.spell.ManaSpell
import org.rexcellentgames.burningknight.entity.item.consumable.spell.SpellOfDamage
import org.rexcellentgames.burningknight.entity.item.consumable.spell.SpellOfTeleportation
import org.rexcellentgames.burningknight.entity.item.key.KeyA
import org.rexcellentgames.burningknight.entity.item.key.KeyB
import org.rexcellentgames.burningknight.entity.item.key.KeyC
import org.rexcellentgames.burningknight.entity.item.pet.Bumbo
import org.rexcellentgames.burningknight.entity.item.pet.LibGDX
import org.rexcellentgames.burningknight.entity.item.pet.Pico8
import org.rexcellentgames.burningknight.entity.item.pet.StrawberryPet
import org.rexcellentgames.burningknight.entity.item.pet.orbital.*
import org.rexcellentgames.burningknight.entity.item.reference.*
import org.rexcellentgames.burningknight.entity.item.weapon.Guitar
import org.rexcellentgames.burningknight.entity.item.weapon.HeadshotGun
import org.rexcellentgames.burningknight.entity.item.weapon.axe.*
import org.rexcellentgames.burningknight.entity.item.weapon.bow.BowA
import org.rexcellentgames.burningknight.entity.item.weapon.bow.BowB
import org.rexcellentgames.burningknight.entity.item.weapon.bow.BowC
import org.rexcellentgames.burningknight.entity.item.weapon.bow.arrows.ArrowA
import org.rexcellentgames.burningknight.entity.item.weapon.bow.arrows.ArrowB
import org.rexcellentgames.burningknight.entity.item.weapon.bow.arrows.ArrowC
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.DaggerA
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.DaggerB
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.DaggerC
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.ManaKnife
import org.rexcellentgames.burningknight.entity.item.weapon.gun.*
import org.rexcellentgames.burningknight.entity.item.weapon.gun.shotgun.BronzeShotgun
import org.rexcellentgames.burningknight.entity.item.weapon.laser.LaserGun
import org.rexcellentgames.burningknight.entity.item.weapon.magic.*
import org.rexcellentgames.burningknight.entity.item.weapon.magic.book.*
import org.rexcellentgames.burningknight.entity.item.weapon.rocketlauncher.RocketLauncherA
import org.rexcellentgames.burningknight.entity.item.weapon.rocketlauncher.RocketLauncherB
import org.rexcellentgames.burningknight.entity.item.weapon.rocketlauncher.RocketLauncherC
import org.rexcellentgames.burningknight.entity.item.weapon.rocketlauncher.rocket.RocketA
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
import org.rexcellentgames.burningknight.entity.item.weapon.yoyo.YoyoA
import org.rexcellentgames.burningknight.entity.item.weapon.yoyo.YoyoB
import org.rexcellentgames.burningknight.entity.item.weapon.yoyo.YoyoC

object ItemRegistry {
  val items = mapOf(
    "dagger_a" to DaggerA::class.java,
    "dagger_b" to DaggerB::class.java,
    "dagger_c" to DaggerC::class.java,
    "sword_a" to SwordA::class.java,
    "sword_b" to SwordB::class.java,
    "sword_c" to SwordC::class.java,
    "butcher_a" to ButcherA::class.java,
    "butcher_b" to ButcherB::class.java,
    "butcher_c" to ButcherC::class.java,
    "morning_a" to MorningStarA::class.java,
    "morning_b" to MorningStarB::class.java,
    "morning_c" to MorningStarC::class.java,
    "axe_a" to AxeA::class.java,
    "axe_b" to AxeB::class.java,
    "axe_c" to AxeC::class.java,
    "axe_d" to AxeD::class.java,
    "the_sword" to TheSword::class.java,
    "gold" to Gold::class.java,
    "healing_potion" to HealingPotion::class.java,
    "fire_potion" to FirePotion::class.java,
    "invis_potion" to InvisibilityPotion::class.java,
    "regen_potion" to RegenerationPotion::class.java,
    "poison_potion" to PoisonPotion::class.java,
    "tp_spell" to SpellOfTeleportation::class.java,
    "damage_spell" to SpellOfDamage::class.java,
    "bomb" to Bomb::class.java,
    "arrow_a" to ArrowA::class.java,
    "arrow_b" to ArrowB::class.java,
    "arrow_c" to ArrowC::class.java,
    "bow_a" to BowA::class.java,
    "bow_b" to BowB::class.java,
    "bow_c" to BowC::class.java,
    "guitar" to Guitar::class.java,
    "lamp" to Lamp::class.java,
    "mana_spell" to ManaSpell::class.java,
    "key_a" to KeyA::class.java,
    "key_b" to KeyB::class.java,
    "key_c" to KeyC::class.java,
    "compass" to Compass::class.java,
    "ushanka_hat" to UshankaHat::class.java,
    "moai_hat" to MoaiHat::class.java,
    "knight_hat" to KnightHat::class.java,
    "fungi_hat" to FungiHat::class.java,
    "shroom_hat" to ShroomHat::class.java,
    "dunce_hat" to DunceHat::class.java,
    "rave_hat" to RaveHat::class.java,
    "skull_hat" to SkullHat::class.java,
    "coboi_hat" to CoboiHat::class.java,
    "valkyre_hat" to ValkyreHat::class.java,
    "gold_hat" to GoldHat::class.java,
    "ruby_hat" to RubyHat::class.java,
    "viking_hat" to VikingHat::class.java,
    "launcher_a" to RocketLauncherA::class.java,
    "launcher_b" to RocketLauncherB::class.java,
    "launcher_c" to RocketLauncherC::class.java,
    "rocket_a" to RocketA::class.java,
    "meetboy" to MeetBoy::class.java,
    "dendy" to Dendy::class.java,
    "poison_ring" to PoisonRing::class.java,
    "metal_ring" to MetalRing::class.java,
    "fire_ring" to FireRing::class.java,
    "thorn_ring" to ThornRing::class.java,
    "fortune_ring" to FortuneRing::class.java,
    "ice_ring" to IceRing::class.java,
    "blue_boomerang" to BlueBoomerang::class.java,
    "magic_mushroom" to MagicMushroom::class.java,
    "isaac_head" to IsaacHead::class.java,
    "fire_flower" to FireFlower::class.java,
    "gravelord_sword" to GravelordSword::class.java,
    "star_cannon" to StarCannon::class.java,
    "switch" to Switch::class.java,
    "backpack" to Backpack::class.java,
    "blood_ring" to BloodRing::class.java,
    "claymore_a" to ClaymoreA::class.java,
    "claymore_b" to ClaymoreB::class.java,
    "claymore_c" to ClaymoreC::class.java,
    "celeste" to StrawberryPet::class.java,
    "goo" to GooOrbital::class.java,
    "bumbo" to Bumbo::class.java,
    "gold_ring" to GoldRing::class.java,
    "vampire_ring" to VampireRing::class.java,
    "laser_gun" to LaserGun::class.java,
    "vvvvv" to VVVVV::class.java,
    "jelly" to JellyOrbital::class.java,
    "obsidian_boots" to ObsidianBoots::class.java,
    "campfire_in_a_bottle" to CampfireInABottle::class.java,
    "antidote" to Antidote::class.java,
    "fire_extinguisher" to FireExtinguisher::class.java,
    "broken_orbital" to BrokenOrbital::class.java,
    "vampire_orbital" to VampireOrbital::class.java,
    "lightsaber_a" to LightsaberA::class.java,
    "lightsaber_b" to LightsaberB::class.java,
    "lightsaber_c" to LightsaberC::class.java,
    "lightsaber_d" to LightsaberD::class.java,
    "chicken_sword" to ChickenSword::class.java,
    "shovel" to Shovel::class.java,
    "pico8" to Pico8::class.java,
    "libgdx" to LibGDX::class.java,
    "magic_shield" to MagicShield::class.java,
    "cobalt_shield" to CobaltShield::class.java,
    "cross" to Cross::class.java,
    "spectacles" to Spectacles::class.java,
    "diamond" to Diamond::class.java,
    "penetration_rune" to PenetrationRune::class.java,
    "map" to Map::class.java,
    "map_greenprints" to MapGreenprints::class.java,
    "meetboy_axe" to MeetboyAxe::class.java,
    "star" to Star::class.java,
    "blue_watch" to BlueWatch::class.java,
    "stopwatch" to StopWatch::class.java,
    "the_eye" to TheEye::class.java,
    "lucky_cube" to LuckyCube::class.java,
    "fortune_armor" to FortuneArmor::class.java,
    "stop_and_play" to StopAndPlay::class.java,
    "spear_a" to SpearA::class.java,
    "spear_b" to SpearB::class.java,
    "spear_c" to SpearC::class.java,
    "yoyo_a" to YoyoA::class.java,
    "yoyo_b" to YoyoB::class.java,
    "yoyo_c" to YoyoC::class.java,
    "nano_orbital" to NanoOrbital::class.java,
    "ammo_orbital" to AmmoOrbital::class.java,
    "bomb_orbital" to BombOrbital::class.java,
    "lootpick" to Lootpick::class.java,
    "sword_orbital" to SwordOrbital::class.java,
    "dew_vial" to DewVial::class.java,
    "challenge_rune" to ChallengeRune::class.java,
    "luck_rune" to LuckRune::class.java,
    "stoneheart_rune" to StoneHeartRune::class.java,
    "rage_rune" to RageRune::class.java,
    "gravity_booster" to GravityBooster::class.java,
    "wings" to Wings::class.java,
    "throwing_a" to ThrowingDaggerA::class.java,
    "throwing_b" to ThrowingDaggerB::class.java,
    "throwing_c" to ThrowingDaggerC::class.java,
    "fire_bombs" to FireBombs::class.java,
    "ice_bombs" to IceBombs::class.java,
    "poison_bombs" to PoisonBombs::class.java,
    "protective_band" to ProtectiveBand::class.java,
    "shovel_a" to ShovelA::class.java,
    "shovel_b" to ShovelB::class.java,
    "shovel_c" to ShovelC::class.java,
    "pickaxe_a" to PickaxeA::class.java,
    "pickaxe_b" to PickaxeB::class.java,
    "pickaxe_c" to PickaxeC::class.java,
    "pickaxe_d" to PickaxeD::class.java,
    "missile_wand" to MagicMissileWand::class.java,
    "laser_aim" to LaserAim::class.java,
    "poison_wand" to PoisonWand::class.java,
    "fire_wand" to FireWand::class.java,
    "ice_wand" to IceWand::class.java,
    "waterbolt" to Waterbolt::class.java,
    "firebolt" to Firebolt::class.java,
    "mana_ring" to ManaRing::class.java,
    "mana_up" to StarOnAString::class.java,
    "mana_bottle" to ManaBottle::class.java,
    "mana_heart" to ManaHeart::class.java,
    "blue_bomb" to BlueBomb::class.java,
    "mana_knife" to ManaKnife::class.java,
    "halo" to Halo::class.java,
    "blue_book" to BlueBook::class.java,
    "red_book" to RedBook::class.java,
    "yellow_book" to YellowBook::class.java,
    "green_book" to GreenBook::class.java,
    "arcane_shield" to ManaShield::class.java,
    "flying_star" to FlyingStar::class.java,
    "diamond_sword" to DiamondSword::class.java,
    "blood_crown" to BloodCrown::class.java,
    "mana_boots" to ManaBoots::class.java,
    "damage_emblem" to DamageEmblem::class.java,
    "defense_emblem" to DefenseEmblem::class.java,
    "arcane_battery" to ArcaneBattery::class.java,
    "blue_coin" to BlueCoin::class.java,
    "blue_heart" to BlueHeart::class.java,
    "black_heart" to BlackHeart::class.java,
    "clock_heart" to ClockHeart::class.java,
    "aim_book" to HomingBook::class.java,
    "triple_book" to TripleShotBook::class.java,
    "wall_book" to MagicWallBook::class.java,
    "slow_book" to SlowBook::class.java,
    "fast_book" to FastBook::class.java,
    "crazy_book" to CrazyBook::class.java,
    "crash_book" to SuperCrazyBook::class.java,
    "demage_emblem" to DemageEmblem::class.java,
    "apple" to Apple::class.java,
    "mana_bottle" to ManaInABottle::class.java,
    "bread" to Bread::class.java,
    "mana_emblem" to ManaEmblem::class.java,
    "bronze_shotgun" to BronzeShotgun::class.java,
    "back_gun" to BackGun::class.java,
    "bad_gun" to BadGun::class.java,
    "ck_gun" to CKGun::class.java,
    "kotling_gun" to KotlingGun::class.java,
    "machine_gun" to MachineGun::class.java,
    "revolver" to Revolver::class.java,
    "triple_machine_gun" to TripleMachineGun::class.java,
	  "aim" to Aim::class.java,
	  "cursed_aim" to CursedAim::class.java,
	  "ball" to Ball::class.java,
	  "zoom" to Zoom::class.java,
	  "riffle" to Riffle::class.java,
	  "chopper" to Chopper::class.java,
	  "pistol" to Pistol::class.java,
	  "headshot" to HeadshotGun::class.java,
	  "snowgun" to SnowGun::class.java,
	  "hammer" to Hammer::class.java,
	  "money_printer" to MoneyPrinter::class.java,
	  "reload_rune" to ReloadRune::class.java,
		"izu" to Izu::class.java,
	  "old_manual" to OldManual::class.java,
	  "ammo_holder" to AmmoHolder::class.java,
	  "lucky_bullet" to LuckyBullet::class.java,
	  "smart_bullet" to SmartBullet::class.java,
	  "rng_bullet" to RngBullet::class.java
  )

  val modItems = mutableMapOf<String, Item>()
}