package org.rexcellentgames.burningknight.mod

import com.badlogic.gdx.files.FileHandle
import org.rexcellentgames.burningknight.util.Log

class Mod {
	var name: String? = null
	var description: String? = null

	fun init() {

	}

	fun destroy() {

	}

	fun update(dt: Float) {

	}

	fun draw() {

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

			return mod
		}
	}
}