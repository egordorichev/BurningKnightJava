package org.rexcellentgames.burningknight.entity.item.weapon.gun

import org.rexcellentgames.burningknight.util.Random

class BackGun : Gun() {
  init {
    damage = 10
    accuracy = 2f
    origin.x = 4f
    origin.y = 2f
    hole.x = 8f
    hole.y = 12f
    accuracy = 0f
    sprite = "item-back_gun"
  }

  override fun sendBullets() {
    val aim = this.owner.aim
    val a = (this.owner.getAngleTo(aim.x, aim.y) + Math.PI).toFloat()

    this.owner.knockback.x -= (Math.cos(a.toDouble()) * 60f).toFloat()
    this.owner.knockback.y -= (Math.sin(a.toDouble()) * 60f).toFloat()

    this.sendBullet((a + Math.toRadians(Random.newFloat(-this.accuracy, this.accuracy).toDouble())).toFloat())
  }
}