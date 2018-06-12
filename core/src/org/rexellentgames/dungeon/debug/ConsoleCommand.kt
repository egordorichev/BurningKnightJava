package org.rexellentgames.dungeon.debug

abstract class ConsoleCommand(var name: String, var shortName: String, var description: String) {
    open fun run(console: Console, args: Array<String>) {

    }
}