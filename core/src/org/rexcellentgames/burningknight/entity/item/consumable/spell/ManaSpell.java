package org.rexcellentgames.burningknight.entity.item.consumable.spell;

import org.rexcellentgames.burningknight.entity.creature.player.Player;

public class ManaSpell extends Spell {
  {  }

  @Override
  public void use() {
    if (this.owner instanceof Player) {
      Player player = (Player) this.owner;

      if (player.getMana() == player.getManaMax()) {
        return;
      }

      super.use();

      player.modifyMana(player.getManaMax() - player.getMana());
    }
  }
}