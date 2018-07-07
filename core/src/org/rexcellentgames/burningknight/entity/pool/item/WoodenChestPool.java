package org.rexcellentgames.burningknight.entity.pool.item;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Compass;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.accessory.equipable.*;
import org.rexcellentgames.burningknight.entity.item.autouse.ManaHeart;
import org.rexcellentgames.burningknight.entity.item.consumable.food.ManaInABottle;
import org.rexcellentgames.burningknight.entity.item.pet.LibGDX;
import org.rexcellentgames.burningknight.entity.item.pet.Pico8;
import org.rexcellentgames.burningknight.entity.item.pet.StrawberryPet;
import org.rexcellentgames.burningknight.entity.item.pet.orbital.FlyingStar;
import org.rexcellentgames.burningknight.entity.item.pet.orbital.GooOrbital;
import org.rexcellentgames.burningknight.entity.item.pet.orbital.NanoOrbital;
import org.rexcellentgames.burningknight.entity.item.reference.BlueBoomerang;
import org.rexcellentgames.burningknight.entity.item.reference.Dendy;
import org.rexcellentgames.burningknight.entity.item.reference.MeetBoy;
import org.rexcellentgames.burningknight.entity.item.weapon.HeadshotGun;
import org.rexcellentgames.burningknight.entity.item.weapon.axe.AxeA;
import org.rexcellentgames.burningknight.entity.item.weapon.axe.MeetboyAxe;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.BowA;
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.DaggerA;
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.ManaKnife;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.*;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.shotgun.BronzeShotgun;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.FireWand;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.IceWand;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.MagicMissileWand;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.PoisonWand;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.book.FastBook;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.ChickenSword;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.Shovel;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.SwordA;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.butcher.ButcherA;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.morning.MorningStarA;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.starwars.LightsaberA;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.tool.PickaxeA;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.tool.ShovelA;
import org.rexcellentgames.burningknight.entity.item.weapon.throwing.ThrowingDaggerA;
import org.rexcellentgames.burningknight.entity.item.weapon.throwing.ToxicFlask;
import org.rexcellentgames.burningknight.entity.item.weapon.yoyo.YoyoA;
import org.rexcellentgames.burningknight.entity.pool.Pool;

public class WoodenChestPool extends Pool<Item> {
	public static WoodenChestPool weapons = new WoodenChestPool();
	public static WoodenChestPool items = new WoodenChestPool();

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

		items.add(FireFlower.class, 0.3f);
		items.add(StrawberryPet.class, 0.1f);
		items.add(Pico8.class, 0.1f);
		items.add(LibGDX.class, 0.1f);
		items.add(FireRing.class, 1f);
		items.add(IceRing.class, 1f);
		items.add(ThornRing.class, 1f);
		items.add(MetalRing.class, 1f);
		items.add(PoisonRing.class, 1f);
		items.add(BloodRing.class, 1f);
		items.add(MeetBoy.class, 1f);
		items.add(Dendy.class, 1f);
		items.add(FortuneRing.class, 1f);
		items.add(GoldRing.class, 1f);
		items.add(GooOrbital.class, 1f);
		items.add(VampireRing.class, 1f);
		items.add(NanoOrbital.class, 1f);
		items.add(FireExtinguisher.class, 1f);
		items.add(ObsidianBoots.class, 1f);
		items.add(CampfireInABottle.class, 1f);
		items.add(Antidote.class, 1f);
		items.add(CobaltShield.class, 1f);
		items.add(MagicShield.class, 1f);
		items.add(Compass.class, 1f);
		items.add(Halo.class, 1f);
	}

	private static void addWarrior() {
		// Weapons

		weapons.add(SwordA.class, 1f);
		weapons.add(ButcherA.class, 1f);
		weapons.add(MorningStarA.class, 1f);
		weapons.add(LightsaberA.class, 0.3f);
		weapons.add(ChickenSword.class, 1f);
		weapons.add(Shovel.class, 1f);
		weapons.add(YoyoA.class, 1f);
		weapons.add(ThrowingDaggerA.class, 1f);
		weapons.add(DaggerA.class, 1f);
		weapons.add(ThrowingDaggerA.class, 1f);
		weapons.add(PickaxeA.class, 1f);
		weapons.add(ShovelA.class, 1f);
		weapons.add(AxeA.class, 1f);
		weapons.add(MeetboyAxe.class, 0.4f);

		// Items

		items.add(StopAndPlay.class, 1f);
	}

	private static void addMage() {
		// Weapons

		weapons.add(PoisonWand.class, 1f);
		weapons.add(FireWand.class, 1f);
		weapons.add(IceWand.class, 1f);
		weapons.add(MagicMissileWand.class, 1f);
		weapons.add(FastBook.class, 1f);

		// Items

		items.add(ManaRing.class, 1f);
		items.add(ManaBottle.class, 1f);
		items.add(ManaHeart.class, 1f);
		items.add(ManaKnife.class, 1f);
		items.add(BlueBomb.class, 1f);
		items.add(BlueBook.class, 1f);
		items.add(FlyingStar.class, 1f);
		items.add(ManaShield.class, 1f);
		items.add(ManaBoots.class, 1f);
		items.add(ArcaneBattery.class, 1f);
		items.add(BlueCoin.class, 1f);
		items.add(ManaInABottle.class, 2f);
	}

	private static void addRanger() {
		// Weapons

		weapons.add(BowA.class, 1f);
		weapons.add(AxeA.class, 1f);
		weapons.add(MeetboyAxe.class, 1f);
		weapons.add(BlueBoomerang.class, 1f);
		weapons.add(Revolver.class, 1f);
		weapons.add(BackGun.class, 1f);
		weapons.add(ThrowingDaggerA.class, 1f);
		weapons.add(SnowGun.class, 1f);
		weapons.add(KotlingGun.class, 0.1f);
		weapons.add(HeadshotGun.class, 1f);
		weapons.add(Pistol.class, 1f);
		weapons.add(BronzeShotgun.class, 0.5f);
		weapons.add(ToxicFlask.class, 0.5f);

		// Items

		items.add(Aim.class, 1f);
		items.add(CursedAim.class, 1f);
		items.add(Zoom.class, 1f);
		items.add(OldManual.class, 1f);
		items.add(BigBullet.class, 1f);
	}
}