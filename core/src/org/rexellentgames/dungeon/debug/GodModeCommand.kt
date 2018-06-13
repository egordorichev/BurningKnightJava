package org.rexcellentgames.burningknight.debug

import org.rexcellentgames.burningknight.entity.creature.player.Player

class GodModeCommand : ConsoleCommand("/godmode", "/gm", "Invincibility") {
    override fun run(console: Console, args: Array<String>) {
        Player.instance.isUnhittable = !Player.instance.isUnhittable
    }
}