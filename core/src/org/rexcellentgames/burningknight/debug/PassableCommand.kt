package org.rexcellentgames.burningknight.debug

import org.rexcellentgames.burningknight.entity.level.Level

class PassableCommand : ConsoleCommand("/pas", "/p", "Some debug") {

	override fun run(console: Console, args: Array<String>) {
		Level.RENDER_PASSABLE = !Level.RENDER_PASSABLE
	}
}