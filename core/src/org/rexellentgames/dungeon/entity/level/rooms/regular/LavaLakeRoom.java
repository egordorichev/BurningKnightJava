package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.util.MathUtils;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;
import org.rexellentgames.dungeon.util.geometry.Rect;

public class LavaLakeRoom extends PatchRoom {
	@Override
	protected float[] getSizeChance() {
		return new float[]{1, 3, 6};
	}

	@Override
	public void paint(Level level) {
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.LAVA);

		float fill = 0.1f + (this.getWidth() * this.getHeight()) / 512f;

		setupPatch(level, fill, 20, true);
		cleanDiagonalEdges();

		for (int i = top + 1; i < bottom; i++) {
			for (int j = left + 1; j < right; j++) {
				int in = xyToPatchCoords(j, i);

				if (!this.patch[in]) {
					level.set(j, i, Terrain.FLOOR_A);
				}
			}
		}

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);
		}

		byte floor = Terrain.FLOOR_A;
		boolean bold = false;

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

			//always goes inward first
			if (door.x == left || door.x == right) {
				mid = new Point(start.x + rightShift, start.y);
				end = new Point(mid.x, mid.y + downShift);

			} else {
				mid = new Point(start.x, start.y + downShift);
				end = new Point(mid.x + rightShift, mid.y);

			}

			Painter.drawLine(level, start, mid, floor, bold);
			Painter.drawLine(level, mid, end, floor, bold);
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
}