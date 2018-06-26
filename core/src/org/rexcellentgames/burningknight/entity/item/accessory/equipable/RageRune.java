package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class RageRune extends Equipable {  @Override
  public void onEquip() {
    super.onEquip();

    if (this.owner instanceof Player) {
      ((Player) this.owner).lowHealthDamage = true;
    }
  }

  @Override
  public void onUnequip() {
    super.onUnequip();

    if (this.owner instanceof Player) {
      ((Player) this.owner).lowHealthDamage = false;
    }
  }
}