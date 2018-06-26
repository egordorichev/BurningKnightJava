package org.rexcellentgames.burningknight.mod

import com.badlogic.gdx.Gdx
import org.rexcellentgames.burningknight.util.Log
import java.util.*

object ModManager {
  private const val modDirectory = "mods/"
  val mods = ArrayList<Mod>()

  fun load() {
    val dir = Gdx.files.internal(modDirectory)

    if (!dir.exists()) {
      Log.error("Mods directory does not exist!")
      return
    }

    val files = dir.list()

    for (file in files) {
      if (file.isDirectory) {
        val mod = Mod.load(file)

        if (mod != null) {
          mods.add(mod)

          mod.init()
        }
      }
    }
  }

  fun update(dt: Float) {
    for (mod in mods) {
      mod.update(dt)
    }
  }

  fun draw() {
    for (mod in mods) {
      mod.draw()
    }
  }

  fun destroy() {
    for (mod in mods) {
      mod.destroy()
    }

    mods.clear()
  }
}