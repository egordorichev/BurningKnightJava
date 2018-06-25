package org.rexcellentgames.burningknight.mod

import org.rexcellentgames.burningknight.entity.item.ItemRegistry
import org.rexcellentgames.burningknight.entity.item.ModItem

class Item(private val modId: String) {
  fun create(name: String) {
    val nameWithId = "$modId:$name"
    
    ItemRegistry.modItems[nameWithId] = ModItem(modId, nameWithId, "", "item-missing")
  }
}