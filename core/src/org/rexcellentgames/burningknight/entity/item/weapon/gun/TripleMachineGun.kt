package org.rexcellentgames.burningknight.entity.item.weapon.gun

import org.rexcellentgames.burningknight.game.input.Input

class TripleMachineGun : Gun() {
  init {
    sprite = "item-gun_c"
  }

  override fun setStats() {
    super.setStats()
    useTime = 0.15f
  }

  override fun sendBullets() {
    val a = (this.owner.getAngleTo(Input.instance.worldMouse.x, Input.instance.worldMouse.y) - Math.PI * 2).toFloat()

    this.sendBullet((a + Math.toRadians(10.0)).toFloat())
    this.sendBullet((a - Math.toRadians(10.0)).toFloat())
    this.sendBullet(a)
  }
}