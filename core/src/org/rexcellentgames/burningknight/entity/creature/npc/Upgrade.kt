package org.rexcellentgames.burningknight.entity.creature.npc

import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import org.rexcellentgames.burningknight.Dungeon
import org.rexcellentgames.burningknight.assets.Audio
import org.rexcellentgames.burningknight.assets.Graphics
import org.rexcellentgames.burningknight.entity.Camera
import org.rexcellentgames.burningknight.entity.Entity
import org.rexcellentgames.burningknight.entity.creature.mob.Mob
import org.rexcellentgames.burningknight.entity.creature.player.Player
import org.rexcellentgames.burningknight.entity.fx.Confetti
import org.rexcellentgames.burningknight.entity.item.Item
import org.rexcellentgames.burningknight.entity.item.ItemRegistry
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase
import org.rexcellentgames.burningknight.entity.level.SaveableEntity
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave
import org.rexcellentgames.burningknight.game.Achievements
import org.rexcellentgames.burningknight.game.Ui
import org.rexcellentgames.burningknight.game.input.Input
import org.rexcellentgames.burningknight.physics.World
import org.rexcellentgames.burningknight.util.MathUtils
import org.rexcellentgames.burningknight.util.Random
import org.rexcellentgames.burningknight.util.file.FileReader
import org.rexcellentgames.burningknight.util.file.FileWriter
import java.io.IOException

class Upgrade : SaveableEntity() {
	companion object {
		var all = ArrayList<Upgrade>()
		var activeUpgrade: Upgrade? = null
	}

	var type = Type.PERMANENT
	private var item: Item? = null
	private var str: String? = null
	protected var z = 0f
	private var body: Body? = null
	private var price = 0
	private var costStr = ""
	private var costW = 0
	private var nameW = 0
	var idd = ""

	private var hidden: Boolean = false

	enum class Type constructor(id: Int) {
		CONSUMABLE(0),
		WEAPON(1),
		ACCESSORY(2),
		PET(3),
		PERMANENT(4),
		NONE(5),
		DECOR(6);

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
			this.idd = reader.readString()
			this.str = reader.readString()

