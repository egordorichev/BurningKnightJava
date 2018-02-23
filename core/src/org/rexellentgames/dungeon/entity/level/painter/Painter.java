package org.rexellentgames.dungeon.entity.level.painter;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.features.Room;

public class Painter {
	public static void fill(Level level, Room room, short v) {
		fill(level, room.left, room.top, room.getWidth(), room.getHeight(), v);
	}

	public static void fill(Level level, int x, int y, int w, int h, short v) {
		for (int xx = x; xx <= x + w; xx++) {
			for (int yy = y; yy <= y + h; yy++) {
				level.set(xx, yy, v);
			}
		}
	}
}