package org.rexcellentgames.burningknight.entity.item.mod

import com.badlogic.gdx.graphics.g2d.TextureRegion
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.rexcellentgames.burningknight.assets.Graphics
import org.rexcellentgames.burningknight.assets.Locale
import org.rexcellentgames.burningknight.entity.item.Item

open class ModItem(val modId: String, name: String, args: LuaTable) : Item(Locale.get(name), Locale.get("$modId:${name}_desc"), if(args["sprite"] == LuaValue.NIL) "item-$name" else args["sprite"].toString()) {
  override fun getSprite(): TextureRegion {
    return Graphics.getModTexture(this.modId, sprite)
  }
}