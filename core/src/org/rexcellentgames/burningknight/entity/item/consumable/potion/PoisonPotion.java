package org.rexcellentgames.burningknight.entity.item.consumable.potion;

import org.rexcellentgames.burningknight.entity.creature.buff.PoisonBuff;

public class PoisonPotion extends Potion {
  {  }

  @Override
  public void use() {
    super.use();
    this.owner.addBuff(new PoisonBuff());
  }
}