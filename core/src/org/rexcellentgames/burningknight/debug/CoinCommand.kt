package org.rexcellentgames.burningknight.debug

import org.rexcellentgames.burningknight.entity.level.save.GlobalSave

class CoinCommand : ConsoleCommand("/coin", "/c", "Gives coins") {
	override fun run(console: Console, args: Array<String>) {
		super.run(console, args)

		GlobalSave.put("num_coins", 9999)
	}
}