package org.rexcellentgames.burningknight.debug

import org.rexcellentgames.burningknight.entity.creature.player.Player

class HealCommand : ConsoleCommand("/heal", "/hl", "Heals you") {
    override fun run(console: Console, args: Array<String>) {
        Player.instance.modifyHp(Player.instance.hpMax - Player.instance.hp, null)
    }
}