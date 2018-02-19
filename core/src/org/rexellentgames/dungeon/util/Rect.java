package org.rexellentgames.dungeon.util;

public class Rect {
	public int left;
	public int top;
	public int right;
	public int bottom;

	public Rect() {
		this(0, 0, 0, 0);
	}

	public Rect(Rect other) {
		this.set(other);
	}

	public Rect(int left, int top, int right, int bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	public Rect set(Rect other) {
		this.left = other.left;
		this.top = other.top;
		this.right = other.right;
		this.bottom = other.bottom;

		return this;
	}

	public Rect intersect(Rect other) {
		Rect result = new Rect();

		result.left = Math.max(this.left, other.left);
		result.right = Math.min(this.right, other.right);
		result.top = Math.max(this.top, other.top);
		result.bottom = Math.min(this.bottom, other.bottom);

		return result;
	}

	public int getWidth() {
		return this.right - this.left;
	}

	public int getHeight() {
		return this.bottom - this.top;
	}

	public Point getCenter() {
		return new Point(
			(left + right) / 2 + (((right - left) & 1) == 1 ? Random.newInt(2) : 0),
			(top + bottom) / 2 + (((bottom - top) & 1) == 1 ? Random.newInt(2) : 0));
	}
}