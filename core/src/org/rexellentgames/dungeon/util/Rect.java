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
		this(other.left, other.top, other.right, other.bottom);
	}

	public Rect(int left, int top, int right, int bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	public int getWidth() {
		return this.right - this.left;
	}

	public int getHeight() {
		return this.bottom - this.top;
	}
}