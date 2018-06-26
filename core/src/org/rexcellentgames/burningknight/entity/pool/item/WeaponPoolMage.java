package org.rexcellentgames.burningknight.entity.pool.item;

import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.*;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.book.*;
import org.rexcellentgames.burningknight.entity.pool.Pool;

public class WeaponPoolMage extends Pool<Item> {
	public static WeaponPoolMage instance = new WeaponPoolMage();

	public WeaponPoolMage() {
		add(HomingBook.class, 1f);
		add(Firebolt.class, 1f);
		add(Waterbolt.class, 1f);
		add(PoisonWand.class, 1f);
		add(FireWand.class, 1f);
		add(IceWand.class, 1f);
		add(MagicMissileWand.class, 1f);
		add(TripleShotBook.class, 1f);
		add(MagicWallBook.class, 1f);
		add(SlowBook.class, 1f);
		add(FastBook.class, 1f);
		add(CrazyBook.class, 0.5f);
		add(SuperCrazyBook.class, 0.2f);
	}
}