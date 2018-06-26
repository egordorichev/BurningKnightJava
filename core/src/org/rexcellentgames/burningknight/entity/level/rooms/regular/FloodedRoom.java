package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;

public class FloodedRoom extends RegularRoom {
  @Override
  public void paint(Level level) {
    Painter.fill(level, this, Terrain.WALL);
    Painter.fill(level, this, 1, Terrain.randomFloor());
    Painter.fill(level, this, 1, Terrain.WATER);

    for (Door door: this.connected.values()) {
      door.setType(Door.Type.REGULAR);
    }
  }
}