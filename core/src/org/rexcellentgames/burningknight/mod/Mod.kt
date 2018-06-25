package org.rexcellentgames.burningknight.mod

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.JsonReader
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.jse.JsePlatform
import org.rexcellentgames.burningknight.util.Log

class Mod {
	var name: String? = null
	var description: String? = null
	var author: String? = null

	private var updateCallback: LuaValue? = null
	private var drawCallback: LuaValue? = null
	val globals = JsePlatform.standardGlobals()

	fun init() {
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
	}

	fun destroy() {
		val disposeCallback = globals.get("destroy")

		if (disposeCallback != null && disposeCallback.isfunction()) {
			disposeCallback.call()
		}
	}

	fun update(dt: Float) {
		this.updateCallback?.call(LuaValue.valueOf(dt.toDouble()))
	}

	fun draw() {
		this.drawCallback?.call()
	}

	companion object {
		fun make(folder: FileHandle): Mod? {
			val mod = Mod()

			Log.info("Loading mod '" + folder.name() + "'...")

			if (!folder.isDirectory) {
				Log.error("Mod folder is not a directory!")
				return null
			}

			val files = folder.list()

			if (files.size == 0) {
				Log.error("Mod folder is empty!")
				return null
			}

			var main: FileHandle? = null
			var info: FileHandle? = null
			var items: FileHandle? = null

			for (file in files) {
				if (file.nameWithoutExtension() == "main" && file.extension() == "lua") {
					main = file
				} else if (file.nameWithoutExtension() == "mod" && file.extension() == "json") {
					info = file
				} else if (file.isDirectory && file.name() == "items") {
					items = file
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

			if (items == null) {
				Log.error("Warning: items folder was not found!")
				return null
			}

			val root = JsonReader().parse(info)

			mod.name = root.getString("name", "Missing name")
			mod.description = root.getString("description", "Missing description")
			mod.author = root.getString("author", "Someone")

			mod.globals.loadfile(main.path()).call()

			return mod
		}
	}
}