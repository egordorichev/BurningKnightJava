package org.rexellentgames.dungeon.entity.level.painters;

import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;
import org.rexellentgames.dungeon.util.geometry.Rect;

import java.util.ArrayList;
import java.util.Arrays;

public class Painter {
	public void paint(Level level, ArrayList<Room> rooms) {
		int leftMost = Integer.MAX_VALUE, topMost = Integer.MAX_VALUE;

		for (Room r : rooms) {
			if (r.left < leftMost) leftMost = r.left;
			if (r.top < topMost) topMost = r.top;
		}

		//subtract 1 for padding
		leftMost--;
		topMost--;

		int rightMost = 0, bottomMost = 0;

		for (Room r : rooms) {
			r.shift(-leftMost, -topMost);
			if (r.right > rightMost) rightMost = r.right;
			if (r.bottom > bottomMost) bottomMost = r.bottom;
		}

		//add 1 for padding
		rightMost++;
		bottomMost++;

		//add 1 to account for 0 values
		level.setSize(rightMost + 1, bottomMost + 1);
		level.fill();

		for (Room room : rooms) {
			this.placeDoors(room);
			room.paint(level);
		}

		this.paintDoors(level, rooms);
		this.decorate(level, rooms);
	}

	protected void decorate(Level level, ArrayList<Room> rooms) {

	}

	private void paintDoors(Level level, ArrayList<Room> rooms) {
		for (Room r : rooms) {
			for (Room n : r.getConnected().keySet()) {
				Door d = r.getConnected().get(n);

				if (d.getType() == Door.Type.REGULAR) {
					level.set((int) d.x, (int) d.y, Terrain.DOOR);
				} else {
					level.set((int) d.x, (int) d.y, Terrain.FLOOR);
				}
			}
		}
	}

	private void placeDoors(Room r) {
		for (Room n : r.getConnected().keySet()) {
			Door door = r.getConnected().get(n);
			if (door == null) {
				Rect i = r.intersect(n);
				ArrayList<Point> doorSpots = new ArrayList<Point>();

				for (Point p : i.getPoints()) {
					if (r.canConnect(p) && n.canConnect(p))
						doorSpots.add(p);
				}

				door = new Door(doorSpots.get(Random.newInt(doorSpots.size())));

				r.getConnected().put(n, door);
				n.getConnected().put(r, door);
			}
		}
	}

	public static void set(Level level, int cell, int value) {
		level.data[cell] = (short) value;
	}

	public static void set(Level level, int x, int y, int value) {
		set(level, x + y * level.getWIDTH(), value);
	}

	public static void set(Level level, Point p, int value) {
		set(level, (int) p.x, (int) p.y, value);
	}

	public static void fill(Level level, int x, int y, int w, int h, int value) {
		int width = level.getWIDTH();

		int pos = y * width + x;
		for (int i = y; i < y + h; i++, pos += width) {
			Arrays.fill(level.data, pos, pos + w, (short) value);
		}
	}

	public static void fill(Level level, Rect rect, int value) {
		fill(level, rect.left, rect.top, rect.getWidth(), rect.getHeight(), value);
	}

	public static void fill(Level level, Rect rect, int m, int value) {
		fill(level, rect.left + m, rect.top + m, rect.getWidth() - m * 2, rect.getHeight() - m * 2, value);
	}

	public static void fill(Level level, Rect rect, int l, int t, int r, int b, int value) {
		fill(level, rect.left + l, rect.top + t, rect.getWidth() - (l + r), rect.getHeight() - (t + b), value);
	}

	public static void drawLine(Level level, Point from, Point to, int value) {
		float x = from.x;
		float y = from.y;
		float dx = to.x - from.x;
		float dy = to.y - from.y;

		boolean movingbyX = Math.abs(dx) >= Math.abs(dy);
		//normalize
		if (movingbyX) {
			dy /= Math.abs(dx);
			dx /= Math.abs(dx);
		} else {
			dx /= Math.abs(dy);
			dy /= Math.abs(dy);
		}

		set(level, Math.round(x), Math.round(y), value);

		while ((movingbyX && to.x != x) || (!movingbyX && to.y != y)) {
			x += dx;
			y += dy;

			set(level, Math.round(x), Math.round(y), value);
		}
	}

	public static void fillEllipse(Level level, Rect rect, int value) {
		fillEllipse(level, rect.left, rect.top, rect.getWidth(), rect.getHeight(), value);
	}

	public static void fillEllipse(Level level, Rect rect, int m, int value) {
		fillEllipse(level, rect.left + m, rect.top + m, rect.getWidth() - m * 2, rect.getHeight() - m * 2, value);
	}

	public static void fillEllipse(Level level, int x, int y, int w, int h, int value) {
		//radii
		double radH = h / 2f;
		double radW = w / 2f;

		//fills each row of the ellipse from top to bottom
		for (int i = 0; i < h; i++) {

			//y coordinate of the row for determining ellipsis width
			//always want to test the middle of a tile, hence the 0.5 shift
			double rowY = -radH + 0.5 + i;

			//equation is derived from ellipsis formula: y^2/radH^2 + x^2/radW^2 = 1
			//solves for x and then doubles to get the width
			double rowW = 2.0 * Math.sqrt((radW * radW) * (1.0 - (rowY * rowY) / (radH * radH)));

			//need to round to nearest even or odd number, depending on width
			if (w % 2 == 0) {
				rowW = Math.round(rowW / 2.0) * 2.0;

			} else {
				rowW = Math.floor(rowW / 2.0) * 2.0;
				rowW++;
			}

			int cell = x + (w - (int) rowW) / 2 + ((y + i) * level.getWIDTH());
			Arrays.fill(level.data, cell, cell + (int) rowW, (short) value);
		}
	}

	public static Point drawInside(Level level, Room room, Point from, int n, int value) {
		Point step = new Point();

		if (from.x == room.left) {
			step.set(+1, 0);
		} else if (from.x == room.right) {
			step.set(-1, 0);
		} else if (from.y == room.top) {
			step.set(0, +1);
		} else if (from.y == room.bottom) {
			step.set(0, -1);
		}

		Point p = new Point(from).offset(step);

		for (int i = 0; i < n; i++) {
			if (value != -1) {
				set(level, p, value);
			}

			p.offset(step);
		}

		return p;
	}
}