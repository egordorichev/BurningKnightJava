package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

public class CobaltShield extends Equipable {  @Override
  public void onEquip() {
    super.onEquip();
    this.owner.modifyStat("knockback", -1);
  }

  @Override
  public void onUnequip() {
    super.onUnequip();
    this.owner.modifyStat("knockback", 1);
  }
}