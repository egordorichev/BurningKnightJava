package org.rexcellentgames.burningknight.entity.pool.item;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.accessory.equipable.*;
import org.rexcellentgames.burningknight.entity.item.consumable.food.Apple;
import org.rexcellentgames.burningknight.entity.item.consumable.food.Bread;
import org.rexcellentgames.burningknight.entity.item.pet.Bumbo;
import org.rexcellentgames.burningknight.entity.item.reference.*;
import org.rexcellentgames.burningknight.entity.item.weapon.axe.AxeC;
import org.rexcellentgames.burningknight.entity.item.weapon.axe.AxeD;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.BowC;
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.DaggerC;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.*;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.*;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.book.*;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.SwordC;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.butcher.ButcherC;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.morning.MorningStarC;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.starwars.LightsaberC;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.starwars.LightsaberD;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.tool.PickaxeC;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.tool.PickaxeD;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.tool.ShovelC;
import org.rexcellentgames.burningknight.entity.item.weapon.throwing.ThrowingDaggerB;
import org.rexcellentgames.burningknight.entity.item.weapon.throwing.ThrowingDaggerC;
import org.rexcellentgames.burningknight.entity.item.weapon.yoyo.YoyoC;
import org.rexcellentgames.burningknight.entity.pool.Pool;

public class GoldChestPool extends Pool<Item> {
	public static GoldChestPool weapons = new GoldChestPool();
	public static GoldChestPool items = new GoldChestPool();

	public static void init() {
		weapons.clear();
		items.clear();

		switch (Player.instance.type) {
			case WARRIOR: addWarrior(); break;
			case WIZARD: addMage(); break;
			case RANGER: addRanger(); break;
		}

		addAll();
	}

	private static void addAll() {
		// Weapons
		// - null

		// Items

		items.add(MeetBoy.class, 1f);
		items.add(Dendy.class, 1f);
		items.add(MagicMushroom.class, 1f);
		items.add(Switch.class, 0.1f);
		items.add(Bumbo.class, 0.5f);
		items.add(Spectacles.class, 0.1f);
		items.add(Diamond.class, 0.5f);
		items.add(Star.class, 1f);
		items.add(LuckyCube.class, 1f);
		items.add(FortuneArmor.class, 1f);
		items.add(ChallengeRune.class, 1f);
		items.add(LuckRune.class, 1f);
		items.add(StoneHeartRune.class, 1f);
		items.add(RageRune.class, 1f);
		items.add(Wings.class, 1f);
		items.add(Halo.class, 1f);
		items.add(BloodCrown.class, 1f);
		items.add(DamageEmblem.class, 1f);
		items.add(DefenseEmblem.class, 1f);
		items.add(BlackHeart.class, 1f);
		items.add(ClockHeart.class, 1f);
		items.add(Apple.class, 1f);
		items.add(Bread.class, 1f);
		items.add(RedBalloon.class, 1f);
		items.add(MimicTotem.class, 1f);
		items.add(MimicSummoner.class, 1f);
	}

	private static void addWarrior() {
		// Weapons

	  weapons.add(SwordC.class, 1f);
	  weapons.add(ButcherC.class, 1f);
	  weapons.add(MorningStarC.class, 1f);
	  weapons.add(LightsaberC.class, 1f);
	  weapons.add(LightsaberD.class, 0.5f);
	  weapons.add(YoyoC.class, 1f);
	  weapons.add(ThrowingDaggerB.class, 1f);
	  weapons.add(DaggerC.class, 1f);
	  weapons.add(ThrowingDaggerC.class, 1f);
	  weapons.add(PickaxeC.class, 1f);
	  weapons.add(PickaxeD.class, 0.5f);
	  weapons.add(ShovelC.class, 1f);
	  weapons.add(AxeC.class, 1f);
	  weapons.add(AxeD.class, 0.5f);
		
		// Items
		
		items.add(StopAndPlay.class, 1f);
	}

	private static void addMage() {
		// Weapons

	  weapons.add(HomingBook.class, 1f);
	  weapons.add(Firebolt.class, 1f);
	  weapons.add(Waterbolt.class, 1f);
	  weapons.add(TripleShotBook.class, 1f);
	  weapons.add(SlowBook.class, 1f);
	  weapons.add(CrazyBook.class, 0.5f);
	  weapons.add(SuperCrazyBook.class, 0.2f);
	  
		// Items

	  items.add(BlueBook.class, 1f);
	  items.add(GreenBook.class, 1f);
	  items.add(RedBook.class, 1f);
	  items.add(YellowBook.class, 1f);
	  items.add(ManaShield.class, 1f);
	  items.add(ArcaneBattery.class, 1f);
	  items.add(BlueCoin.class, 1f);
	  items.add(BlueHeart.class, 1f);
	}

	private static void addRanger() {
		// Weapons

	  weapons.add(BowC.class, 1f);
	  weapons.add(AxeC.class, 1f);
	  weapons.add(AxeD.class, 0.5f);
	  weapons.add(MachineGun.class, 1f);
	  weapons.add(TripleMachineGun.class, 1f);
	  weapons.add(ThrowingDaggerC.class, 1f);
	  weapons.add(SnowGun.class, 1f);
	  weapons.add(KotlingGun.class, 0.1f);
	  weapons.add(Chopper.class, 1f);
	  weapons.add(Riffle.class, 1f);
	  weapons.add(Hammer.class, 1f);
		
		// Items

	  items.add(LaserAim.class, 1f);
	  items.add(Aim.class, 1f);
	  items.add(Zoom.class, 1f);
	  items.add(AmmoHolder.class, 1f);
	  items.add(LuckyBullet.class, 1f);
	  items.add(BigBullet.class, 1f);
	}
}