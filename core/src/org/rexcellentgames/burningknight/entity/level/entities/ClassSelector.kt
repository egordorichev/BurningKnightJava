package org.rexcellentgames.burningknight.entity.level.entities

import org.rexcellentgames.burningknight.entity.creature.player.Player
import org.rexcellentgames.burningknight.entity.item.ItemHolder
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Revolver
import org.rexcellentgames.burningknight.entity.item.weapon.magic.MagicMissileWand
import org.rexcellentgames.burningknight.entity.item.weapon.sword.SwordA
import org.rexcellentgames.burningknight.util.file.FileReader
import org.rexcellentgames.burningknight.util.file.FileWriter

import java.io.IOException

class ClassSelector(id: String) : ItemHolder(when (id) {
  "ranger" -> Revolver()
  "warrior" -> SwordA()
  "wizard" -> MagicMissileWand()
  else -> null!!
}) {
  var `class`: String = id

  @Throws(IOException::class)
  override fun load(reader: FileReader) {
    super.load(reader)
    this.`class` = reader.readString()
  }

  @Throws(IOException::class)
  override fun save(writer: FileWriter) {
    super.save(writer)
    writer.writeString(this.`class`)
  }

  fun same(type: Player.Type): Boolean {
    return when (type) {
      Player.Type.WARRIOR -> this.`class` == "warrior"
      Player.Type.WIZARD -> this.`class` == "wizard"
      Player.Type.RANGER -> this.`class` == "ranger"
    }
  }
}