package org.rexcellentgames.burningknight.entity.item.weapon.gun

import org.rexcellentgames.burningknight.util.Random

class BackGun : Gun() {
  init {
    damage = 6
    accuracy = 2f
    origin.x = 4f
    origin.y = 2f
    hole.x = 8f
    hole.y = 12f
    accuracy = 0f
  }

  override fun sendBullets() {
    val aim = this.owner.aim
    val a = (this.owner.getAngleTo(aim.x, aim.y) + Math.PI).toFloat()

    this.owner.vel.x -= (Math.cos(a.toDouble()) * 80f).toFloat()
    this.owner.vel.y -= (Math.sin(a.toDouble()) * 80f).toFloat()

    this.sendBullet((a + Math.toRadians(Random.newFloat(-this.accuracy, this.accuracy).toDouble())).toFloat())
  }
}