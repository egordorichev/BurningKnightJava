package org.rexellentgames.dungeon.debug

import org.rexellentgames.dungeon.game.state.InGameState
import org.rexellentgames.dungeon.physics.World

class DebugCommand : ConsoleCommand() {
    init {
        shortName = "/d"
        name = "/debug"
        description = "Toggles physical debug"
    }

    override fun run(console: Console, args: Array<String>) {
        super.run(console, args)

        World.DRAW_DEBUG = !World.DRAW_DEBUG
    }
}