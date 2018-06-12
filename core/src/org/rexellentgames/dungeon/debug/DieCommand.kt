package org.rexellentgames.dungeon.debug

import org.rexellentgames.dungeon.entity.creature.player.Player

class DieCommand : ConsoleCommand("/die", "de", "Death") {
    override fun run(console: Console, args: Array<String>) {
        if (Player.instance != null) {
            Player.instance.die()
        }
    }
}