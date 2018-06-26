package org.rexcellentgames.burningknight.entity.item.accessory.equipable

import org.rexcellentgames.burningknight.entity.creature.player.Player

class FireFlower : Equipable() {
  init {
    super.init()  }

  override fun onEquip() {
    super.onEquip()

    if (this.owner is Player) {
      (this.owner as Player).burnChance += 100f
    }
  }

  override fun onUnequip() {
    super.onUnequip()

    if (this.owner is Player) {
      (this.owner as Player).burnChance -= 100f
    }
  }
}