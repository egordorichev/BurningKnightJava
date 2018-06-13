package org.rexcellentgames.burningknight.entity.pool.item;

import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.pool.Pool;

public class WoodenChestPool extends Pool<Item> {
	public static WoodenChestPool instance = new WoodenChestPool();

	public WoodenChestPool() {
		addFrom(AccessoryPool.instance);
		addFrom(ShopWeaponPool.instance);
		addFrom(ShopHatPool.instance);
	}
}