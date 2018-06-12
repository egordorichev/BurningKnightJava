package org.rexellentgames.dungeon.debug

import org.rexellentgames.dungeon.Dungeon

class ResetCommand : ConsoleCommand("/reset", "/rst", "Generates a new game and player (might freeze)") {
    override fun run(console: Console, args: Array<String>) {
        Dungeon.newGame()
    }
}