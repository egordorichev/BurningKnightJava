package org.rexcellentgames.burningknight.entity.item

import org.rexcellentgames.burningknight.assets.Locale

class ModItem(val id: String, name: String, sprite: String) : Item(Locale.get(name), Locale.get("${name}_desc"), sprite)