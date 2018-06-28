package org.rexcellentgames.burningknight.entity.pool.item;

import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.accessory.equipable.*;
import org.rexcellentgames.burningknight.entity.pool.Pool;

public class AccessoryPoolRanger extends Pool<Item> {
	public static AccessoryPoolRanger instance = new AccessoryPoolRanger();

	public AccessoryPoolRanger() {
		add(LaserAim.class, 1f);
		add(Aim.class, 1f);
		add(CursedAim.class, 1f);
		add(Zoom.class, 1f);
		add(OldManual.class, 1f);
		add(AmmoHolder.class, 1f);
		add(LuckyBullet.class, 1f);
		add(BigBullet.class, 1f);
	}
}