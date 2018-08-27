package org.rexcellentgames.burningknight.mod.item

import org.luaj.vm2.LuaTable
import org.rexcellentgames.burningknight.entity.item.ItemRegistry
import org.rexcellentgames.burningknight.entity.item.accessory.equippable.Equippable
import org.rexcellentgames.burningknight.entity.item.consumable.Consumable
import org.rexcellentgames.burningknight.entity.item.weapon.Weapon

class Item(private val modId: String) {
  fun create(name: String, args: LuaTable) {
    register(name, args, ItemType.ITEM)
  }

	fun consumable(name: String, args: LuaTable) {
		register(name, args, ItemType.CONSUMABLE)
	}

  fun equipable(name: String, args: LuaTable) {
    register(name, args, ItemType.EQUIPABLE)
  }

  fun weapon(name: String, args: LuaTable) {
    register(name, args, ItemType.WEAPON)
  }
  
  private fun register(name: String, args: LuaTable, type: ItemType) {
    var item: org.rexcellentgames.burningknight.entity.item.Item?

		item = when (type) {
			ItemType.ITEM -> org.rexcellentgames.burningknight.entity.item.Item()
			ItemType.EQUIPABLE -> Equippable()
			ItemType.WEAPON -> Weapon()
			ItemType.CONSUMABLE -> Consumable()
		}

    item.initFromMod(modId, name, args)
		
    ItemRegistry.modItems["$modId:$name"] = item
  }
}