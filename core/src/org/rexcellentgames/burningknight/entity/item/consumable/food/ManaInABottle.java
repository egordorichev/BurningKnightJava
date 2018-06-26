package org.rexcellentgames.burningknight.entity.item.consumable.food;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.consumable.Consumable;

public class ManaInABottle extends Consumable {  @Override
  public void use() {
    super.use();
    setCount(count - 1);

    if (this.owner instanceof Player) {
      ((Player) this.owner).modifyMana(((Player) this.owner).getManaMax());
    }
  }
}