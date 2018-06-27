package org.rexcellentgames.burningknight.mod

import com.badlogic.gdx.Gdx
import org.rexcellentgames.burningknight.util.Log
import java.util.*

object ModManager {
  private const val modDirectory = "mods/"
  val mods = ArrayList<Mod>()
  private var inited = false

  fun load() {
    if (inited) {
      return
    }

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

    inited = true
  }

  fun update(dt: Float) {
    if (!inited) {
      return
    }

    for (mod in mods) {
      mod.update(dt)
    }
  }

  fun draw() {
    if (!inited) {
      return
    }

    for (mod in mods) {
      mod.draw()
    }
  }

  fun destroy() {
    if (!inited) {
      return
    }

    for (mod in mods) {
      mod.destroy()
    }

    mods.clear()
  }
}