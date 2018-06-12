package org.rexellentgames.dungeon.debug

import org.rexellentgames.dungeon.entity.creature.player.Player

class HealCommand : ConsoleCommand("/heal", "/hl", "Heals you") {
    override fun run(console: Console, args: Array<String>) {
        Player.instance.modifyHp(Player.instance.hpMax - Player.instance.hp, null)
    }
}