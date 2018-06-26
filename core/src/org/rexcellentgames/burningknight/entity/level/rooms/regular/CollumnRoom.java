package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;

public class CollumnRoom extends RegularRoom {
  @Override
  public void paint(Level level) {
    super.paint(level);

    if (Random.chance(50)) {
      Painter.fillEllipse(level, this, 3, Random.chance(50) ? Terrain.CHASM : Terrain.WALL);
    } else {
      Painter.fill(level, this, 3, Random.chance(50) ? Terrain.CHASM : Terrain.WALL);
    }
  }

  @Override
  public int getMinWidth() {
    return 8;
  }

  @Override
  public int getMinHeight() {
    return 8;
  }
}