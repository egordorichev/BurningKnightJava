package org.rexcellentgames.burningknight.debug

import org.rexcellentgames.burningknight.entity.creature.player.Player

class DieCommand : ConsoleCommand("/die", "de", "Death") {
    override fun run(console: Console, args: Array<String>) {
        if (Player.instance != null) {
            Player.instance.die()
        }
    }
}