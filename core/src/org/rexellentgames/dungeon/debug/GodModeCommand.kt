package org.rexellentgames.dungeon.debug

import org.rexellentgames.dungeon.entity.creature.player.Player

class GodModeCommand : ConsoleCommand() {
    init {
        name = "/godmode"
        shortName = "/gm"
        description = "makes you unkillable"
    }

    override fun run(console: Console, args: Array<String>) {
        Player.instance.isUnhittable = !Player.instance.isUnhittable
    }
}