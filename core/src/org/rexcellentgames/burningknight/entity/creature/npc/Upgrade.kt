package org.rexcellentgames.burningknight.entity.creature.npc

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3
import org.rexcellentgames.burningknight.Dungeon
import org.rexcellentgames.burningknight.assets.Graphics
import org.rexcellentgames.burningknight.entity.creature.mob.Mob
import org.rexcellentgames.burningknight.entity.item.Item
import org.rexcellentgames.burningknight.entity.item.ItemRegistry
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase
import org.rexcellentgames.burningknight.entity.level.SaveableEntity
import org.rexcellentgames.burningknight.util.MathUtils
import org.rexcellentgames.burningknight.util.file.FileReader
import org.rexcellentgames.burningknight.util.file.FileWriter
import java.io.IOException

class Upgrade : SaveableEntity() {

	var type = Type.PERMANENT
	private var item: Item? = null
	private var str: String? = null

	private var hidden: Boolean = false

	init {
		depth = -1
	}

	enum class Type private constructor(id: Int) {
		CONSUMABLE(0),
		WEAPON(1),
		ACCESSORY(2),
		PET(3),
		PERMANENT(4),
		NONE(5);

		internal var id: Byte = 0

		init {
			this.id = id.toByte()
		}
	}

	@Throws(IOException::class)
	override fun load(reader: FileReader) {
		super.load(reader)
		type = Type.values()[reader.readByte().toInt()]

		try {
			this.str = reader.readString()

			if (str != null) {
				this.item = ItemRegistry.items[this.str!!]!!.type.newInstance()
			}
		} catch (e: InstantiationException) {
			e.printStackTrace()
		} catch (e: IllegalAccessException) {
			e.printStackTrace()
		}

	}

	protected var z = 0f

	protected fun generateItem(): Item? {
		for ((key, value) in ItemRegistry.items) {
			if (!value.busy && value.pool == this.type && !value.unlocked()) {
				this.str = key
				value.busy = true

				try {
					return value.type.newInstance()
				} catch (e: InstantiationException) {
					e.printStackTrace()
				} catch (e: IllegalAccessException) {
					e.printStackTrace()
				}

			}
		}

		return null
	}

	@Throws(IOException::class)
	override fun save(writer: FileWriter) {
		super.save(writer)

		this.checkItem()

		writer.writeByte(type.id)
		writer.writeString(this.str)
	}

	override fun update(dt: Float) {
		if (this.hidden) {
			return
		}

		this.checkItem()

		this.z += (Math.cos((this.t * 1.7f).toDouble()) / 5f * dt.toDouble() * 60.0).toFloat()
		this.z = MathUtils.clamp(0f, 5f, this.z)

		super.update(dt)
	}

	private fun checkItem() {
		if (this.item == null) {
			this.item = this.generateItem()

			if (this.item == null) {
				this.hidden = true
			} else {
				this.w = this.item!!.sprite.regionWidth.toFloat()
				this.w = this.item!!.sprite.regionHeight.toFloat()
			}
		}
	}

	private var al = 0f

	override fun render() {
		if (hidden || item == null) {
			return
		}

		val sprite = this.item!!.getSprite()

		val a = Math.cos((this.t * 3f).toDouble()).toFloat() * 8f
		val sy = (1f + Math.sin((this.t * 2f).toDouble()) / 10f).toFloat()

		Graphics.batch.end()

		val dt = Gdx.graphics.deltaTime
		this.al = MathUtils.clamp(0f, 1f, this.al + ((if (false) 1 else 0) - this.al) * dt * 10f)

		if (this.al > 0) {
			Mob.shader.begin()
			Mob.shader.setUniformf("u_color", Vector3(1f, 1f, 1f))
			Mob.shader.setUniformf("u_a", this.al)
			Mob.shader.end()
			Graphics.batch.shader = Mob.shader
			Graphics.batch.begin()

			for (xx in -1..1) {
				for (yy in -1..1) {
					if (Math.abs(xx) + Math.abs(yy) == 1) {
						Graphics.render(sprite, this.x + (16 - w) / 2 + xx.toFloat(), this.y + z + (16 - h) / 2 + yy.toFloat(), a, w / 2, h / 2, false, false, 1f, sy)
					}
				}
			}

			Graphics.batch.end()
			Graphics.batch.shader = null
		}

		WeaponBase.shader.begin()
		WeaponBase.shader.setUniformf("a", 1f)
		WeaponBase.shader.setUniformf("gray", 1f)
		WeaponBase.shader.setUniformf("time", Dungeon.time + this.t)
		WeaponBase.shader.end()
		Graphics.batch.shader = WeaponBase.shader
		Graphics.batch.begin()

		Graphics.render(sprite, this.x + (16 - w) / 2, this.y + z + (16 - h) / 2, a, w / 2, h / 2, false, false, 1f, sy)

		Graphics.batch.end()
		Graphics.batch.shader = null
		Graphics.batch.begin()
	}
}