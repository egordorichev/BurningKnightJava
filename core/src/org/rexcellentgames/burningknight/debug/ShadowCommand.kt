package org.rexcellentgames.burningknight.debug

import org.rexcellentgames.burningknight.entity.level.Level

class ShadowCommand : ConsoleCommand("/shadow", "/sd", "Toggles shadows") {
	override fun run(console: Console, args: Array<String>) {
		Level.SHADOWS = !Level.SHADOWS
	}
}