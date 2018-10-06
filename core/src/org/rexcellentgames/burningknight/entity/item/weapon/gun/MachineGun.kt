package org.rexcellentgames.burningknight.entity.item.weapon.gun

class MachineGun : Gun() {
  init {
    useTime = 0.1f
    damage = 1
    sprite = "item-gun_b"
  }

  override fun getSfx(): String {
    return "gun_machinegun"
  }
}