package org.rexcellentgames.burningknight.entity.pool.item;

import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.pool.Pool;

public class WoodenChestPool extends Pool<Item> {
  public static WoodenChestPool instance = new WoodenChestPool();

  public WoodenChestPool() {
    switch (Player.instance.type) {
      case WARRIOR:
        addFrom(AccessoryPoolWarrior.instance);
        break;
      case WIZARD:
        addFrom(AccessoryPoolMage.instance);
        break;
      case RANGER:
        addFrom(AccessoryPoolRanger.instance);
        break;
    }

    addFrom(AccessoryPoolAll.instance);

    switch (Player.instance.getType()) {
      case WARRIOR:
        addFrom(AccessoryPoolWarrior.instance);
        break;
      case WIZARD:
        addFrom(AccessoryPoolMage.instance);
        break;
      case RANGER:
        addFrom(AccessoryPoolRanger.instance);
        break;
    }

    addFrom(ShopHatPool.instance);
  }
}