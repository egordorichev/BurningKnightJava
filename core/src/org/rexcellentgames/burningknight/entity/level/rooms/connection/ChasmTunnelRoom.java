package org.rexcellentgames.burningknight.entity.level.rooms.connection;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;

public class ChasmTunnelRoom extends TunnelRoom {
  @Override
  protected void fill(Level level) {
    if (Dungeon.depth != -1) {
      Painter.fill(level, this, Terrain.WALL);
    }

    Painter.fill(level, this, 1, Terrain.CHASM);

    for (Door door: this.connected.values()) {
      door.setType(Door.Type.TUNNEL);
    }
  }

  @Override
  public int getMinHeight() {
    return 7;
  }

  @Override
  public int getMinWidth() {
    return 7;
  }
}