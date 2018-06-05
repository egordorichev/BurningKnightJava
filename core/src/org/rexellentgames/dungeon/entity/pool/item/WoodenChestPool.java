package org.rexellentgames.dungeon.entity.pool.item;

import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.reference.Dendy;
import org.rexellentgames.dungeon.entity.item.reference.MeetBoy;
import org.rexellentgames.dungeon.entity.item.reference.Switch;
import org.rexellentgames.dungeon.entity.pool.Pool;

public class WoodenChestPool extends Pool<Item> {
	public static WoodenChestPool instance = new WoodenChestPool();

	public WoodenChestPool() {
		add(MeetBoy.class, 1f);
		add(Dendy.class, 1f);
		add(Switch.class, 1f);

		addFrom(AccessoryPool.instance);
		addFrom(ShopWeaponPool.instance);
		addFrom(ShopHatPool.instance);
	}
}