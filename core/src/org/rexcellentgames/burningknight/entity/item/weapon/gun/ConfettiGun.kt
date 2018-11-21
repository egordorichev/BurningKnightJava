package org.rexcellentgames.burningknight.entity.item.weapon.gun

import org.rexcellentgames.burningknight.Dungeon
import org.rexcellentgames.burningknight.entity.fx.Confetti
import org.rexcellentgames.burningknight.game.input.Input
import org.rexcellentgames.burningknight.util.Random

class ConfettiGun : Gun() {
	init {
		origin.x = 4f
		origin.y = 3f
		hole.x = 11f
		hole.y = 5f
		accuracy = 20f
		damage = 4
		ammoMax = 1
		sprite = "item-tada_gun"
		reloadRate = 3f
	}

	override fun sendBullets() {
		var a = (this.owner.getAngleTo(Input.instance.worldMouse.x, Input.instance.worldMouse.y) - Math.PI * 2).toFloat()
		val ac = this.accuracy

		for (i in 0 .. (this.level * 2 + 1)) {
			this.sendBullet((a + Math.toRadians(Random.newFloat(-ac, ac).toDouble())).toFloat())
		}

		val aim = this.owner.aim
		a = (this.owner.getAngleTo(aim.x, aim.y) - Math.PI * 2).toFloat()

		for (i in 0..9) {
			val an = (a + Math.toRadians(Random.newFloat(-ac, ac).toDouble())).toFloat()

			val fx = Confetti()

			val x = this.owner.x + this.owner.w / 2
			val y = this.owner.y + this.owner.h / 4 + (region.regionHeight / 2).toFloat() - 2

			fx.x = x + this.getAimX(0f, 0f)
			fx.y = y + this.getAimY(0f, 0f)

			val f = Random.newFloat(40f, 80f) * 1.3f

			fx.vel.x = Math.cos(an.toDouble()).toFloat() * f
			fx.vel.y = Math.sin(an.toDouble()).toFloat() * f

			Dungeon.area.add(fx)
		}
	}

	override fun getMaxLevel(): Int {
		return 6
	}
}