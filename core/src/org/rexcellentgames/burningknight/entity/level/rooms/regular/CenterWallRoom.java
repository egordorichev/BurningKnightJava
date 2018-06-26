package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class CenterWallRoom extends RegularRoom {
  @Override
  public void paint(Level level) {
    super.paint(level);

    byte f = Random.chance(40) ? Terrain.CHASM : Terrain.WALL;

    Painter.drawLine(level, new Point(this.left + this.getWidth() / 2 - 2, this.top + this.getHeight() / 2 + 1),
      new Point(this.left + this.getWidth() / 2 - 2, this.top + 1), f);

    Painter.drawLine(level, new Point(this.left + this.getWidth() / 2 + 2, this.top + this.getHeight() / 2 - 1),
      new Point(this.left + this.getWidth() / 2 + 2, this.bottom - 1), f);

    if (Random.chance(50)) {
      Painter.set(level, new Point(this.left + this.getWidth() / 2 - 1, this.top + this.getHeight() / 2 + 1), f);
      Painter.set(level, new Point(this.left + this.getWidth() / 2 + 1, this.top + this.getHeight() / 2 - 1), f);
    }
  }

  @Override
  public boolean canConnect(Point p) {
    if (p.y == this.top || p.y == this.bottom) {
      return false;
    }

    return super.canConnect(p);
  }

  @Override
  public int getMinWidth() {
    return 10;
  }

  @Override
  public int getMinHeight() {
    return 7;
  }
}