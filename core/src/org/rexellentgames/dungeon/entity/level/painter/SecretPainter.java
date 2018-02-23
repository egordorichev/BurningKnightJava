package org.rexellentgames.dungeon.entity.level.painter;

import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.features.Room;
import org.rexellentgames.dungeon.entity.level.Terrain;

public class SecretPainter extends Painter {
	public static void paint(Level level, Room room) {
		fill(level, room.left + 1, room.top + 1, room.getWidth() - 2, room.getHeight() - 2, Terrain.WATER);
		// todo: fill it
		
		for (Door door : room.getConnected().values()) {
			door.setType(Door.Type.SECRET);
		}
	}
}