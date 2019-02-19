package org.rexcellentgames.burningknight.entity.item.weapon.gun

import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile

class KotlingGun : Gun() {
	init {
		sprite = "item-kotlin"
		origin.x = 4f
		origin.y = 3f
		hole.x = 13f
		hole.y = 9f
		useTime = 1f
		damage = 3
		vel = 0.7f
	}

	override fun modifyBullet(bullet: BulletProjectile?) {
		super.modifyBullet(bullet)
		bullet!!.second = false
		bullet.rotates = true
	}
}