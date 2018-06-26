package org.rexcellentgames.burningknight.entity.item.consumable.food;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.consumable.Consumable;

public class Bread extends Consumable {  @Override
  public void use() {
    super.use();
    setCount(count - 1);
    this.owner.modifyHp(this.owner.getHpMax(), null);

    if (this.owner instanceof Player) {
      ((Player) this.owner).drawInvt = true;
      this.owner.setInvt(5);
    }
  }
}