package org.rexcellentgames.burningknight.mod.item

import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.rexcellentgames.burningknight.entity.item.ItemRegistry
import org.rexcellentgames.burningknight.entity.item.accessory.equipable.Equipable
import org.rexcellentgames.burningknight.entity.item.consumable.Consumable
import org.rexcellentgames.burningknight.entity.item.weapon.Weapon
import org.rexcellentgames.burningknight.entity.pool.item.AccessoryPoolAll
import org.rexcellentgames.burningknight.entity.pool.item.AccessoryPoolMage
import org.rexcellentgames.burningknight.entity.pool.item.AccessoryPoolRanger
import org.rexcellentgames.burningknight.entity.pool.item.AccessoryPoolWarrior

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
    var item: org.rexcellentgames.burningknight.entity.item.Item? = null

    when (type) {
      ItemType.ITEM -> item = org.rexcellentgames.burningknight.entity.item.Item()
      ItemType.EQUIPABLE -> item = Equipable()
      ItemType.WEAPON -> item = Weapon()
      ItemType.CONSUMABLE -> item = Consumable()
    }

    item.initFromMod(modId, name, args)
    ItemRegistry.modItems["$modId:$name"] = item

    val pool = args.get("pool")

    if (pool !== LuaValue.NIL && pool.isstring()) {
      val pool = pool.toString()
      var chance = 1f
      val chanceField = args.get("chance")

      if (chanceField !== LuaValue.NIL && chanceField.isnumber()) {
        chance = chanceField.tofloat()
      }

      when (pool) {
        "accessory_all" -> AccessoryPoolAll.instance.add(item.javaClass, chance)
        "accessory_mage" -> AccessoryPoolMage.instance.add(item.javaClass, chance)
        "accessory_warrior" -> AccessoryPoolWarrior.instance.add(item.javaClass, chance)
        "accessory_ranged" -> AccessoryPoolRanger.instance.add(item.javaClass, chance)
      }
    }
  }
}