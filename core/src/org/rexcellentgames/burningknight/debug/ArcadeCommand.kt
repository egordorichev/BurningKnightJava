package org.rexcellentgames.burningknight.debug

import org.rexcellentgames.burningknight.Dungeon

class ArcadeCommand: ConsoleCommand("/arcade", "/ar", "Toggles arcade mode") {
    override fun run(console: Console, args: Array<String>) {
        Dungeon.type = Dungeon.Type.ARCADE
        Dungeon.newGame()
    }
}