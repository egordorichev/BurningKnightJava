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
import org.rexcellentgames.burningknight.entity.pool.Pool;

public class IronChestPool extends Pool<Item> {
	public static IronChestPool weapons = new IronChestPool();
	public static IronChestPool items = new IronChestPool();

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

	private static void addMage() {
		// Weapons

		// Items

	}

	private static void addRanger() {
		// Weapons

		// Items

	}
}