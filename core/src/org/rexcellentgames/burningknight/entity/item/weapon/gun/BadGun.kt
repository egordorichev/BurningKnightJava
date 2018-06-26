package org.rexcellentgames.burningknight.entity.item.weapon.gun

import org.rexcellentgames.burningknight.entity.creature.mob.Mob

class BadGun : Gun() {
  init {
    useTime = 1.5f
    sprite = "item-gun_a";
  }

  override fun use() {
    this.vel = Mob.shotSpeedMod
    
    super.use()
  }
}