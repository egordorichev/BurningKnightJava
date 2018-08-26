package org.rexcellentgames.burningknight.entity.item.weapon.gun

class Izu : Gun() {
  init {
    sprite = "item-gun_d"
    useTime = 0.1f
    origin.x = 5f
    origin.y = 7f
    hole.x = 10f
    hole.y = 10f
	  accuracy = 1f
	  ammoMax = 19
  }

	override fun onAmmoAdded() {
		super.onAmmoAdded()
		usedTime = 0
	}

	override fun getUseTimeGun() : Float {
		if (this.usedTime >= 3f) {
			this.usedTime = 0
			return 0.5f
		}

		return super.getUseTime()
	}
}