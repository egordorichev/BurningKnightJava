package org.rexcellentgames.burningknight.entity.item.autouse;

import org.rexcellentgames.burningknight.Dungeon;

public class MapGreenprints extends Autouse {  @Override
  public void use() {
    super.use();
    this.setCount(count - 1);
    Dungeon.level.exploreRandom();
  }
}