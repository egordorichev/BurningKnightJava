package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

public class PenetrationRune extends Equipable {  @Override
  public void onEquip() {
    super.onEquip();
    this.owner.penetrates = true;
  }

  @Override
  public void onUnequip() {
    super.onUnequip();
    this.owner.penetrates = false;
  }
}