package org.rexellentgames.dungeon.util.geometry;

public class Point {
	public float x;
	public float y;

	public Point() {
		this(0, 0);
	}

	public Point(Point other) {
		this(other.x, other.y);
	}

	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}
}