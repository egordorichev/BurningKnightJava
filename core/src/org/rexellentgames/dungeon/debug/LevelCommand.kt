package org.rexellentgames.dungeon.debug

import org.rexellentgames.dungeon.Dungeon

class LevelCommand : ConsoleCommand() {
    init {
        name = "/level"
        shortName = "/lvl"
        description = "Switches to given depth"
    }

    override fun run(console: Console, args: Array<String>) {
        if (args.size == 1) {
            Dungeon.goToLevel(Integer.valueOf(args[0]))
        } else {
            UiLog.instance.print("/level [id]")
        }
    }
}