			if (str != null) {
				val pair = ItemRegistry.items[this.str!!]!!

				pair.busy = true

				this.item = pair.type.newInstance()
				this.price = pair.cost
				this.setupInfo()

				this.createBody()
			}
		} catch (e: InstantiationException) {
			e.printStackTrace()
		} catch (e: IllegalAccessException) {
			e.printStackTrace()
		}
	}

	override fun init() {
		super.init()
		this.t = Random.newFloat(10f)
		all.add(this)
	}

	protected fun generateItem(): Item? {
		for ((key, value) in ItemRegistry.items) {
			if (!value.busy && value.pool == this.type && !value.unlocked()) {
				this.str = key
				value.busy = true

				try {
					this.price = value.cost
					this.setupInfo()

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

	private fun setupInfo() {
		if (this.item == null) {
			this.costStr = ""
			return
		}

		this.costStr = "" + this.price

		Graphics.layout.setText(Graphics.small, this.costStr)
		this.costW = Graphics.layout.width.toInt()

		Graphics.layout.setText(Graphics.medium, this.item!!.name)
		this.nameW = Graphics.layout.width.toInt()
	}

	private var checked = false

	@Throws(IOException::class)
	override fun save(writer: FileWriter) {
		super.save(writer)

		this.checkItem()

		writer.writeByte(type.id)
		writer.writeString(this.idd)
		writer.writeString(this.str)
	}

	override fun update(dt: Float) {
		if (!checked) {
			checked = true

			for (trader in Trader.all) {
				if (trader.id == this.idd && trader.saved) {
					this.hidden = false
					return
				}
			}

			this.hidden = true
		}

		if (this.hidden) {
			return
		}

		if (colliding && activeUpgrade == null) {
			activeUpgrade = this
		} else if (!colliding && activeUpgrade == this) {
			activeUpgrade = null
		}

		this.checkItem()

		this.al = MathUtils.clamp(0f, 1f, this.al + ((if (activeUpgrade == this) 1 else 0) - this.al) * dt * 10f)
		this.z += (Math.cos((this.t * 2.4f).toDouble()) / 10f * dt.toDouble() * 60.0).toFloat()
		this.z = MathUtils.clamp(0f, 5f, this.z)

		this.body?.setTransform(this.x + 8 - this.w / 2, this.y + this.z + 8 - this.h / 2, 0f)

		if (activeUpgrade == this && Input.instance.wasPressed("interact")) {
			Input.instance.putState("inventory", Input.State.UP)

			var count = GlobalSave.getInt("num_coins")

			if (count < this.price) {
				Audio.playSfx("item_nocash")
				Camera.shake(6f)
			} else {
				count -= this.price
				GlobalSave.put("num_coins", count)
				this.playSfx("item_purchase");

				Achievements.unlock("SHOP_" + this.str!!.toUpperCase())

				for (i in 0..9) {
					val fx = PoofFx()

					fx.x = this.x + this.w / 2
					fx.y = this.y + this.h / 2

					Dungeon.area.add(fx)
				}

				for (i in 0..14) {
					val c = Confetti()

					c.x = this.x + Random.newFloat(this.w)
					c.y = this.y + Random.newFloat(this.h)
					c.vel.x = Random.newFloat(-30f, 30f)
					c.vel.y = Random.newFloat(30f, 40f)

					Dungeon.area.add(c)
				}

				this.item = null
				this.str = null
				this.body = World.removeBody(this.body)
			}
		}

		super.update(dt)
	}

	private fun checkItem() {
		if (this.item == null) {
			this.item = this.generateItem()

			if (this.item == null) {
				this.hidden = true
			} else {
				this.setupInfo()
				this.createBody()
			}
		}
	}

	private fun createBody() {
		this.w = this.item!!.sprite.regionWidth.toFloat()
		this.h = this.item!!.sprite.regionHeight.toFloat()

		this.body = World.removeBody(this.body)
		this.body = World.createSimpleBody(this, 0f, 0f,
			this.w, this.h, BodyDef.BodyType.DynamicBody, true)
		this.body!!.setTransform(this.x, this.y, 0f)
	}

	override fun destroy() {
		super.destroy()
		this.body = World.removeBody(this.body)
		all.remove(this)
	}

	private var al = 0f

	fun renderSigns() {
		if (hidden || item == null) {
			return
		}

		if (this.al > 0.05f && !Ui.hideUi) {
			Graphics.medium.setColor(1f, 1f, 1f, this.al)
			Graphics.print(this.item!!.name, Graphics.medium, this.x + 8 - nameW / 2, this.y + this.h + 8)
			Graphics.medium.setColor(1f, 1f, 1f, 1f)
		}
	}

	override fun render() {
		if (hidden || item == null) {
			return
		}

		val sprite = this.item!!.sprite

		val a = Math.cos((this.t * 3f).toDouble()).toFloat() * 8f
		val sy = (1f + Math.sin((this.t * 2f).toDouble()) / 10f).toFloat()

		Graphics.batch.end()

		if (this.al > 0.05f && !Ui.hideUi) {
			Mob.shader.begin()
			Mob.shader.setUniformf("u_color", Vector3(1f, 1f, 1f))
			Mob.shader.setUniformf("u_a", this.al)
			Mob.shader.end()
			Graphics.batch.shader = Mob.shader
			Graphics.batch.begin()

			for (xx in -1..1) {
				for (yy in -1..1) {
					if (Math.abs(xx) + Math.abs(yy) == 1) {
						Graphics.render(sprite, this.x + 8 + xx.toFloat(),
							this.y + z + 8 + yy.toFloat(), a, w / 2, h / 2, false, false, 1f, sy)
					}
				}
			}

			Graphics.batch.end()
			Graphics.batch.shader = null
		}

		Graphics.batch.begin()
		Graphics.print(this.costStr, Graphics.small,
			this.x + 8 - this.costW / 2, this.y - 8)
		Graphics.batch.end()

		WeaponBase.shader.begin()
		WeaponBase.shader.setUniformf("a", 1f)
		WeaponBase.shader.setUniformf("gray", 1f)
		WeaponBase.shader.setUniformf("time", Dungeon.time + this.t)
		WeaponBase.shader.end()
		Graphics.batch.shader = WeaponBase.shader
		Graphics.batch.begin()

		Graphics.render(sprite, this.x + 8,
			this.y + z + 8, a, w / 2, h / 2, false, false, 1f, sy)

		Graphics.batch.end()
		Graphics.batch.shader = null
		Graphics.batch.begin()
	}

	private var colliding = false

	override fun onCollision(entity: Entity?) {
		super.onCollision(entity)

		if (entity is Player) {
			colliding = true
		}
	}

	override fun onCollisionEnd(entity: Entity?) {
		super.onCollisionEnd(entity)

		if (entity is Player) {
			colliding = false
		}
	}
}