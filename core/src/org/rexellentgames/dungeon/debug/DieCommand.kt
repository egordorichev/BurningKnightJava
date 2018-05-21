package org.rexellentgames.dungeon.debug

import org.rexellentgames.dungeon.entity.creature.player.Player

class DieCommand : ConsoleCommand() {
    init {
        shortName = "/de"
        name = "/die"
        description = "Death"
    }

    override fun run(console: Console, args: Array<String>) {
        if (Player.instance != null) {
            Player.instance.die()
        }
    }
}