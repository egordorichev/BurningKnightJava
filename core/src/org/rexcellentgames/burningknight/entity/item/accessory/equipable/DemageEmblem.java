package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class DemageEmblem extends Equipable {  @Override
  public void onEquip() {
    super.onEquip();

    if (this.owner instanceof Player) {
      ((Player) this.owner).defenseModifier += 0.2f;
      ((Player) this.owner).damageModifier += 0.2f;
    }
  }

  @Override
  public void onUnequip() {
    super.onUnequip();

    if (this.owner instanceof Player) {
      ((Player) this.owner).defenseModifier -= 0.2f;
      ((Player) this.owner).damageModifier -= 0.2f;
    }
  }
}