package org.rexcellentgames.burningknight.mod

import org.rexcellentgames.burningknight.Dungeon

class Game {
  fun slowmo(amount: Float, time: Float) {
    Dungeon.slowDown(amount, time)
  }

  fun glitch(time: Float) {
    Dungeon.glitchTime = time
  }
}