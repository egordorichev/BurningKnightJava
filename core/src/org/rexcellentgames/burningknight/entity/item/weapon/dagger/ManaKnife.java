package org.rexcellentgames.burningknight.entity.item.weapon.dagger;

import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class ManaKnife extends Dagger {
  {    damage = 1;
  }

  @Override
  public void onHit(Creature creature) {
    super.onHit(creature);

    if (this.owner instanceof Player) {
      ((Player) this.owner).modifyMana(1);
    }
  }
}