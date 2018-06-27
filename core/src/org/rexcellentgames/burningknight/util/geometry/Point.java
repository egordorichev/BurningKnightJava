package org.rexcellentgames.burningknight.util.geometry;

import com.badlogic.gdx.math.Vector2;

public class Point extends Vector2 {
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

	public Point mul(float v) {
		this.x *= v;
		this.y *= v;

		return this;
	}

	public Point offset(Point d) {
		x += d.x;
		y += d.y;
		return this;
	}

	@Override
	public String toString() {
		return "Point(" + x + ", " + y + ")";
	}
}