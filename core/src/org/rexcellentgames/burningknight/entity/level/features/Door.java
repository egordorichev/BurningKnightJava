package org.rexcellentgames.burningknight.entity.level.features;

import org.rexcellentgames.burningknight.util.geometry.Point;

public class Door extends Point {
  private Type type = Type.EMPTY;

  public Door(Point p) {
    super(p);
  }

  public Type getType() {
    return this.type;
  }

  public void setType(Type type) {
    if (type.compareTo(this.type) > 0) {
      this.type = type;
    }
  }

  public enum Type {
    EMPTY, TUNNEL, REGULAR, MAZE, ENEMY, LOCKED, LEVEL_LOCKED, SECRET
  }
}