package org.rexellentgames.dungeon.debug

import org.rexellentgames.dungeon.entity.level.LightLevel

class LightCommand : ConsoleCommand("/light", "/lt", "Toggles light") {
    override fun run(console: Console, args: Array<String>) {
        LightLevel.LIGHT = !LightLevel.LIGHT
    }
}