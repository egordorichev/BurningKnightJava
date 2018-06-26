package org.rexcellentgames.burningknight.entity.level;

import org.rexcellentgames.burningknight.entity.Entity;

public class LightLevel extends Entity {
  public static boolean LIGHT = true;
  private Level level;

  public void setLevel(Level level) {
    this.level = level;
    this.depth = 14;
    this.alwaysRender = true;
  }

  @Override
  public void render() {
    if (LIGHT) {
      this.level.renderLight();
    }
  }
}