package org.rexcellentgames.burningknight.debug

import org.rexcellentgames.burningknight.Dungeon

class ResetCommand : ConsoleCommand("/reset", "/rst", "Generates a new game and player (might freeze)") {
    override fun run(console: Console, args: Array<String>) {
        Dungeon.newGame()
    }
}