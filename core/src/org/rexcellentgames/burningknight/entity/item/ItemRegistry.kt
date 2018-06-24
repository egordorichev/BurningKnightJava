package org.rexcellentgames.burningknight.entity.item

import org.rexcellentgames.burningknight.entity.item.accessory.equipable.*
import org.rexcellentgames.burningknight.entity.item.accessory.hat.*
import org.rexcellentgames.burningknight.entity.item.autouse.Backpack
import org.rexcellentgames.burningknight.entity.item.autouse.ManaHeart
import org.rexcellentgames.burningknight.entity.item.autouse.Map
import org.rexcellentgames.burningknight.entity.item.autouse.MapGreenprints
import org.rexcellentgames.burningknight.entity.item.consumable.food.Bread
import org.rexcellentgames.burningknight.entity.item.consumable.food.Chicken
import org.rexcellentgames.burningknight.entity.item.consumable.plant.Cabbage
import org.rexcellentgames.burningknight.entity.item.consumable.potion.*
import org.rexcellentgames.burningknight.entity.item.consumable.seed.CabbageSeed
import org.rexcellentgames.burningknight.entity.item.consumable.seed.LightroomSeed
import org.rexcellentgames.burningknight.entity.item.consumable.seed.PotionGrassSeed
import org.rexcellentgames.burningknight.entity.item.consumable.spell.GhostLeaver
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
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.BulletA
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.BulletB
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.BulletC
import org.rexcellentgames.burningknight.entity.item.weapon.gun.shotgun.ShotgunA
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
            "dagger_a" to DaggerA::class,
            "dagger_b" to DaggerB::class,
            "dagger_c" to DaggerC::class,
            "sword_a" to SwordA::class,
            "sword_b" to SwordB::class,
            "sword_c" to SwordC::class,
            "butcher_a" to ButcherA::class,
            "butcher_b" to ButcherB::class,
            "butcher_c" to ButcherC::class,
            "morning_a" to MorningStarA::class,
            "morning_b" to MorningStarB::class,
            "morning_c" to MorningStarC::class,
            "axe_a" to AxeA::class,
            "axe_b" to AxeB::class,
            "axe_c" to AxeC::class,
            "axe_d" to AxeD::class,
            "gun_a" to GunA::class,
            "gun_b" to GunB::class,
            "gun_c" to GunC::class,
            "bullet_a" to BulletA::class,
            "bullet_b" to BulletB::class,
            "bullet_c" to BulletC::class,
            "the_sword" to TheSword::class,
            "gold" to Gold::class,
            "healing_potion" to HealingPotion::class,
            "sun_potion" to SunPotion::class,
            "fire_potion" to FirePotion::class,
            "invis_potion" to InvisibilityPotion::class,
            "speed_potion" to SpeedPotion::class,
            "regen_potion" to RegenerationPotion::class,
            "poison_potion" to PoisonPotion::class,
            "defense_potion" to DefensePotion::class,
            "tp_spell" to SpellOfTeleportation::class,
            "damage_spell" to SpellOfDamage::class,
            "bread" to Bread::class,
            "chicken" to Chicken::class,
            "bomb" to Bomb::class,
            "cabbage_seed" to CabbageSeed::class,
            "cabbage" to Cabbage::class,
            "lightroom_seed" to LightroomSeed::class,
            "potiongrass_seed" to PotionGrassSeed::class,
            "arrow_a" to ArrowA::class,
            "arrow_b" to ArrowB::class,
            "arrow_c" to ArrowC::class,
            "bow_a" to BowA::class,
            "bow_b" to BowB::class,
            "bow_c" to BowC::class,
            "guitar" to Guitar::class,
            "lamp" to Lamp::class,
            "ghost" to GhostLeaver::class,
            "mana_spell" to ManaSpell::class,
            "key_a" to KeyA::class,
            "key_b" to KeyB::class,
            "key_c" to KeyC::class,
            "compass" to Compass::class,
            "ushanka_hat" to UshankaHat::class,
            "moai_hat" to MoaiHat::class,
            "knight_hat" to KnightHat::class,
            "fungi_hat" to FungiHat::class,
            "shroom_hat" to ShroomHat::class,
            "dunce_hat" to DunceHat::class,
            "rave_hat" to RaveHat::class,
            "skull_hat" to SkullHat::class,
            "coboi_hat" to CoboiHat::class,
            "valkyre_hat" to ValkyreHat::class,
            "gold_hat" to GoldHat::class,
            "ruby_hat" to RubyHat::class,
            "viking_hat" to VikingHat::class,
            "launcher_a" to RocketLauncherA::class,
            "launcher_b" to RocketLauncherB::class,
            "launcher_c" to RocketLauncherC::class,
            "rocket_a" to RocketA::class,
            "ckgun" to CKGun::class,
            "meetboy" to MeetBoy::class,
            "dendy" to Dendy::class,
            "poison_ring" to PoisonRing::class,
            "metal_ring" to MetalRing::class,
            "fire_ring" to FireRing::class,
            "thorn_ring" to ThornRing::class,
            "fortune_ring" to FortuneRing::class,
            "ice_ring" to IceRing::class,
            "blue_boomerang" to BlueBoomerang::class,
            "magic_mushroom" to MagicMushroom::class,
            "isaac_head" to IsaacHead::class,
            "fire_flower" to FireFlower::class,
            "gravelord_sword" to GravelordSword::class,
            "star_cannon" to StarCannon::class,
            "switch" to Switch::class,
            "backpack" to Backpack::class,
            "blood_ring" to BloodRing::class,
            "claymore_a" to ClaymoreA::class,
            "claymore_b" to ClaymoreB::class,
            "claymore_c" to ClaymoreC::class,
            "celeste" to StrawberryPet::class,
            "goo" to GooOrbital::class,
            "bumbo" to Bumbo::class,
            "gold_ring" to GoldRing::class,
            "vampire_ring" to VampireRing::class,
            "laser_gun" to LaserGun::class,
            "vvvvv" to VVVVV::class,
            "jelly" to JellyOrbital::class,
            "obsidian_boots" to ObsidianBoots::class,
            "campfire_in_a_bottle" to CampfireInABottle::class,
            "antidote" to Antidote::class,
            "fire_extinguisher" to FireExtinguisher::class,
            "broken_orbital" to BrokenOrbital::class,
            "vampire_orbital" to VampireOrbital::class,
            "lightsaber_a" to LightsaberA::class,
            "lightsaber_b" to LightsaberB::class,
            "lightsaber_c" to LightsaberC::class,
            "lightsaber_d" to LightsaberD::class,
            "chicken_sword" to ChickenSword::class,
            "shovel" to Shovel::class,
            "pico8" to Pico8::class,
            "libgdx" to LibGDX::class,
            "magic_shield" to MagicShield::class,
            "cobalt_shield" to CobaltShield::class,
            "cross" to Cross::class,
            "spectacles" to Spectacles::class,
            "diamond" to Diamond::class,
            "penetration_rune" to PenetrationRune::class,
            "map" to Map::class,
            "map_greenprints" to MapGreenprints::class,
            "meetboy_axe" to MeetboyAxe::class,
            "star" to Star::class,
            "blue_watch" to BlueWatch::class,
            "stopwatch" to StopWatch::class,
            "the_eye" to TheEye::class,
            "lucky_cube" to LuckyCube::class,
            "fortune_armor" to FortuneArmor::class,
            "stop_and_play" to StopAndPlay::class,
            "spear_a" to SpearA::class,
            "spear_b" to SpearB::class,
            "spear_c" to SpearC::class,
            "yoyo_a" to YoyoA::class,
            "yoyo_b" to YoyoB::class,
            "yoyo_c" to YoyoC::class,
            "nano_orbital" to NanoOrbital::class,
            "ammo_orbital" to AmmoOrbital::class,
            "bomb_orbital" to BombOrbital::class,
            "lootpick" to Lootpick::class,
            "back_gun" to BackGun::class,
            "sword_orbital" to SwordOrbital::class,
            "dew_vial" to DewVial::class,
            "challenge_rune" to ChallengeRune::class,
            "luck_rune" to LuckRune::class,
            "stoneheart_rune" to StoneHeartRune::class,
            "rage_rune" to RageRune::class,
            "gravity_booster" to GravityBooster::class,
            "wings" to Wings::class,
            "shotgun_a" to ShotgunA::class,
            "throwing_a" to ThrowingDaggerA::class,
            "throwing_b" to ThrowingDaggerB::class,
            "throwing_c" to ThrowingDaggerC::class,
            "fire_bombs" to FireBombs::class,
            "ice_bombs" to IceBombs::class,
            "poison_bombs" to PoisonBombs::class,
            "protective_band" to ProtectiveBand::class,
            "shovel_a" to ShovelA::class,
            "shovel_b" to ShovelB::class,
            "shovel_c" to ShovelC::class,
            "pickaxe_a" to PickaxeA::class,
            "pickaxe_b" to PickaxeB::class,
            "pickaxe_c" to PickaxeC::class,
            "pickaxe_d" to PickaxeD::class,
            "missile_wand" to MagicMissileWand::class,
            "laser_aim" to LaserAim::class,
            "poison_wand" to PoisonWand::class,
            "fire_wand" to FireWand::class,
            "ice_wand" to IceWand::class,
            "waterbolt" to Waterbolt::class,
            "firebolt" to Firebolt::class,
            "mana_ring" to ManaRing::class,
            "mana_up" to StarOnAString::class,
            "mana_bottle" to ManaBottle::class,
            "mana_heart" to ManaHeart::class,
            "blue_bomb" to BlueBomb::class,
            "mana_knife" to ManaKnife::class,
            "halo" to Halo::class,
            "blue_book" to BlueBook::class,
            "red_book" to RedBook::class,
            "yellow_book" to YellowBook::class,
            "green_book" to GreenBook::class,
            "arcane_shield" to ManaShield::class,
	          "flying_star" to FlyingStar::class,
	          "diamond_sword" to DiamondSword::class,
	          "blood_crown" to BloodCrown::class,
	          "mana_boots" to ManaBoots::class,
	          "damage_emblem" to DamageEmblem::class,
	          "defense_emblem" to DefenseEmblem::class,
	          "arcane_battery" to ArcaneBattery::class,
	          "blue_coin" to BlueCoin::class,
	          "blue_heart" to BlueHeart::class,
	          "black_heart" to BlackHeart::class,
	          "clock_heart" to ClockHeart::class,
	          "aim_book" to HomingBook::class,
	          "triple_book" to TripleShotBook::class,
	          "wall_book" to MagicWallBook::class,
	          "slow_book" to SlowBook::class,
	          "fast_book" to FastBook::class,
	          "crazy_book" to CrazyBook::class,
	          "crash_book" to SuperCrazyBook::class
    )
}