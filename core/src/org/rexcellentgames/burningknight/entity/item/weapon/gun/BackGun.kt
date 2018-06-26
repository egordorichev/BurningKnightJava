package org.rexcellentgames.burningknight.entity.item.weapon.gun

import org.rexcellentgames.burningknight.util.Random

class BackGun : Gun() {
  init {
    damage = 6
    accuracy = 2f
  }

  override fun sendBullets() {
    val aim = this.owner.aim
    val a = (this.owner.getAngleTo(aim.x, aim.y) + Math.PI).toFloat()

    this.sendBullet((a + Math.toRadians(Random.newFloat(-this.accuracy, this.accuracy).toDouble())).toFloat())
  }
}