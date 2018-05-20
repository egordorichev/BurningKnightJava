package org.rexellentgames.dungeon.debug

import org.rexellentgames.dungeon.entity.creature.player.Player
import org.rexellentgames.dungeon.entity.item.Item
import org.rexellentgames.dungeon.entity.item.ItemHolder
import org.rexellentgames.dungeon.entity.item.ItemRegistry
import org.rexellentgames.dungeon.ui.UiLog
import kotlin.reflect.full.createInstance

class GiveCommand : ConsoleCommand() {
    init {
        shortName = "/gv"
        name = "/give"
        description = "[item] (count) gives an item"
    }

    override fun run(console: Console, args: Array<String>) {
        var count = 1

        if (args.size == 2) {
            count = Integer.valueOf(args[1])
        }

        if (args.size < 3) {
            val name = args[0]

            try {
                val clazz = ItemRegistry.items[name]
                if (clazz == null) {
                    UiLog.instance.print("[red]Unknown item")
                    return
                }

                val item = clazz.createInstance()
                val itemHolder = ItemHolder()
                itemHolder.item = item

                if (item.isStackable) {
                    item.count = count
                }

                Player.instance.inventory.add(itemHolder)
            } catch (e: Exception) {
                UiLog.instance.print("[red]Failed to create item, consult @egordorichev")
                e.printStackTrace()
            }
        } else {
            UiLog.instance.print("/give [item] (count)")
        }
    }
}