package org.rexellentgames.dungeon.debug

import org.rexellentgames.dungeon.Dungeon

class LevelCommand : ConsoleCommand("/level", "/lvl", "Changes level") {
    override fun run(console: Console, args: Array<String>) {
        if (args.size == 1) {
            Dungeon.goToLevel(Integer.valueOf(args[0]))
        }
    }
}