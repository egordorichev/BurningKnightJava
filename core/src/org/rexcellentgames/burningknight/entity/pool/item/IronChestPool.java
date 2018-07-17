package org.rexcellentgames.burningknight.entity.pool.item;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.accessory.equipable.*;
import org.rexcellentgames.burningknight.entity.item.autouse.ManaHeart;
import org.rexcellentgames.burningknight.entity.item.consumable.food.Apple;
import org.rexcellentgames.burningknight.entity.item.consumable.food.Bread;
import org.rexcellentgames.burningknight.entity.item.consumable.food.ManaInABottle;
import org.rexcellentgames.burningknight.entity.item.pet.Bumbo;
import org.rexcellentgames.burningknight.entity.item.pet.orbital.*;
import org.rexcellentgames.burningknight.entity.item.reference.*;
import org.rexcellentgames.burningknight.entity.item.weapon.axe.AxeB;
import org.rexcellentgames.burningknight.entity.item.weapon.axe.MeetboyAxe;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.BowB;
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.DaggerB;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.*;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.*;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.book.*;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.ChickenSword;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.DiamondSword;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.Shovel;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.SwordB;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.butcher.ButcherB;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.morning.MorningStarB;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.starwars.LightsaberB;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.tool.PickaxeB;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.tool.ShovelB;
import org.rexcellentgames.burningknight.entity.item.weapon.throwing.ThrowingDaggerB;
import org.rexcellentgames.burningknight.entity.item.weapon.yoyo.YoyoB;
import org.rexcellentgames.burningknight.entity.pool.Pool;

public class IronChestPool extends Pool<Item> {
	public static IronChestPool weapons = new IronChestPool();
	public static IronChestPool items = new IronChestPool();

	public static void init() {
		weapons.clear();
		items.clear();
		Player.Type type = Player.Type.WARRIOR;

		if (Player.instance != null) {
			type = Player.instance.type;
		}

		switch (type) {
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
		items.add(Bumbo.class, 0.5f);
		items.add(GooOrbital.class, 1f);
		items.add(VampireRing.class, 1f);
		items.add(VampireOrbital.class, 1f);
		items.add(NanoOrbital.class, 1f);
		items.add(AmmoOrbital.class, 1f);
		items.add(BombOrbital.class, 1f);
		items.add(Spectacles.class, 0.1f);
		items.add(Star.class, 1f);
		items.add(PenetrationRune.class, 1f);
		items.add(BlueWatch.class, 1f);
		items.add(StopWatch.class, 1f);
		items.add(TheEye.class, 1f);
		items.add(LuckyCube.class, 1f);
		items.add(FortuneArmor.class, 1f);
		items.add(SwordOrbital.class, 1f);
		items.add(DewVial.class, 1f);
		items.add(RageRune.class, 1f);
		items.add(GravityBooster.class, 1f);
		items.add(FireBombs.class, 1f);
		items.add(PoisonBombs.class, 1f);
		items.add(IceBombs.class, 1f);
		items.add(Halo.class, 1f);
		items.add(BloodCrown.class, 1f);
		items.add(BlackHeart.class, 1f);
		items.add(ClockHeart.class, 1f);
		items.add(Apple.class, 1f);
		items.add(Bread.class, 1f);
	}

	private static void addWarrior() {
		// Weapons

	  weapons.add(SwordB.class, 1f);
	  weapons.add(ButcherB.class, 1f);
	  weapons.add(MorningStarB.class, 1f);
	  weapons.add(LightsaberB.class, 1f);
	  weapons.add(ChickenSword.class, 1f);
	  weapons.add(Shovel.class, 1f);
	  weapons.add(YoyoB.class, 1f);
	  weapons.add(ThrowingDaggerB.class, 1f);
	  weapons.add(DaggerB.class, 1f);
	  weapons.add(ThrowingDaggerB.class, 1f);
	  weapons.add(PickaxeB.class, 1f);
	  weapons.add(ShovelB.class, 1f);
	  weapons.add(AxeB.class, 1f);
	  weapons.add(MeetboyAxe.class, 1);
	  weapons.add(DiamondSword.class, 0.6f);
		
		// Items
		
		items.add(StopAndPlay.class, 1f);
	}

	private static void addMage() {
		// Weapons

	  weapons.add(HomingBook.class, 1f);
	  weapons.add(Firebolt.class, 1f);
	  weapons.add(Waterbolt.class, 1f);
	  weapons.add(FireWand.class, 1f);
	  weapons.add(IceWand.class, 1f);
	  weapons.add(MagicMissileWand.class, 1f);
	  weapons.add(TripleShotBook.class, 1f);
	  weapons.add(MagicWallBook.class, 1f);
	  weapons.add(SlowBook.class, 1f);
	  weapons.add(CrazyBook.class, 0.5f);

		// Items

		items.add(ManaBottle.class, 1f);
		items.add(ManaHeart.class, 1f);
		items.add(BlueBook.class, 1f);
		items.add(GreenBook.class, 1f);
		items.add(ArcaneBattery.class, 1f);
		items.add(BlueCoin.class, 1f);
		items.add(BlueHeart.class, 1f);
		items.add(ManaInABottle.class, 1f);
	}

	private static void addRanger() {
		// Weapons

	  weapons.add(BowB.class, 1f);
	  weapons.add(AxeB.class, 1f);
	  weapons.add(MeetboyAxe.class, 1f);
	  weapons.add(MachineGun.class, 1f);
	  weapons.add(ThrowingDaggerB.class, 1f);
	  weapons.add(SnowGun.class, 1f);
		weapons.add(KotlingGun.class, 0.1f);
		weapons.add(Pistol.class, 1f);
		weapons.add(Chopper.class, 1f);
	  weapons.add(Hammer.class, 1f);
		
		// Items
		
	  items.add(LaserAim.class, 1f);
	  items.add(Aim.class, 1f);
	  items.add(CursedAim.class, 1f);
	  items.add(Zoom.class, 1f);
	  items.add(OldManual.class, 1f);
	}
}