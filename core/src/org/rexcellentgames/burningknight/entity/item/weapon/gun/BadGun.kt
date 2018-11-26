package org.rexcellentgames.burningknight.entity.item.weapon.gun

import org.rexcellentgames.burningknight.entity.creature.mob.Mob

class BadGun : Gun() {
  init {
    useTime = 1f
    sprite = "item-gun_a"
	  ammoMax = 3
    accuracy = 7f
  }

  override fun use() {
    this.vel = Mob.shotSpeedMod * 0.8f
    super.use()
  }
}