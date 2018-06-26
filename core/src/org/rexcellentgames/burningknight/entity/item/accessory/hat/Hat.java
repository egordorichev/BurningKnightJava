package org.rexcellentgames.burningknight.entity.item.accessory.hat;

import org.rexcellentgames.burningknight.entity.creature.inventory.UiInventory;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.accessory.Accessory;

public class Hat extends Accessory {
  protected int defense = 1;
  protected String skin;

  {
    useable = true;
  }

  @Override
  public void use() {
    super.use();

    UiInventory ui = Player.instance.ui;

    Item item = ui.getInventory().getSlot(6);
    ui.getInventory().setSlot(6, ui.getInventory().getSlot(ui.getActive()));
    ui.getInventory().setSlot(ui.getActive(), item);

    if (item != null) {
      ((Accessory) item).onUnequip();
    }

    ((Accessory) ui.getInventory().getSlot(6)).onEquip();
  }

  @Override
  public void onEquip() {
    super.onEquip();

    this.owner.modifyDefense(this.defense);

    if (this.owner instanceof Player) {
      ((Player) this.owner).setSkin(this.skin);
    }
  }

  @Override
  public void onUnequip() {
    super.onUnequip();

    this.owner.modifyDefense(-this.defense);

    if (this.owner instanceof Player) {
      ((Player) this.owner).setSkin("");
    }
  }

  @Override
  public StringBuilder buildInfo() {
    StringBuilder builder = super.buildInfo();

    builder.append("\n");
    builder.append(this.defense);
    builder.append(" defense");

    return builder;
  }
}