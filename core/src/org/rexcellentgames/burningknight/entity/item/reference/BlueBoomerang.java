package org.rexcellentgames.burningknight.entity.item.reference;

import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.FreezeBuff;
import org.rexcellentgames.burningknight.entity.item.weapon.axe.Axe;
import org.rexcellentgames.burningknight.util.Log;

public class BlueBoomerang extends Axe {
  {    damage = 10;
    speed = 1000;
    penetrates = true;
  }

  @Override
  public void onHit(Creature creature) {
    super.onHit(creature);

    Log.info("Hit " + creature);

    creature.addBuff(new FreezeBuff().setDuration(2));
  }
}