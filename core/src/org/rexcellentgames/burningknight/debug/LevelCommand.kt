package org.rexcellentgames.burningknight.debug

import org.rexcellentgames.burningknight.Dungeon
import org.rexcellentgames.burningknight.entity.level.entities.Entrance

class LevelCommand : ConsoleCommand("/level", "/lvl", "Changes level") {
    override fun run(console: Console, args: Array<String>) {
        if (args.size == 1) {
            Dungeon.loadType = Entrance.LoadType.GO_DOWN
            Dungeon.goToLevel(Integer.valueOf(args[0]))
        }
    }
}