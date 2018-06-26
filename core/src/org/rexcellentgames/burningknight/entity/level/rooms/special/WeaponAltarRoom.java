package org.rexcellentgames.burningknight.entity.level.rooms.special;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.WeaponAltar;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class WeaponAltarRoom extends SpecialRoom {
  @Override
  public void paint(Level level) {
    super.paint(level);

    Point center = this.getCenter();
    WeaponAltar altar = new WeaponAltar();

    altar.x = center.x * 16 - 8;
    altar.y = center.y * 16;

    Dungeon.area.add(altar);
    LevelSave.add(altar);
  }

  @Override
  public int getMinWidth() {
    return 8;
  }
}