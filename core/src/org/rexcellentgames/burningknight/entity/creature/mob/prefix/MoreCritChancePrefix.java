package org.rexcellentgames.burningknight.entity.creature.mob.prefix;

import com.badlogic.gdx.graphics.Color;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;

public class MoreCritChancePrefix extends Prefix {
  private static Color color = Color.valueOf("#00ffff");

  @Override
  public Color getColor() {
    return color;
  }

  @Override
  public void apply(Mob mob) {
    super.apply(mob);
    mob.modifyStat("crit_chance", 0.3f);
  }
}