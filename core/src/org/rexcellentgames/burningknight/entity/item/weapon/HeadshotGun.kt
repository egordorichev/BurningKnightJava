package org.rexcellentgames.burningknight.entity.item.weapon

import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun
import org.rexcellentgames.burningknight.util.Random

class HeadshotGun : Gun() {
	init {
		damage = 3
		accuracy = 6f
		origin.x = 8f
		origin.y = 4f
		hole.x = 0f
		hole.y = 6f
		sprite = "item-gun_j"
	}

	override fun sendBullets() {
		val aim = this.owner.aim
		val a = (this.owner.getAngleTo(aim.x, aim.y) + Math.PI).toFloat()

		this.sendBullet((a + Math.toRadians(Random.newFloat(-this.accuracy, this.accuracy).toDouble())).toFloat())
	}
}