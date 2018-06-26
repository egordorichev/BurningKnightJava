package org.rexcellentgames.burningknight.entity.item.consumable.potion;

import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;

public class FirePotion extends Potion {
  {  }

  @Override
  public void use() {
    super.use();
    this.owner.addBuff(new BurningBuff());
  }
}