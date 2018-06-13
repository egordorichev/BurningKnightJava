package org.rexcellentgames.burningknight.debug

import org.rexcellentgames.burningknight.util.Log

class HelpCommand : ConsoleCommand("/help", "/h", "Prints help") {
    override fun run(console: Console, args: Array<String>) {
        for (command in console.commands) {
            Log.info(command.name + " " + command.description)
        }
    }
}