package org.rexcellentgames.burningknight.entity.item.accessory.equipable;

public class FortuneRing extends Equipable {  @Override
  public void onEquip() {
    super.onEquip();
    this.owner.modifyStat("crit_chance", 0.1f);
  }

  @Override
  public void onUnequip() {
    super.onUnequip();
    this.owner.modifyStat("crit_chance", -0.1f);
  }
}