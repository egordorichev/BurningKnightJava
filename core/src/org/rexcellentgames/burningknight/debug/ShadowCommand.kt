package org.rexcellentgames.burningknight.debug

import org.rexcellentgames.burningknight.entity.level.Level

class ShadowCommand : ConsoleCommand("/shadow", "/sd", "Toggles shadows") {
    override fun run(console: Console, args: Array<String>) {
        super.run(console, args)

        Level.SHADOWS = !Level.SHADOWS
    }
}