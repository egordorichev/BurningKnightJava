package org.rexcellentgames.burningknight.entity.pool.item;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.accessory.equipable.*;
import org.rexcellentgames.burningknight.entity.item.consumable.food.Apple;
import org.rexcellentgames.burningknight.entity.item.consumable.food.Bread;
import org.rexcellentgames.burningknight.entity.item.consumable.food.ManaInABottle;
import org.rexcellentgames.burningknight.entity.item.pet.Bumbo;
import org.rexcellentgames.burningknight.entity.item.reference.*;
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

		// Items
	}

	private static void addMage() {
		// Weapons

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

		// Items

	}
}