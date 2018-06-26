package org.rexcellentgames.burningknight.entity.item.weapon.bow.arrows;

import org.rexcellentgames.burningknight.entity.item.Item;

public class Arrow extends Item {
  public int damage;

  {
    stackable = true;
    useable = false;
  }

  @Override
  public void render(float x, float y, float w, float h, boolean flipped) {

  }
}