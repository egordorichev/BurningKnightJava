package org.rexcellentgames.burningknight.debug

import org.rexcellentgames.burningknight.physics.World

class DebugCommand : ConsoleCommand("/debug", "/d", "Toggles physical debug") {
    override fun run(console: Console, args: Array<String>) {
        super.run(console, args)

        World.DRAW_DEBUG = !World.DRAW_DEBUG
    }
}