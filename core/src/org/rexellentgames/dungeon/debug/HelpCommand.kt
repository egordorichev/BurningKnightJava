package org.rexellentgames.dungeon.debug

class HelpCommand : ConsoleCommand() {
    init {
        name = "/help"
        shortName = "/h"
        description = "prints help"
    }

    override fun run(console: Console, args: Array<String>) {
        for (command in console.commands) {
            UiLog.instance.print(command.name + " " + command.description)
        }
    }
}