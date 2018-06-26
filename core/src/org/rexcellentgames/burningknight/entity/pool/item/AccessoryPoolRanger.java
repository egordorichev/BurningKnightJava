package org.rexcellentgames.burningknight.entity.pool.item;

import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.accessory.equipable.Aim;
import org.rexcellentgames.burningknight.entity.item.accessory.equipable.CursedAim;
import org.rexcellentgames.burningknight.entity.item.accessory.equipable.LaserAim;
import org.rexcellentgames.burningknight.entity.pool.Pool;

public class AccessoryPoolRanger extends Pool<Item> {
	public static AccessoryPoolRanger instance = new AccessoryPoolRanger();

	public AccessoryPoolRanger() {
		add(LaserAim.class, 1f);
		add(Aim.class, 1f);
		add(CursedAim.class, 1f);
	}
}