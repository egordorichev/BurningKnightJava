package org.rexcellentgames.burningknight.mod.item

import org.luaj.vm2.LuaTable
import org.rexcellentgames.burningknight.entity.item.ItemRegistry
import org.rexcellentgames.burningknight.entity.item.mod.ModItem

class Item(private val modId: String) {
  fun create(name: String, args: LuaTable) {
    register("$modId:$name", args, ItemType.ITEM)
  }

  fun equipable(name: String, args: LuaTable) {
    register("$modId:$name", args, ItemType.EQUIPABLE)
  }

  fun weapon(name: String, args: LuaTable) {
    register("$modId:$name", args, ItemType.WEAPON)
  }
  
  private fun register(name: String, args: LuaTable, type: ItemType) {
    ItemRegistry.modItems[name] = when (type) {
      ItemType.ITEM -> ModItem(modId, name, args)
      ItemType.EQUIPABLE -> ModItem(modId, name, args)
      ItemType.WEAPON -> ModItem(modId, name, args)
    }
  }
}