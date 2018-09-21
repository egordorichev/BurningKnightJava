package org.rexcellentgames.burningknight.debug

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector3
import org.rexcellentgames.burningknight.Display
import org.rexcellentgames.burningknight.Dungeon
import org.rexcellentgames.burningknight.entity.Camera
import org.rexcellentgames.burningknight.entity.creature.player.Player
import org.rexcellentgames.burningknight.util.Tween

class ResetCommand : ConsoleCommand("/reset", "/rst", "Generates a new game and player (might freeze)") {
    override fun run(console: Console, args: Array<String>) {
        Dungeon.darkR = Dungeon.MAX_R
        Player.instance.isUnhittable = true
        Camera.follow(null)

        var vec = Camera.game.project(Vector3(Player.instance.x + Player.instance.w / 2, Player.instance.y + Player.instance.h / 2, 0f))
        vec = Camera.ui.unproject(vec)
        vec.y = Display.GAME_HEIGHT - vec.y

        Dungeon.darkX = vec.x
        Dungeon.darkY = vec.y

        Tween.to(object : Tween.Task(0f, 0.3f, Tween.Type.QUAD_OUT) {
            override fun getValue(): Float {
                return Dungeon.darkR
            }

            override fun setValue(value: Float) {
                Dungeon.darkR = value
            }

            override fun onEnd() {
                Dungeon.newGame()
                Dungeon.setBackground2(Color(0f, 0f, 0f, 1f))
            }
        })
    }
}