package org.rexellentgames.dungeon.util;

import org.rexellentgames.dungeon.util.geometry.Point;

import java.util.ArrayList;

public class Line {
	private ArrayList<Point> points;

	public Line(int x0, int y0, int x1, int y1) {
		this.points = new ArrayList<>();

		int dx = Math.abs(x0 - x1);
		int dy = Math.abs(y0 - y1);

		int sx = (x0 < x1) ? 1 : -1;
		int sy = (y0 < y1) ? 1 : -1;
		int err = dx - dy;

		while (true) {
			points.add(new Point(x0, y0));

			if (x0 == x1 && y0 == y1) {
				break;
			}

			int e2 = err * 2;

			if (e2 > -dx) {
				err -= dy;
				x0 += sx;
			}

			if (e2 < dx) {
				err += dx;
				y0 += sy;
			}
		}
	}

	public ArrayList<Point> getPoints() {
		return this.points;
	}
}