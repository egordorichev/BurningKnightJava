package org.rexcellentgames.burningknight.mod

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.JsonReader
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.jse.JsePlatform
import org.rexcellentgames.burningknight.assets.Graphics
import org.rexcellentgames.burningknight.assets.Locale
import org.rexcellentgames.burningknight.mod.item.Item
import org.rexcellentgames.burningknight.util.Log

class Mod(val name: String, val id: String, val description: String, val author: String, val version: String, val main: FileHandle, val itemsDirectory: FileHandle?, val localesDirectory: FileHandle?, val spritesDirectory: FileHandle?) {
  private var updateCallback: LuaValue? = null
  private var drawCallback: LuaValue? = null
  private val globals: Globals = JsePlatform.standardGlobals()

  fun init() {
    Locale.loadForMod(Locale.current, this)
    Graphics.loadModAssets(this)

    globals.loadfile(main.path()).call()

    this.updateCallback = globals.get("update")

    if (updateCallback != null && !updateCallback!!.isfunction()) {
      this.updateCallback = null
    }

    this.drawCallback = globals.get("draw")

    if (drawCallback != null && !drawCallback!!.isfunction()) {
      this.drawCallback = null
    }

    val initCallback = globals.get("init")

    if (initCallback != null && initCallback.isfunction()) {
      initCallback.call()
    }

    val apiCode = "item = luajava.newInstance(\"${Item::class.qualifiedName}\", \"$id\")\ngame = luajava.newInstance(\"${Game::class.qualifiedName}\", \"$id\")"

    globals.load(apiCode).call()

    if (this.itemsDirectory != null) {
      this.parseDirectory(itemsDirectory.list())
    }
  }

  private fun parseDirectory(dir: Array<FileHandle>) {
    for (entry in dir) {
      if (entry.isDirectory) {
        parseDirectory(entry.list())
      } else if (entry.extension() == "lua") {
        try {
          globals.load(entry.readString()).call()
        } catch (error: LuaError) {
          Log.error("Internal mod error!")
          error.printStackTrace()
        }
      }
    }
  }

  fun destroy() {
    val destroyCallback = globals.get("destroy")

    if (destroyCallback != null && destroyCallback.isfunction()) {
      destroyCallback.call()
    }
  }

  fun update(dt: Float) {
    this.updateCallback?.call(LuaValue.valueOf(dt.toDouble()))
  }

  fun draw() {
    this.drawCallback?.call()
  }

  companion object {
    fun load(folder: FileHandle): Mod? {
      Log.info("Loading mod '" + folder.name() + "'...")

      if (!folder.isDirectory) {
        Log.error("Mod folder is not a directory!")
        return null
      }

      val files = folder.list()

      if (files.isEmpty()) {
        Log.error("Mod folder is empty!")
        return null
      }

      var main: FileHandle? = null
      var info: FileHandle? = null
      var items: FileHandle? = null
      var locales: FileHandle? = null
      var sprites: FileHandle? = null
      
      for (file in files) {
        if (file.nameWithoutExtension() == "main" && file.extension() == "lua") {
          main = file
        } else if (file.nameWithoutExtension() == "mod" && file.extension() == "json") {
          info = file
        } else if (file.isDirectory && file.name() == "items") {
          items = file
        } else if (file.isDirectory && file.name() == "locales") {
          locales = file
        } else if (file.isDirectory && file.name() == "sprites") {
          sprites = file
        }
      }

      if (main == null) {
        Log.error("main.lua was not found!")
        return null
      }

      if (info == null) {
        Log.error("mod.json was not found!")
        return null
      }

      val root = JsonReader().parse(info)

      return Mod(root.getString("name", "Name Missing"), root.getString("id"), root.getString("description"), root.getString("author", "Anonymous"), root.getString("version", "Version Missing"), main, items, locales, sprites)
    }
  }
}