package org.rexellentgames.dungeon.entity.level.rooms.ladder;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.geometry.Point;
import org.rexellentgames.dungeon.util.geometry.Rect;

public class CircleEntranceRoom extends EntranceRoom {
	@Override
	public void paint(Level level) {
		byte f = Terrain.randomFloor();
		Painter.fill(level, this, Terrain.WALL);
		Painter.fillEllipse(level, this, 1, f);

		if (this.connected.size() == 0) {
			Log.error("Invalid connection room");
			return;
		}

		Rect c = getConnectionSpace();

		for (Door door : this.getConnected().values()) {
			Point start;
			Point mid;
			Point end;

			start = new Point(door);
			if (start.x == left) start.x++;
			else if (start.y == top) start.y++;
			else if (start.x == right) start.x--;
			else if (start.y == bottom) start.y--;

			int rightShift;
			int downShift;

			if (start.x < c.left) rightShift = (int) (c.left - start.x);
			else if (start.x > c.right) rightShift = (int) (c.right - start.x);
			else rightShift = 0;

			if (start.y < c.top) downShift = (int) (c.top - start.y);
			else if (start.y > c.bottom) downShift = (int) (c.bottom - start.y);
			else downShift = 0;

			// always goes inward first
			if (door.x == left || door.x == right) {
				mid = new Point(start.x + rightShift, start.y);
				end = new Point(mid.x, mid.y + downShift);

			} else {
				mid = new Point(start.x, start.y + downShift);
				end = new Point(mid.x + rightShift, mid.y);
			}

			Painter.drawLine(level, start, mid, f, true);
			Painter.drawLine(level, mid, end, f, false);
		}

		this.place(level, this.getCenter());

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);
		}
	}

	@Override
	public int getMinWidth() {
		return 5;
	}

	@Override
	public int getMinHeight() {
		return 5;
	}

	@Override
	public int getMaxWidth() {
		return 10;
	}

	@Override
	public int getMaxHeight() {
		return 10;
	}
}