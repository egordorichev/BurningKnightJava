package org.rexellentgames.dungeon.util.geometry;

public class Point {
	public int x;
	public int y;

	public Point() {
		this(0, 0);
	}

	public Point(Point other) {
		this(other.x, other.y);
	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
}