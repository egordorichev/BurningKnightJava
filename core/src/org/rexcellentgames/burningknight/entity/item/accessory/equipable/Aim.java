package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

public class Aim extends Equipable {  @Override
  public void onEquip() {
    super.onEquip();
    this.owner.accuracy += 5;
  }

  @Override
  public void onUnequip() {
    super.onUnequip();
    this.owner.accuracy -= 5;
  }
}