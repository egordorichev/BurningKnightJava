package org.rexcellentgames.burningknight.debug

import com.badlogic.gdx.graphics.Color
import org.rexcellentgames.burningknight.Dungeon
import org.rexcellentgames.burningknight.entity.Camera
import org.rexcellentgames.burningknight.entity.creature.player.Player
import org.rexcellentgames.burningknight.entity.level.entities.Entrance

class LevelCommand : ConsoleCommand("/level", "/lvl", "Changes level") {
    override fun run(console: Console, args: Array<String>) {
        if (args.size == 1) {
	        Player.instance.isUnhittable = true
	        Camera.follow(null)

	        Dungeon.loadType = Entrance.LoadType.GO_DOWN
	        Dungeon.goToLevel(Integer.valueOf(args[0]))
	        Dungeon.setBackground2(Color(0f, 0f, 0f, 1f))
        }
    }
}