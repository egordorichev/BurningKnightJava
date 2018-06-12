package org.rexellentgames.dungeon.debug

import org.rexellentgames.dungeon.util.Log

class HelpCommand : ConsoleCommand("/help", "/h", "Prints help") {
    override fun run(console: Console, args: Array<String>) {
        for (command in console.commands) {
            Log.info(command.name + " " + command.description)
        }
    }
}