package org.rexcellentgames.burningknight.debug

import org.rexcellentgames.burningknight.entity.creature.player.Player

class ClearCommand : ConsoleCommand("/clear", "/cl", "Clears your inventory") {
	override fun run(console: Console, args: Array<String>) {
		Player.instance.inventory.clear()
	}
}