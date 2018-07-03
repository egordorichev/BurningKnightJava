package org.rexcellentgames.burningknight.util.geometry;

import java.util.ArrayList;

public class Rect {
	public int left;
	public int top;
	public int right;
	public int bottom;

	public Rect() {
		this(0, 0, 0, 0);
	}

	public Rect(Rect rect) {
		this(rect.left, rect.top, rect.right, rect.bottom);
	}

	public Rect(int left, int top, int right, int bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	public int getWidth() {
		return right - left;
	}

	public int getHeight() {
		return bottom - top;
	}

	public int square() {
		return getWidth() * getHeight();
	}

	public Rect set(int left, int top, int right, int bottom) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		return this;
	}

	public Rect set(Rect rect) {
		return set(rect.left, rect.top, rect.right, rect.bottom);
	}

	public Rect setPos(int x, int y) {
		return set(x, y, x + (right - left), y + (bottom - top));
	}

	public Rect shift(int x, int y) {
		return set(left + x, top + y, right + x, bottom + y);
	}

	public Rect resize(int w, int h) {
		return set(left, top, left + w, top + h);
	}

	public boolean isEmpty() {
		return right <= left || bottom <= top;
	}

	public Rect setEmpty() {
		left = right = top = bottom = 0;
		return this;
	}

	public Rect intersect(Rect other) {
		Rect result = new Rect();
		result.left = Math.max(left, other.left);
		result.right = Math.min(right, other.right);
		result.top = Math.max(top, other.top);
		result.bottom = Math.min(bottom, other.bottom);

		return result;
	}

	public Rect union(Rect other) {
		Rect result = new Rect();
		result.left = Math.min(left, other.left);
		result.right = Math.max(right, other.right);
		result.top = Math.min(top, other.top);
		result.bottom = Math.max(bottom, other.bottom);
		return result;
	}

	public Rect union(int x, int y) {
		if (isEmpty()) {
			return set(x, y, x + 1, y + 1);
		} else {
			if (x < left) {
				left = x;
			} else if (x >= right) {
				right = x + 1;
			}
			if (y < top) {
				top = y;
			} else if (y >= bottom) {
				bottom = y + 1;
			}
			return this;
		}
	}

	public Rect union(Point p) {
		return union((int) p.x, (int) p.y);
	}

	public boolean inside(Point p) {
		return p.x >= left && p.x < right && p.y >= top && p.y < bottom;
	}

	public Rect shrink(int d) {
		return new Rect(left + d, top + d, right - d, bottom - d);
	}

	public Rect shrink(int d, int h) {
		return new Rect(left + d, top + h, right - d, bottom - h);
	}

	public Rect shrink() {
		return shrink(1);
	}

	public ArrayList<Point> getPoints() {
		ArrayList<Point> points = new ArrayList<>();

		for (int i = Math.min(right, left); i <= Math.max(right, left); i++) {
			for (int j = Math.min(top, bottom); j <= Math.max(top, bottom); j++) {
				points.add(new Point(i, j));
			}
		}

		return points;
	}

}