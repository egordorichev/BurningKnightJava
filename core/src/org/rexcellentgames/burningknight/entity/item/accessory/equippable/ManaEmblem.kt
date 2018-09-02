package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.assets.Locale
import org.rexcellentgames.burningknight.entity.creature.player.Player

class ManaEmblem : Equippable() {
  init {
    name = Locale.get("mana_emblem")
    description = Locale.get("mana_emblem_desc")
    sprite = "item-mana-emblem"
  }

  override fun onEquip(load: Boolean) {
    super.onEquip(load)

    if (this.owner is Player) {
      (this.owner as Player).manaRegenRate += 5.0f
    }
  }

  override fun onUnequip(load: Boolean) {
    super.onUnequip(load)

    if (this.owner is Player) {
      (this.owner as Player).manaRegenRate -= 5.0f
    }
  }

  override fun canBeDegraded(): Boolean {
    return false
  }

  override fun canBeUpgraded(): Boolean {
    return false
  }
}