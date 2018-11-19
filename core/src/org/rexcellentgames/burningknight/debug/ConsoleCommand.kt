package org.rexcellentgames.burningknight.debug

abstract class ConsoleCommand(var name: String, var shortName: String, var description: String) {
	abstract fun run(console: Console, args: Array<String>)
}