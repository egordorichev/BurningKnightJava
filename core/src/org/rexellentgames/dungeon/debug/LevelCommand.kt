package org.rexcellentgames.burningknight.debug

import org.rexcellentgames.burningknight.Dungeon

class LevelCommand : ConsoleCommand("/level", "/lvl", "Changes level") {
    override fun run(console: Console, args: Array<String>) {
        if (args.size == 1) {
            Dungeon.goToLevel(Integer.valueOf(args[0]))
        }
    }
}