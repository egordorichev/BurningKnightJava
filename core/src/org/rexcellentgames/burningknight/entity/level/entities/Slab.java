package org.rexcellentgames.burningknight.entity.level.entities;

import com.badlogic.gdx.math.Rectangle;
import org.rexcellentgames.burningknight.Dungeon;

public class Slab extends SolidProp {
  private boolean s;

  {

    collider = new Rectangle(3, 10, 8, 1);
  }

  @Override
  public void update(float dt) {
    super.update(dt);

    if (!s) {
      s = true;
      Dungeon.level.setPassable((int) (this.x / 16), (int) ((this.y + 8) / 16), false);
    }
  }
}