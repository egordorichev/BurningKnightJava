package org.rexcellentgames.burningknight.entity.item.reference

import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Tear

class IsaacHead : Gun() {
  init {
    ammo = Tear::class.java
    textureA = 90f
    ox = 9
    vel = 2f
  }

  override fun init() {
    super.init()

    this.tw = (this.getSprite().regionHeight / 2).toFloat()
    this.th = this.getSprite().regionWidth.toFloat()
  }

  override fun sendBullets() {
    val aim = this.owner.aim
    val a = (this.owner.getAngleTo(aim.x, aim.y) - Math.PI * 2).toFloat()

    sendBullet(a, Math.cos(a + Math.PI / 2).toFloat() * 2, Math.sin(a + Math.PI / 2).toFloat() * 2)
    sendBullet(a, Math.cos(a - Math.PI / 2).toFloat() * 7, Math.sin(a - Math.PI / 2).toFloat() * 7)
  }
}