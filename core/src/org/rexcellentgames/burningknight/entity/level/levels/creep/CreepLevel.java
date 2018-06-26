package org.rexcellentgames.burningknight.entity.level.levels.creep;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.level.RegularLevel;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.HallPainter;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;

public class CreepLevel extends RegularLevel {
  public CreepLevel() {
    Terrain.loadTextures(4);
    this.uid = 4;
  }

  @Override
  public String getName() {
    return Locale.get("corrupted_castle");
  }

  @Override
  public String getMusic() {
    return Dungeon.depth == 0 ? "Gobbeon" : "Believer";
  }

  @Override
  protected Painter getPainter() {
    return new HallPainter().setGrass(0.45f).setWater(0.45f);
  }

  @Override
  protected int getNumConnectionRooms() {
    return 0;
  }
}