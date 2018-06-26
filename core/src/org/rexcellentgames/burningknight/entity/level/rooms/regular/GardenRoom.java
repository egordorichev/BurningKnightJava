package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class GardenRoom extends RegularRoom {
  @Override
  public void paint(Level level) {
    byte f = Terrain.randomFloor();

    Painter.fill(level, this, Terrain.WALL);
    Painter.fill(level, this, 1, f);

    if (Random.chance(50)) {
      for (int x = this.left + 1; x < this.right - 1; x++) {
        Painter.drawLine(level, new Point(x, this.top + 1), new Point(x, this.bottom - 1),
          x % 2 == 0 ? Terrain.DIRT : f);
      }
    } else {
      for (int y = this.top + 1; y < this.bottom - 1; y++) {
        Painter.drawLine(level, new Point(this.left + 1, y), new Point(this.right - 1, y),
          y % 2 == 0 ? Terrain.DIRT : f);

      }
    }

    for (Door door: this.connected.values()) {
      door.setType(Door.Type.REGULAR);
    }
  }
}