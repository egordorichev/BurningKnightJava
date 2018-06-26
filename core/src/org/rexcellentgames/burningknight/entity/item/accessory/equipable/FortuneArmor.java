package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class FortuneArmor extends Equipable {  @Override
  public void onEquip() {
    super.onEquip();

    if (this.owner instanceof Player) {
      ((Player) this.owner).luckDefense = true;
    }
  }

  @Override
  public void onUnequip() {
    super.onUnequip();

    if (this.owner instanceof Player) {
      ((Player) this.owner).luckDefense = false;
    }
  }
}