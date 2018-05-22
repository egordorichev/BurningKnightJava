package org.rexellentgames.dungeon.entity.item.pool;

import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.accessory.equipable.*;
import org.rexellentgames.dungeon.entity.item.reference.Dendy;
import org.rexellentgames.dungeon.entity.item.reference.MagicMushroom;
import org.rexellentgames.dungeon.entity.item.reference.MeetBoy;
import org.rexellentgames.dungeon.entity.item.reference.Switch;

public class AccessoryPool extends Pool<Item> {
	public static AccessoryPool instance = new AccessoryPool();

	public AccessoryPool() {
		add(FireFlower.class, 0.3f);
		add(FireRing.class, 1f);
		add(IceRing.class, 1f);
		add(ThornRing.class, 1f);
		add(MetalRing.class, 1f);
		add(PoisonRing.class, 1f);
		add(MeetBoy.class, 1f);
		add(Dendy.class, 1f);
		add(MagicMushroom.class, 1f);
		add(Switch.class, 1f);
	}
}