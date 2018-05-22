package org.rexellentgames.dungeon.entity.level.rooms.special;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.item.key.KeyB;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.features.Door;

public class LockedRoom extends SpecialRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.LOCKED);
		}

		Dungeon.level.itemsToSpawn.add(new KeyB());
	}
}