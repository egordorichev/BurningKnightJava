package org.rexellentgames.dungeon.debug

open class ConsoleCommand {
    var name: String? = null
        protected set
    var shortName: String? = null
        protected set
    var description: String? = null
        protected set

    open fun run(console: Console, args: Array<String>) {

    }
}