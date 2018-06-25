package org.rexcellentgames.burningknight.entity.item

import org.rexcellentgames.burningknight.assets.Locale

class ModItem(val id: String, name: String, description: String, sprite: String) : Item(Locale.get(name), description, sprite) {
}