package org.rexcellentgames.burningknight.entity.item.weapon.gun

import org.rexcellentgames.burningknight.entity.creature.mob.Mob
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile

class BadGun : Gun() {
  init {
    useTime = 1.5f
    sprite = "item-gun_a"
  }

  override fun use() {
    this.vel = Mob.shotSpeedMod
    super.use()
  }

  override fun modifyBullet(bullet: BulletProjectile?) {
    super.modifyBullet(bullet)

    bullet!!.bad = true
  }
}