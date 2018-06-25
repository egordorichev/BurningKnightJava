package org.rexcellentgames.burningknight.mod

import com.badlogic.gdx.Gdx
import org.rexcellentgames.burningknight.util.Log
import java.util.*

object ModManager {
	val MODS_SAVE_DIR = "mods/"
	val mods = HashMap<String, Mod>()

	fun load() {
		val dir = Gdx.files.internal(MODS_SAVE_DIR)

		if (!dir.exists()) {
			Log.error("Mods directory does not exist!")
			return
		}

		val files = dir.list()

		for (file in files) {
			if (file.isDirectory) {
				val mod = Mod.make(file)

				if (mod != null) {
					mods[mod.name!!] = mod
				}
			}
		}
	}

	fun destroy() {
		for (mod in mods.values) {
			mod.destroy()
		}

		mods.clear()
	}
}