package org.rexellentgames.dungeon.debug

import org.rexellentgames.dungeon.Dungeon

class ArcadeCommand : ConsoleCommand() {
    init {
        shortName = "/ar"
        name = "/arcade"
        description = "Toggles arcade mode"
    }

    override fun run(console: Console, args: Array<String>) {
        Dungeon.type = Dungeon.Type.ARCADE
        Dungeon.newGame()
    }
}