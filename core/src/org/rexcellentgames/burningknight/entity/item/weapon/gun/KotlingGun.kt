package org.rexcellentgames.burningknight.entity.item.weapon.gun

import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Kotlin

class KotlingGun : Gun() {
	init {
		sprite = "item-kotlin"
		origin.x = 4f
		origin.y = 3f
		hole.x = 11f
		hole.y = 6f
		useTime = 1f
		damage = 3
		vel = 0.7f
		ammo = Kotlin::class.java
	}
}