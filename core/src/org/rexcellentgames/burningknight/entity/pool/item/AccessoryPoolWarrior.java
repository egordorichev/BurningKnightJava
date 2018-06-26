package org.rexcellentgames.burningknight.entity.pool.item;

import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.accessory.equipable.StopAndPlay;
import org.rexcellentgames.burningknight.entity.pool.Pool;

public class AccessoryPoolWarrior extends Pool<Item> {
	public static AccessoryPoolWarrior instance = new AccessoryPoolWarrior();

	public AccessoryPoolWarrior() {
		add(StopAndPlay.class, 1f);
	}
}