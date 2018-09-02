package org.rexcellentgames.burningknight.entity.item.accessory.equippable

import org.rexcellentgames.burningknight.Dungeon
import org.rexcellentgames.burningknight.assets.Locale
import org.rexcellentgames.burningknight.util.Tween

class VVVVV : Equippable() {
	private var lastTween: Tween.Task? = null

	init {
		name = Locale.get("vvvvv")
		description = Locale.get("vvvvv_desc")
		sprite = "item-vvvvv"
	}

	override fun canBeUpgraded(): Boolean {
		return false
	}

	override fun canBeDegraded(): Boolean {
		return false
	}

	override fun onEquip(load: Boolean) {
		super.onEquip(load)

		Tween.remove(lastTween)
		lastTween = Tween.to(object : Tween.Task(1f, 0.4f) {
			override fun getValue(): Float {
				return Dungeon.flip
			}

			override fun setValue(value: Float) {
				Dungeon.flip = value
			}
		})

		this.owner.goldModifier += 0.3f
	}

	override fun onUnequip(load: Boolean) {
		super.onEquip(load)

		Tween.remove(lastTween)
		lastTween = Tween.to(object : Tween.Task(-1f, 0.4f) {
			override fun getValue(): Float {
				return Dungeon.flip
			}

			override fun setValue(value: Float) {
				Dungeon.flip = value
			}
		})

		this.owner.goldModifier -= 0.3f
	}
}