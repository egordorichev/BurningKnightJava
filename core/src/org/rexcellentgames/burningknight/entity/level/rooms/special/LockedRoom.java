package org.rexcellentgames.burningknight.entity.level.rooms.special;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.features.Door;

public class LockedRoom extends SpecialRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.LOCKED);
		}
	}
}