package org.rexellentgames.dungeon.entity.level;

import org.rexellentgames.dungeon.util.Point;

public class Door extends Point {
	public enum Type {
		EMPTY, TUNNEL, REGULAR, SECRET
	}

	private Type type = Type.EMPTY;

	public Door(int x, int y) {
		super(x, y);
	}

	public void setType(Type type) {
		if (type.compareTo(this.type) > 0) {
			this.type = type;
		}
	}

	public Type getType() {
		return this.type;
	}
}