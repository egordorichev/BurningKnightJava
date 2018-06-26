package org.rexcellentgames.burningknight.entity.item.consumable.potion;

import org.rexcellentgames.burningknight.entity.creature.buff.RegenerationBuff;

public class RegenerationPotion extends Potion {
  {  }

  @Override
  public void use() {
    super.use();
    this.owner.addBuff(new RegenerationBuff());
  }
}