package org.rexellentgames.dungeon.entity.level.painter;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Room;
import org.rexellentgames.dungeon.entity.level.Terrain;

public class ExitPainter extends Painter {
	public static void paint(Level level, Room room) {
		fill(level, room, Terrain.WALL);
		fill(level, room.left + 1, room.top + 1, room.getWidth() - 2, room.getHeight() - 2, Terrain.GRASS);
	}
}