package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.entity.creature.mob.Mob;

public class TheEye extends Equipable {  @Override
  public void onEquip() {
    super.onEquip();
    Mob.shotSpeedMod = 0.5f;
  }

  @Override
  public void onUnequip() {
    super.onUnequip();
    Mob.shotSpeedMod = 1;
  }
}