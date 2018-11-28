package org.rexcellentgames.burningknight.debug

import org.rexcellentgames.burningknight.entity.creature.player.Player
import org.rexcellentgames.burningknight.entity.item.Item
import org.rexcellentgames.burningknight.entity.item.ItemHolder
import org.rexcellentgames.burningknight.entity.item.ItemRegistry
import org.rexcellentgames.burningknight.util.Log

class GiveCommand : ConsoleCommand("/give", "/gv", "[item] (count) gives an item") {
  override fun run(console: Console, args: Array<String>) {
    var count = 1

    if (args.size == 2) {
      count = Integer.valueOf(args[1])
    }

    if (args.size > 0 && args.size < 3) {
      val name = args[0]
      val item: Item
      
      try {
        item = if (name.contains(":")) {
          ItemRegistry.modItems[name] as Item
        } else {
          val clazz = ItemRegistry.items[name]

          if (clazz == null) {
            Log.error("Unknown item $name")

            return
          }

          clazz.type.newInstance()
        }

        if (item.isStackable) {
          item.count = count
        }
        
        val itemHolder = ItemHolder()
        itemHolder.item = item
        
        Player.instance.tryToPickup(itemHolder)
      } catch (e: Exception) {
        Log.error("Failed to create item, consult @egordorichev")
        e.printStackTrace()
      }
    }
  }
}