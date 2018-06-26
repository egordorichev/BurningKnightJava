package org.rexcellentgames.burningknight.debug

import org.rexcellentgames.burningknight.entity.level.LightLevel

class LightCommand : ConsoleCommand("/light", "/lt", "Toggles light") {
  override fun run(console: Console, args: Array<String>) {
    LightLevel.LIGHT = !LightLevel.LIGHT
  }
}