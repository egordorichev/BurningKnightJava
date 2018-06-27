package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.assets.Locale
import org.rexcellentgames.burningknight.entity.creature.player.Player

class ManaEmblem : Equipable() {
  init {
    
    
    
  }

  override fun onEquip() {
    super.onEquip()

    if (this.owner is Player) {
      (this.owner as Player).manaRegenRate += 5.0f
    }
  }

  override fun onUnequip() {
    super.onUnequip()

    if (this.owner is Player) {
      (this.owner as Player).manaRegenRate -= 5.0f
    }
  }
}