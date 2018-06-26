package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.entity.trap.RollingSpike;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;
import org.rexcellentgames.burningknight.util.geometry.Rect;

public class SpikeTrapRoom extends TrapRoom {
  @Override
  public void paint(Level level) {
    byte f = Terrain.randomFloor();

    Painter.fill(level, this, Terrain.WALL);
    Painter.fill(level, new Rect(this.left + 1, this.top + 1, this.left + 4, this.bottom), f);
    Painter.fill(level, new Rect(this.right - 3, this.top + 1, this.right, this.bottom), f);

    int y = this.top + Random.newInt(1, this.getHeight() - 1);

    Painter.drawLine(level, new Point(this.left + 1, y),
      new Point(this.right - 1, y), f);

    RollingSpike spike = new RollingSpike();

    spike.x = (this.left + 1) * 16 + 1;
    spike.y = y * 16 - 1;
    spike.vel = new Point(20f, 0);

    Dungeon.area.add(spike);
    LevelSave.add(spike);

    for (Door door: connected.values()) {
      door.setType(Door.Type.REGULAR);
    }
  }

  @Override
  public boolean canConnect(Point p) {
    if (p.x != this.left && p.x != this.right) {
      return false;
    }

    return super.canConnect(p);
  }

  @Override
  public int getMinWidth() {
    return 10;
  }
}