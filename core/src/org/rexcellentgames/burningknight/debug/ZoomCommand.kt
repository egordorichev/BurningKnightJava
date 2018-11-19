package org.rexcellentgames.burningknight.debug


import com.badlogic.gdx.Gdx
import org.rexcellentgames.burningknight.Display

class ZoomCommand : ConsoleCommand("/zoom", "/z", "Zoom [int]") {

	override fun run(console: Console, args: Array<String>) {
		if (args.isEmpty()) {
			return
		}

		val zoom = Math.max(0, Integer.valueOf(args[0]))

		Gdx.graphics.setWindowedMode(Display.GAME_WIDTH * zoom, Display.GAME_HEIGHT * zoom)
	}
}