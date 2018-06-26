package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;

public class CollumnsRoom extends RegularRoom {
  @Override
  public void paint(Level level) {
    super.paint(level);

    int minDim = Math.min(getWidth(), getHeight());
    boolean circ = Random.chance(50);

    if (minDim == 7 || Random.newInt(2) == 0) {
      int pillarInset = minDim >= 11 ? 2 : 1;
      int pillarSize = ((minDim - 3) / 2) - pillarInset;

      int pillarX, pillarY;
      if (Random.newInt(2) == 0) {
        pillarX = Random.newInt(left + 1 + pillarInset, right - pillarSize - pillarInset);
        pillarY = top + 1 + pillarInset;
      } else {
        pillarX = left + 1 + pillarInset;
        pillarY = Random.newInt(top + 1 + pillarInset, bottom - pillarSize - pillarInset);
      }

      if (circ) {
        Painter.fillEllipse(level, pillarX, pillarY, pillarSize, pillarSize, Terrain.WALL);
      } else {
        Painter.fill(level, pillarX, pillarY, pillarSize, pillarSize, Terrain.WALL);
      }

      pillarX = right - (pillarX - left + pillarSize - 1);
      pillarY = bottom - (pillarY - top + pillarSize - 1);

      if (circ) {
        Painter.fillEllipse(level, pillarX, pillarY, pillarSize, pillarSize, Terrain.WALL);
      } else {
        Painter.fill(level, pillarX, pillarY, pillarSize, pillarSize, Terrain.WALL);
      }
    } else {
      int pillarInset = minDim >= 12 ? 2 : 1;
      int pillarSize = (minDim - 6) / (pillarInset + 1);

      float xSpaces = getWidth() - 2 * pillarInset - pillarSize - 2;
      float ySpaces = getHeight() - 2 * pillarInset - pillarSize - 2;
      float minSpaces = Math.min(xSpaces, ySpaces);

      float percentSkew = Math.round(Random.newFloat() * minSpaces) / minSpaces;

      if (circ) {
        Painter.fillEllipse(level, left + 1 + pillarInset + Math.round(percentSkew * xSpaces), top + 1 + pillarInset, pillarSize, pillarSize, Terrain.WALL);
        Painter.fillEllipse(level, right - pillarSize - pillarInset, top + 1 + pillarInset + Math.round(percentSkew * ySpaces), pillarSize, pillarSize, Terrain.WALL);
        Painter.fillEllipse(level, right - pillarSize - pillarInset - Math.round(percentSkew * xSpaces), bottom - pillarSize - pillarInset, pillarSize, pillarSize, Terrain.WALL);
        Painter.fillEllipse(level, left + 1 + pillarInset, bottom - pillarSize - pillarInset - Math.round(percentSkew * ySpaces), pillarSize, pillarSize, Terrain.WALL);
      } else {
        Painter.fill(level, left + 1 + pillarInset + Math.round(percentSkew * xSpaces), top + 1 + pillarInset, pillarSize, pillarSize, Terrain.WALL);
        Painter.fill(level, right - pillarSize - pillarInset, top + 1 + pillarInset + Math.round(percentSkew * ySpaces), pillarSize, pillarSize, Terrain.WALL);
        Painter.fill(level, right - pillarSize - pillarInset - Math.round(percentSkew * xSpaces), bottom - pillarSize - pillarInset, pillarSize, pillarSize, Terrain.WALL);
        Painter.fill(level, left + 1 + pillarInset, bottom - pillarSize - pillarInset - Math.round(percentSkew * ySpaces), pillarSize, pillarSize, Terrain.WALL);
      }
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