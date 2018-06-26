package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class SideChasmsRoom extends RegularRoom {
  @Override
  public void paint(Level level) {
    for (Door door: this.connected.values()) {
      door.setType(Door.Type.REGULAR);
    }

    byte f = Terrain.randomFloor();

    Painter.fill(level, this, Terrain.WALL);
    Painter.fill(level, this, 1, Terrain.CHASM);
    Painter.fill(level, this, Random.newInt(2, 4), f);

    this.paintTunnel(level, f);
  }

  @Override
  protected Point getDoorCenter() {
    return getCenter();
  }

  @Override
  public int getMinWidth() {
    return 9;
  }

  @Override
  public int getMinHeight() {
    return 9;
  }
}