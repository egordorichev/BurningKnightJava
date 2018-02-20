package org.rexellentgames.dungeon.entity.level.painter;

import org.rexellentgames.dungeon.entity.level.Door;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Room;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.util.Point;
import org.rexellentgames.dungeon.util.Random;

public class TunnelPainter extends Painter {
	public static void paint(Level level, Room room) {
		Point c = room.getCenter();
		short floor = Terrain.FLOOR;

		if (room.getWidth() > room.getHeight() || (room.getWidth() == room.getHeight() && Random.newInt(2) == 0)) {
			int from = room.right - 1;
			int to = room.left + 1;

			for (Door door : room.getConnected().values()) {
				int step = door.y < c.y ? +1 : -1;

				if (door.x == room.left) {
					from = room.left + 1;

					for (int i = door.y; i != c.y; i += step) {
						level.set(from, i, floor);
					}
				} else if (door.x == room.right) {
					to = room.right - 1;

					for (int i = door.y; i != c.y; i += step) {
						level.set(to, i, floor);
					}
				} else {
					if (door.x < from) {
						from = door.x;
					}

					if (door.x > to) {
						to = door.x;
					}

					for (int i = door.y + step; i != c.y; i += step) {
						level.set(door.x, i, floor);
					}
				}
			}

			for (int i = from; i <= to; i++) {
				level.set(i, c.y, floor);
			}
		} else {
			int from = room.bottom - 1;
			int to = room.top + 1;

			for (Door door : room.getConnected().values()) {
				int step = door.x < c.x ? +1 : -1;

				if (door.y == room.top) {
					from = room.top + 1;

					for (int i = door.x; i != c.x; i += step) {
						level.set(i, from, floor);
					}
				} else if (door.y == room.bottom) {
					to = room.bottom - 1;

					for (int i = door.x; i != c.x; i += step) {
						level.set(i, to, floor);
					}
				} else {
					if (door.y < from) {
						from = door.y;
					}

					if (door.y > to) {
						to = door.y;
					}

					for (int i = door.x + step; i != c.x; i += step) {
						level.set(i, door.y, floor);
					}
				}
			}

			for (int i = from; i <= to; i++) {
				level.set(c.x, i, floor);
			}
		}

		for (Door door : room.getConnected().values()) {
			door.setType(Door.Type.TUNNEL);
		}
	}
}