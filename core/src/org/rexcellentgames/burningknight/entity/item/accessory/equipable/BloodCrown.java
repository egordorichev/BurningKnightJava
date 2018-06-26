package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class BloodCrown extends Equipable {  @Override
  public void onEquip() {
    super.onEquip();
    Player.mobSpawnModifier += 1f;
  }

  @Override
  public void onUnequip() {
    super.onUnequip();
    Player.mobSpawnModifier -= 1f;
  }
}