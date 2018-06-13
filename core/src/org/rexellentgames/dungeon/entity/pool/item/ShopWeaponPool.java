package org.rexellentgames.dungeon.entity.pool.item;

import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.reference.BlueBoomerang;
import org.rexellentgames.dungeon.entity.item.reference.GravelordSword;
import org.rexellentgames.dungeon.entity.item.reference.IsaacHead;
import org.rexellentgames.dungeon.entity.item.reference.StarCannon;
import org.rexellentgames.dungeon.entity.item.weapon.axe.*;
import org.rexellentgames.dungeon.entity.item.weapon.bow.BowA;
import org.rexellentgames.dungeon.entity.item.weapon.bow.BowB;
import org.rexellentgames.dungeon.entity.item.weapon.bow.BowC;
import org.rexellentgames.dungeon.entity.item.weapon.dagger.DaggerB;
import org.rexellentgames.dungeon.entity.item.weapon.dagger.DaggerC;
import org.rexellentgames.dungeon.entity.item.weapon.gun.BackGun;
import org.rexellentgames.dungeon.entity.item.weapon.gun.GunA;
import org.rexellentgames.dungeon.entity.item.weapon.gun.GunB;
import org.rexellentgames.dungeon.entity.item.weapon.gun.GunC;
import org.rexellentgames.dungeon.entity.item.weapon.magic.DefenseBook;
import org.rexellentgames.dungeon.entity.item.weapon.magic.FireBook;
import org.rexellentgames.dungeon.entity.item.weapon.magic.NoteBook;
import org.rexellentgames.dungeon.entity.item.weapon.sword.*;
import org.rexellentgames.dungeon.entity.item.weapon.sword.butcher.ButcherA;
import org.rexellentgames.dungeon.entity.item.weapon.sword.butcher.ButcherB;
import org.rexellentgames.dungeon.entity.item.weapon.sword.butcher.ButcherC;
import org.rexellentgames.dungeon.entity.item.weapon.sword.morning.MorningStarA;
import org.rexellentgames.dungeon.entity.item.weapon.sword.morning.MorningStarB;
import org.rexellentgames.dungeon.entity.item.weapon.sword.morning.MorningStarC;
import org.rexellentgames.dungeon.entity.item.weapon.sword.starwars.LightsaberA;
import org.rexellentgames.dungeon.entity.item.weapon.sword.starwars.LightsaberB;
import org.rexellentgames.dungeon.entity.item.weapon.sword.starwars.LightsaberC;
import org.rexellentgames.dungeon.entity.item.weapon.sword.starwars.LightsaberD;
import org.rexellentgames.dungeon.entity.item.weapon.yoyo.YoyoA;
import org.rexellentgames.dungeon.entity.item.weapon.yoyo.YoyoB;
import org.rexellentgames.dungeon.entity.item.weapon.yoyo.YoyoC;
import org.rexellentgames.dungeon.entity.pool.Pool;

public class ShopWeaponPool extends Pool<Item> {
	public static ShopWeaponPool instance = new ShopWeaponPool();

	public ShopWeaponPool() {
		switch (Player.instance.getType()) {
			case WARRIOR: addWarrior(); break;
			case WIZARD: addMagic(); break;
			case ARCHER: addArcher(); break;
			case GUNNER: addGunner(); break;
			case ROGUE: addRogue(); break;
		}
	}

	private void addGunner() {
		add(GunA.class, 1f);
		add(GunB.class, 1f);
		add(GunC.class, 1f);
		add(BackGun.class, 1f);
		add(IsaacHead.class, 1f);
		add(StarCannon.class, 1f);
	}

	private void addArcher() {
		add(BowA.class, 1f);
		add(BowB.class, 1f);
		add(BowC.class, 1f);
		add(AxeA.class, 1f);
		add(AxeB.class, 1f);
		add(AxeC.class, 1f);
		add(AxeD.class, 1f);
		add(MeetboyAxe.class, 1f);
		add(BlueBoomerang.class, 1f);
	}

	private void addRogue() {
		add(DaggerB.class, 1f);
		add(DaggerC.class, 1f);
	}

	private void addMagic() {
		add(NoteBook.class, 1f);
		add(FireBook.class, 1f);
		add(DefenseBook.class, 1f);
	}

	private void addWarrior() {
		add(SwordA.class, 1f);
		add(SwordB.class, 1f);
		add(SwordC.class, 1f);
		add(ButcherA.class, 1f);
		add(ButcherB.class, 1f);
		add(ButcherC.class, 1f);
		add(MorningStarA.class, 1f);
		add(MorningStarB.class, 1f);
		add(MorningStarC.class, 1f);
		add(GravelordSword.class, 1f);
		add(LightsaberA.class, 1f);
		add(LightsaberB.class, 1f);
		add(LightsaberC.class, 1f);
		add(LightsaberD.class, 1f);
		add(ChickenSword.class, 1f);
		add(Shovel.class, 1f);
		add(YoyoA.class, 1f);
		add(YoyoB.class, 1f);
		add(YoyoC.class, 1f);
	}
}