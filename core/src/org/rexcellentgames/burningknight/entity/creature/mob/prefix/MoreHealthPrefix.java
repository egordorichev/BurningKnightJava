package org.rexcellentgames.burningknight.entity.creature.mob.prefix;

import com.badlogic.gdx.graphics.Color;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;

public class MoreHealthPrefix extends Prefix {
  private static Color color = Color.valueOf("#ac3232");

  @Override
  public Color getColor() {
    return color;
  }

  @Override
  public void onGenerate(Mob mob) {
    super.onGenerate(mob);

    int add = (int) Math.ceil(((float) mob.getHpMax()) * 1.5f);

    mob.setHpMax(mob.getHpMax() + add);
    mob.modifyHp(add, null);
  }
}