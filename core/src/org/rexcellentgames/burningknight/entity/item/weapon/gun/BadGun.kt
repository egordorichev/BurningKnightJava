package org.rexcellentgames.burningknight.entity.item.weapon.gun

import org.rexcellentgames.burningknight.entity.creature.mob.Mob

class BadGun : Gun() {
  init {
    useTime = 1f
    sprite = "item-gun_a"
	  ammoMax = 1000000
    accuracy = 10f
  }

  override fun use() {
    this.vel = Mob.shotSpeedMod
    super.use()
  }
}