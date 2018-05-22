package org.rexellentgames.dungeon.entity.item.pool;

import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.reference.BlueBoomerang;
import org.rexellentgames.dungeon.entity.item.reference.GravelordSword;
import org.rexellentgames.dungeon.entity.item.reference.IsaacHead;
import org.rexellentgames.dungeon.entity.item.reference.StarCannon;
import org.rexellentgames.dungeon.entity.item.weapon.axe.AxeA;
import org.rexellentgames.dungeon.entity.item.weapon.axe.AxeB;
import org.rexellentgames.dungeon.entity.item.weapon.axe.AxeC;
import org.rexellentgames.dungeon.entity.item.weapon.axe.AxeD;
import org.rexellentgames.dungeon.entity.item.weapon.bow.BowA;
import org.rexellentgames.dungeon.entity.item.weapon.bow.BowB;
import org.rexellentgames.dungeon.entity.item.weapon.bow.BowC;
import org.rexellentgames.dungeon.entity.item.weapon.dagger.DaggerA;
import org.rexellentgames.dungeon.entity.item.weapon.dagger.DaggerB;
import org.rexellentgames.dungeon.entity.item.weapon.dagger.DaggerC;
import org.rexellentgames.dungeon.entity.item.weapon.gun.GunA;
import org.rexellentgames.dungeon.entity.item.weapon.gun.GunB;
import org.rexellentgames.dungeon.entity.item.weapon.gun.GunC;
import org.rexellentgames.dungeon.entity.item.weapon.magic.DefenseBook;
import org.rexellentgames.dungeon.entity.item.weapon.magic.FireBook;
import org.rexellentgames.dungeon.entity.item.weapon.magic.NoteBook;
import org.rexellentgames.dungeon.entity.item.weapon.sword.SwordA;
import org.rexellentgames.dungeon.entity.item.weapon.sword.SwordB;
import org.rexellentgames.dungeon.entity.item.weapon.sword.SwordC;
import org.rexellentgames.dungeon.entity.item.weapon.sword.butcher.ButcherA;
import org.rexellentgames.dungeon.entity.item.weapon.sword.butcher.ButcherB;
import org.rexellentgames.dungeon.entity.item.weapon.sword.butcher.ButcherC;
import org.rexellentgames.dungeon.entity.item.weapon.sword.morning.MorningStarA;
import org.rexellentgames.dungeon.entity.item.weapon.sword.morning.MorningStarB;
import org.rexellentgames.dungeon.entity.item.weapon.sword.morning.MorningStarC;

public class ShopWeaponPool extends Pool<Item> {
	public static ShopWeaponPool instance = new ShopWeaponPool();

	public ShopWeaponPool() {
		addRanged();
		addMagic();
		addMelee();
	}

	private void addRanged() {
		add(BowA.class, 1f);
		add(BowB.class, 1f);
		add(BowC.class, 1f);
		add(GunA.class, 1f);
		add(GunB.class, 1f);
		add(GunC.class, 1f);
		add(AxeA.class, 1f);
		add(AxeB.class, 1f);
		add(AxeC.class, 1f);
		add(AxeD.class, 1f);
		add(BlueBoomerang.class, 1f);
		add(IsaacHead.class, 1f);
		add(StarCannon.class, 1f);
	}

	private void addMagic() {
		add(NoteBook.class, 1f);
		add(FireBook.class, 1f);
		add(DefenseBook.class, 1f);
	}

	private void addMelee() {
		add(DaggerA.class, 1f);
		add(DaggerB.class, 1f);
		add(DaggerC.class, 1f);
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
	}
}