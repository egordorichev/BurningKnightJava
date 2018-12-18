package org.rexcellentgames.burningknight.entity.level.features;

import org.rexcellentgames.burningknight.util.geometry.Point;

public class Door extends Point {
	public enum Type {
		EMPTY, TUNNEL, REGULAR, MAZE, ENEMY, LOCKED, LEVEL_LOCKED, BOSS, SECRET
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