package org.rexcellentgames.burningknight.entity.item.weapon.gun

import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BigBulletProjectile
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile

class CKGun : Gun() {
  init {
    useTime = 0.2f
    damage = 8
    sprite = "item-gun_a"
  }

  override fun sendBullets() {

  }

  fun defaultShot() {
    super.use()
    this.vel = 1f
    super.sendBullets()
  }

  fun bigShot() {
    super.use()
    val aim = this.owner.aim
    val a = (this.owner.getAngleTo(aim.x, aim.y) - Math.PI * 2).toFloat()

    this.vel = 1f
    sendBullet(a, 0f, 0f, BigBulletProjectile())
  }

  fun trippleShot() {
    super.use()
    val aim = this.owner.aim
    val a = (this.owner.getAngleTo(aim.x, aim.y) - Math.PI * 2).toFloat()

    val d = 13f
    this.vel = 2f

    sendBullet(a)
    sendBullet(a, Math.cos(a + Math.PI / 2).toFloat() * d, Math.sin(a + Math.PI / 2).toFloat() * d)
    sendBullet(a, Math.cos(a - Math.PI / 2).toFloat() * d, Math.sin(a - Math.PI / 2).toFloat() * d)
  }

	override fun modifyBullet(bullet: BulletProjectile?) {
		super.modifyBullet(bullet)

		bullet?.parts = true
	}
}