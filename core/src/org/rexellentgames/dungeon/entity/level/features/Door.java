package org.rexellentgames.dungeon.entity.level.features;

import org.rexellentgames.dungeon.util.geometry.Point;

public class Door extends Point {
	public enum Type {
		EMPTY, TUNNEL, REGULAR, MAZE, ENEMY
	}

	private Type type = Type.EMPTY;

	public Door(Point p) {
		super(p);
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