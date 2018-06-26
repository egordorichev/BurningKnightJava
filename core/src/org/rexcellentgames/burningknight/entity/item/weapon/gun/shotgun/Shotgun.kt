package org.rexcellentgames.burningknight.entity.item.weapon.gun.shotgun

import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun
import org.rexcellentgames.burningknight.util.Random

open class Shotgun : Gun() {
  init {
    accuracy = 20f
    useTime = 3f
    damage = 3
  }

  override fun sendBullets() {
    val aim = this.owner.aim
    val an = this.owner.getAngleTo(aim.x, aim.y)

    for (i in 0..2) {
      val a = (an - Math.PI * 2).toFloat()
      this.sendBullet((a + Math.toRadians(Random.newFloat(-this.accuracy, this.accuracy).toDouble())).toFloat())
    }
  }
}