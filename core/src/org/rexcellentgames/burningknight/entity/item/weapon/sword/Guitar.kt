package org.rexcellentgames.burningknight.entity.item.weapon.sword

import org.rexcellentgames.burningknight.Dungeon
import org.rexcellentgames.burningknight.assets.Graphics
import org.rexcellentgames.burningknight.assets.Locale
import org.rexcellentgames.burningknight.entity.creature.fx.Note
import org.rexcellentgames.burningknight.entity.creature.player.Player
import org.rexcellentgames.burningknight.game.input.Input
import org.rexcellentgames.burningknight.util.Random

class Guitar : Sword() {
	override fun setStats() {
		val letter = if (this.level <= 2) "a" else if (this.level <= 4) "b" else "c"

		description = Locale.get("guitar_desc")
		name = Locale.get("guitar_" + letter)
		damage = 4
		sprite = "item-guitar_" + letter
		useTime = 0.5f
		region = Graphics.getTexture(this.sprite)
	}

	override fun use() {
		super.use()

		if (this.level >= 3f) {
			val note = Note()

			val a = Math.max(0f, 10 - (this.owner as Player).accuracy)

			note.a = (this.owner.getAngleTo(Input.instance.worldMouse.x, Input.instance.worldMouse.y) + Math.toRadians(Random.newFloat(-a, a).toDouble())).toFloat()
			note.x = this.owner.x + this.owner.w / 2
			note.y = this.owner.y + this.owner.h / 2
			note.bad = false

			Dungeon.area.add(note)
		}
	}
}