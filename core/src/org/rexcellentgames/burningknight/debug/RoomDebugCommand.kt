package org.rexcellentgames.burningknight.debug

import org.rexcellentgames.burningknight.entity.level.Level

class RoomDebugCommand : ConsoleCommand("/room", "/r", "Some room debug") {
	override fun run(console: Console, args: Array<String>) {
		Level.RENDER_ROOM_DEBUG = !Level.RENDER_ROOM_DEBUG
	}
}