package org.rexellentgames.dungeon.entity.pool.item;

import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.pool.Pool;

public class WoodenChestPool extends Pool<Item> {
	public static WoodenChestPool instance = new WoodenChestPool();

	public WoodenChestPool() {
		addFrom(AccessoryPool.instance);
		addFrom(ShopWeaponPool.instance);
		addFrom(ShopHatPool.instance);
	}
}