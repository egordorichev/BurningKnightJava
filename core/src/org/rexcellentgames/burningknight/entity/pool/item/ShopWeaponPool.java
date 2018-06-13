package org.rexcellentgames.burningknight.entity.pool.item;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.weapon.axe.MeetboyAxe;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.GunB;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.GunC;
import org.rexcellentgames.burningknight.entity.item.weapon.yoyo.YoyoA;
import org.rexcellentgames.burningknight.entity.item.weapon.yoyo.YoyoB;
import org.rexcellentgames.burningknight.entity.item.weapon.yoyo.YoyoC;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.reference.BlueBoomerang;
import org.rexcellentgames.burningknight.entity.item.reference.GravelordSword;
import org.rexcellentgames.burningknight.entity.item.reference.IsaacHead;
import org.rexcellentgames.burningknight.entity.item.reference.StarCannon;
import org.rexcellentgames.burningknight.entity.item.weapon.axe.*;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.BowA;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.BowB;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.BowC;
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.DaggerB;
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.DaggerC;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.BackGun;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.GunA;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.GunB;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.GunC;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.DefenseBook;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.FireBook;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.NoteBook;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.*;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.butcher.ButcherA;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.butcher.ButcherB;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.butcher.ButcherC;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.morning.MorningStarA;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.morning.MorningStarB;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.morning.MorningStarC;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.starwars.LightsaberA;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.starwars.LightsaberB;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.starwars.LightsaberC;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.starwars.LightsaberD;
import org.rexcellentgames.burningknight.entity.item.weapon.yoyo.YoyoA;
import org.rexcellentgames.burningknight.entity.item.weapon.yoyo.YoyoB;
import org.rexcellentgames.burningknight.entity.item.weapon.yoyo.YoyoC;
import org.rexcellentgames.burningknight.entity.pool.Pool;

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