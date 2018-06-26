package org.rexcellentgames.burningknight.entity.creature.mob.prefix;

import com.badlogic.gdx.graphics.Color;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;

public class KnockbackPrefix extends Prefix {
  private static Color color = Color.valueOf("#ffff00");

  @Override
  public Color getColor() {
    return color;
  }

  @Override
  public void apply(Mob mob) {
    super.apply(mob);
    mob.setStat("knockback", 1);
  }
}