package org.rexellentgames.dungeon.entity.item

import org.rexellentgames.dungeon.entity.item.accessory.equipable.*
import org.rexellentgames.dungeon.entity.item.accessory.hat.*
import org.rexellentgames.dungeon.entity.item.autouse.Backpack
import org.rexellentgames.dungeon.entity.item.consumable.food.Bread
import org.rexellentgames.dungeon.entity.item.consumable.food.Chicken
import org.rexellentgames.dungeon.entity.item.consumable.plant.Cabbage
import org.rexellentgames.dungeon.entity.item.consumable.potion.*
import org.rexellentgames.dungeon.entity.item.consumable.seed.CabbageSeed
import org.rexellentgames.dungeon.entity.item.consumable.seed.LightroomSeed
import org.rexellentgames.dungeon.entity.item.consumable.seed.PotionGrassSeed
import org.rexellentgames.dungeon.entity.item.consumable.spell.GhostLeaver
import org.rexellentgames.dungeon.entity.item.consumable.spell.ManaSpell
import org.rexellentgames.dungeon.entity.item.consumable.spell.SpellOfDamage
import org.rexellentgames.dungeon.entity.item.consumable.spell.SpellOfTeleportation
import org.rexellentgames.dungeon.entity.item.key.KeyA
import org.rexellentgames.dungeon.entity.item.key.KeyB
import org.rexellentgames.dungeon.entity.item.key.KeyC
import org.rexellentgames.dungeon.entity.item.pet.Bumbo
import org.rexellentgames.dungeon.entity.item.pet.LibGDX
import org.rexellentgames.dungeon.entity.item.pet.Pico8
import org.rexellentgames.dungeon.entity.item.pet.StrawberryPet
import org.rexellentgames.dungeon.entity.item.pet.orbital.BrokenOrbital
import org.rexellentgames.dungeon.entity.item.pet.orbital.GooOrbital
import org.rexellentgames.dungeon.entity.item.pet.orbital.JellyOrbital
import org.rexellentgames.dungeon.entity.item.pet.orbital.VampireOrbital
import org.rexellentgames.dungeon.entity.item.reference.*
import org.rexellentgames.dungeon.entity.item.weapon.Guitar
import org.rexellentgames.dungeon.entity.item.weapon.axe.AxeA
import org.rexellentgames.dungeon.entity.item.weapon.axe.AxeB
import org.rexellentgames.dungeon.entity.item.weapon.axe.AxeC
import org.rexellentgames.dungeon.entity.item.weapon.axe.AxeD
import org.rexellentgames.dungeon.entity.item.weapon.bow.BowA
import org.rexellentgames.dungeon.entity.item.weapon.bow.BowB
import org.rexellentgames.dungeon.entity.item.weapon.bow.BowC
import org.rexellentgames.dungeon.entity.item.weapon.bow.arrows.ArrowA
import org.rexellentgames.dungeon.entity.item.weapon.bow.arrows.ArrowB
import org.rexellentgames.dungeon.entity.item.weapon.bow.arrows.ArrowC
import org.rexellentgames.dungeon.entity.item.weapon.dagger.DaggerA
import org.rexellentgames.dungeon.entity.item.weapon.dagger.DaggerB
import org.rexellentgames.dungeon.entity.item.weapon.dagger.DaggerC
import org.rexellentgames.dungeon.entity.item.weapon.gun.CKGun
import org.rexellentgames.dungeon.entity.item.weapon.gun.GunA
import org.rexellentgames.dungeon.entity.item.weapon.gun.GunB
import org.rexellentgames.dungeon.entity.item.weapon.gun.GunC
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.BulletA
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.BulletB
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.BulletC
import org.rexellentgames.dungeon.entity.item.weapon.laser.LaserGun
import org.rexellentgames.dungeon.entity.item.weapon.magic.DefenseBook
import org.rexellentgames.dungeon.entity.item.weapon.magic.FireBook
import org.rexellentgames.dungeon.entity.item.weapon.magic.NoteBook
import org.rexellentgames.dungeon.entity.item.weapon.rocketlauncher.RocketLauncherA
import org.rexellentgames.dungeon.entity.item.weapon.rocketlauncher.RocketLauncherB
import org.rexellentgames.dungeon.entity.item.weapon.rocketlauncher.RocketLauncherC
import org.rexellentgames.dungeon.entity.item.weapon.rocketlauncher.rocket.RocketA
import org.rexellentgames.dungeon.entity.item.weapon.sword.*
import org.rexellentgames.dungeon.entity.item.weapon.sword.butcher.ButcherA
import org.rexellentgames.dungeon.entity.item.weapon.sword.butcher.ButcherB
import org.rexellentgames.dungeon.entity.item.weapon.sword.butcher.ButcherC
import org.rexellentgames.dungeon.entity.item.weapon.sword.claymore.ClaymoreA
import org.rexellentgames.dungeon.entity.item.weapon.sword.claymore.ClaymoreB
import org.rexellentgames.dungeon.entity.item.weapon.sword.claymore.ClaymoreC
import org.rexellentgames.dungeon.entity.item.weapon.sword.morning.MorningStarA
import org.rexellentgames.dungeon.entity.item.weapon.sword.morning.MorningStarB
import org.rexellentgames.dungeon.entity.item.weapon.sword.morning.MorningStarC
import org.rexellentgames.dungeon.entity.item.weapon.sword.starwars.LightsaberA
import org.rexellentgames.dungeon.entity.item.weapon.sword.starwars.LightsaberB

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
		"fire_book" to FireBook::class,
		"note_book" to NoteBook::class,
		"arrow_a" to ArrowA::class,
		"arrow_b" to ArrowB::class,
		"arrow_c" to ArrowC::class,
		"bow_a" to BowA::class,
		"bow_b" to BowB::class,
		"bow_c" to BowC::class,
		"guitar" to Guitar::class,
		"lamp" to Lamp::class,
		"defense_book" to DefenseBook::class,
		"ghost" to GhostLeaver::class,
		"worm_hole" to WormHole::class,
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
		"lightsaber_c" to LightsaberA::class,
		"lightsaber_d" to LightsaberB::class,
		"chicken_sword" to ChickenSword::class,
		"shovel" to Shovel::class,
		"pico8" to Pico8::class,
		"libgdx" to LibGDX::class,
		"magic_shield" to MagicShield::class,
        "cobalt_shield" to CobaltShield::class,
        "cross" to Cross::class
    )
}