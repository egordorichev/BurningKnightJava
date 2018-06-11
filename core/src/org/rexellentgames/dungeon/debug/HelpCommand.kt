package org.rexellentgames.dungeon.debug

import org.rexellentgames.dungeon.util.Log

class HelpCommand : ConsoleCommand() {
    init {
        name = "/help"
        shortName = "/h"
        description = "prints help"
    }

    override fun run(console: Console, args: Array<String>) {
        for (command in console.commands) {
            Log.info(command.name + " " + command.description)
        }
    }
}