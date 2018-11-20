package org.rexcellentgames.burningknight.debug

import org.rexcellentgames.burningknight.entity.creature.player.Player

class HurtCommand : ConsoleCommand("/hurt", "/h", "Ouch") {

	override fun run(console: Console, args: Array<String>) {
		Player.instance.modifyHp(-1, null, true)
	}
}