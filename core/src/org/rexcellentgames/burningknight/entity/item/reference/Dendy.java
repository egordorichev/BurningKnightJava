package org.rexcellentgames.burningknight.entity.item.reference;

import org.rexcellentgames.burningknight.entity.item.consumable.Consumable;

public class Dendy extends Consumable {
  {    useOnPickup = true;

  }

  @Override
  public void use() {
    super.use();
    this.setCount(this.count - 1);
    this.owner.modifySpeed(3);
  }
}