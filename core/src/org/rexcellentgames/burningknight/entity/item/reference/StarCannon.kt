package org.rexcellentgames.burningknight.entity.item.reference

import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Star

class StarCannon : Gun() {
  init {
    damage = 4
    ammo = Star::class.java
    accuracy = 1f
    penetrates = true
  }
}