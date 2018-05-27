package org.rexellentgames.dungeon.entity.level.rooms.ladder;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.MathUtils;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;
import org.rexellentgames.dungeon.util.geometry.Rect;

public class CircleEntranceRoom extends EntranceRoom {
	@Override
	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);
		Painter.fillEllipse(level, this, 1, Terrain.FLOOR_A);

		if (this.connected.size() == 0) {
			Log.error("Invalid connection room");
			return;
		}

		byte floor = Terrain.FLOOR_A;

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

			Painter.drawLine(level, start, mid, floor, true);
			Painter.drawLine(level, mid, end, floor, false);
		}

		this.place(level, this.getCenter());

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);
		}
	}

	protected Rect getConnectionSpace() {
		Point c = getDoorCenter();

		return new Rect((int) c.x, (int) c.y, (int) c.x, (int) c.y);
	}

	//returns a point equidistant from all doors this room has
	protected final Point getDoorCenter() {
		Point doorCenter = new Point(0, 0);

		for (Door door : connected.values()) {
			doorCenter.x += door.x;
			doorCenter.y += door.y;
		}

		int n = this.connected.size();

		Point c = new Point((int) doorCenter.x / n, (int) doorCenter.y / n);
		if (Random.newFloat() < doorCenter.x % 1) c.x++;
		if (Random.newFloat() < doorCenter.y % 1) c.y++;
		c.x = (int) MathUtils.clamp(left + 1, right - 1, c.x);
		c.y = (int) MathUtils.clamp(top + 1, bottom - 1, c.y);

		return c;
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