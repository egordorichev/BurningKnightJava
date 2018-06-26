package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.Statue;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;

public class StatueRoom extends RegularRoom {
  @Override
  public void paint(Level level) {
    super.paint(level);

    this.row(this.left + 2);
    this.row(this.right - 2);
  }

  private void row(int x) {
    for (int y = this.top + 2; y < this.bottom - 1; y += 3) {
      Statue statue = new Statue();

      statue.x = x * 16;
      statue.y = y * 16;

      Dungeon.area.add(statue);
      LevelSave.add(statue);
    }
  }

  @Override
  public int getMinWidth() {
    return 7;
  }

  @Override
  public int getMinHeight() {
    return 7;
  }
}