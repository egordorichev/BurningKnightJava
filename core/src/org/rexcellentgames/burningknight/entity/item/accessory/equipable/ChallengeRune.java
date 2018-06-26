package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.entity.creature.mob.Mob;

public class ChallengeRune extends Equipable {  @Override
  public void onEquip() {
    super.onEquip();
    Mob.challenge = true;

    for (Mob mob: Mob.all) {
      mob.generatePrefix();
    }

    this.owner.modifyDefense(2);
  }

  @Override
  public void onUnequip() {
    super.onUnequip();
    Mob.challenge = false;
    this.owner.modifyDefense(-2);
  }
}