package org.rexcellentgames.burningknight.entity.item.weapon.gun

class MachineGun : Gun() {
  init {
    damage = 1
    sprite = "item-gun_b"
  }

  override fun setStats() {
    super.setStats()
    useTime = 0.1f
  }

  override fun getSfx(): String {
    return "gun_machinegun"
  }
}