package org.rexcellentgames.burningknight.assets

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader
import org.rexcellentgames.burningknight.mod.Mod

object Locale {
  private val map = HashMap<String, String>()
  private val fallback = HashMap<String, String>()
  lateinit var current: String
    private set

  private fun loadRaw(json: String, modId: String = "", fl: Boolean = false) {
    val reader = JsonReader()
    val root = reader.parse(json)

    for (value in root) {
      if (!modId.isBlank()) {
        if (fl) {
          fallback["$modId:${value.name}"] = value.asString()
        } else {
          map["$modId:${value.name}"] = value.asString()
        }
      } else {
        if (fl) {
          fallback[value.name] = value.asString()
        } else {
          map[value.name] = value.asString()
        }
      }
    }
  }
  
  @JvmStatic
  fun load(locale: String) {
    this.current = locale
    
    map.clear()
    loadRaw(Gdx.files.internal("locales/$locale.json").readString())

    if (locale != "en") {
      loadRaw(Gdx.files.internal("locales/en.json").readString(), "", true)
    }
  }
  
  @JvmStatic
  fun loadForMod(locale: String, mod: Mod) {
    if (mod.localesDirectory != null) {
      for (file in mod.localesDirectory.list()) {
        if (file.name() == "$locale.json") {
          loadRaw(file.readString(), mod.id)
        }
      }
    }
  }

  @JvmStatic
  fun has(name: String): Boolean {
    return map.containsKey(name) || fallback.containsKey(name)
  }

  @JvmStatic
  fun get(name: String): String {
    return map[name] ?: (fallback[name] ?: name)
  }
}