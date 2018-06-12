package org.rexellentgames.dungeon.debug

import org.rexellentgames.dungeon.physics.World

class DebugCommand : ConsoleCommand("/debug", "/d", "Toggles physical debug") {
    override fun run(console: Console, args: Array<String>) {
        super.run(console, args)

        World.DRAW_DEBUG = !World.DRAW_DEBUG
    }
}