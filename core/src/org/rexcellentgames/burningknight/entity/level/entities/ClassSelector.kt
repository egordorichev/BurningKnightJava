package org.rexcellentgames.burningknight.entity.level.entities

import org.rexcellentgames.burningknight.entity.creature.player.Player
import org.rexcellentgames.burningknight.entity.item.ItemHolder
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Revolver
import org.rexcellentgames.burningknight.entity.item.weapon.magic.MagicMissileWand
import org.rexcellentgames.burningknight.entity.item.weapon.sword.Sword
import org.rexcellentgames.burningknight.util.file.FileReader
import org.rexcellentgames.burningknight.util.file.FileWriter
import java.io.IOException

class ClassSelector : ItemHolder {
  var `class`: String = ""

  constructor() {

  }

  constructor(id: String) {
    `class` = id

    item = when (id) {
      "ranger" -> Revolver()
      "warrior" -> Sword()
      "wizard" -> MagicMissileWand()
      else -> null!!
    }
  }

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
      Player.Type.NONE -> this.`class` == "none"
    }
  }
}