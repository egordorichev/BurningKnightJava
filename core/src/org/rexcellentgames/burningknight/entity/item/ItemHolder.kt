package org.rexcellentgames.burningknight.entity.item

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import org.rexcellentgames.burningknight.Dungeon
import org.rexcellentgames.burningknight.assets.Graphics
import org.rexcellentgames.burningknight.entity.Entity
import org.rexcellentgames.burningknight.entity.creature.Creature
import org.rexcellentgames.burningknight.entity.creature.mob.Mob
import org.rexcellentgames.burningknight.entity.creature.player.Player
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase
import org.rexcellentgames.burningknight.entity.level.SaveableEntity
import org.rexcellentgames.burningknight.game.input.Input
import org.rexcellentgames.burningknight.physics.World
import org.rexcellentgames.burningknight.util.MathUtils
import org.rexcellentgames.burningknight.util.Random
import org.rexcellentgames.burningknight.util.Tween
import org.rexcellentgames.burningknight.util.file.FileReader
import org.rexcellentgames.burningknight.util.file.FileWriter
import java.io.IOException
import java.util.*

open class ItemHolder : SaveableEntity {
  var body: Body? = null
    private set

	constructor(item: Item? = null) {
		this.item = item
	}
  
  constructor()

  var item: Item? = null
    set(value) {
      field = value

      this.body = World.removeBody(this.body)

	    if (item == null) {
		    return
	    }

      if (this.item is Gold) {
        Gold.all.add(this)
      }

      // This might be bad!
      this.body = this.createSimpleBody(-2, -2, item!!.getSprite().regionWidth + 4, item!!.getSprite().regionHeight + 4, BodyDef.BodyType.DynamicBody, false)

      this.w = item!!.getSprite().regionWidth.toFloat()
      this.h = item!!.getSprite().regionWidth.toFloat()
    }
  
  var falling: Boolean = false
  var auto: Boolean = false
  
  var last: Float = 0.toFloat()
  var price: ItemPrice? = null

  private var z = 0f
  private var hw: Int = 0
  private var hh: Int = 0
  private var added: Boolean = false

  private var al: Float = 0.toFloat()
  private var sz = 1f

  fun createSimpleBody(x: Int, y: Int, w: Int, h: Int, type: BodyDef.BodyType, sensor: Boolean): Body {
    this.hw = w
    this.hh = h

    return World.createSimpleBody(this, x.toFloat(), y.toFloat(), w.toFloat(), h.toFloat(), type, sensor)
  }

  fun randomVelocity() {
    val a = Random.newFloat((Math.PI * 2).toFloat()).toDouble()

    this.velocity.x = (Math.cos(a) * 100f).toFloat()
    this.velocity.y = (Math.sin(a) * 100f).toFloat()
  }

  fun velocityToMouse() {
    val dx = Input.instance.worldMouse.x - this.x
    val dy = Input.instance.worldMouse.y - this.y

    val a = Math.atan2(dy.toDouble(), dx.toDouble()).toFloat()

    this.velocity.x = (Math.cos(a.toDouble()) * 100f).toFloat()
    this.velocity.y = (Math.sin(a.toDouble()) * 100f).toFloat()
  }

  fun sale() {
    item!!.sale = true
    item!!.price = Math.max(0.0, Math.floor((item!!.price / 2).toDouble())).toInt()

    added = false

    if (price != null) {
      price!!.done = true
    }
  }

  fun unSale() {
    item!!.sale = false
    item!!.price *= 2

    if (item!!.price % 2 == 0) {
      item!!.price++
    }

    added = false

    if (price != null) {
      price!!.done = true
    }
  }

  override fun update(dt: Float) {
    if (this.item!!.shop && !added) {
      added = true
      
      val price = ItemPrice()

      price.x = this.x + this.w / 2
      price.y = this.y - 6f - (16 - this.h) / 2
      price.price = this.item!!.price
      price.sale = this.item!!.sale

      this.price = price
      
      Dungeon.area.add(price)
    }

    this.t += dt
    this.last += dt

    if (this.done) {
      return
    }

    if (this.last > 0.5f) {
      this.last = 0f
      Spark.randomOn(this.x, this.y, this.hw.toFloat(), this.hh.toFloat())
    }

    super.update(dt)

    if (this.body != null) {
      this.x = this.body!!.position.x
      this.y = this.body!!.position.y - this.z
    }

    this.velocity.mul(0.9f)

    this.sz = Math.max(1f, this.sz - this.sz * dt)

    if (this.velocity.len() <= 0.1f) {
      this.velocity.mul(0f)
      this.x = Math.round(this.x).toFloat()
      this.y = Math.round(this.y).toFloat()

      this.z += (Math.cos((this.t * 1.7f).toDouble()) / 5f * (this.sz / 2).toDouble() * dt.toDouble() * 60.0).toFloat()

      this.z = MathUtils.clamp(0f, 5f, this.z)

	    if (this.body != null) {
		    World.checkLocked(this.body).setTransform(this.x, this.y + this.z, 0f)
	    }
    }

    this.item!!.update(dt)

    if (this.body != null) {
      this.body!!.linearVelocity = this.velocity
    }
  }

  override fun init() {
    super.init()

    this.t = Random.newFloat(32f)
    this.last = Random.newFloat(1f)

    if (this.body != null) {
      World.checkLocked(this.body).setTransform(this.x, this.y, 0f)
    }

    all.add(this)
  }

  override fun destroy() {
    if (this.item is Gold) {
      Gold.all.remove(this)
    }

    super.destroy()

    if (price != null) {
      price!!.remove()
      price = null
    }

    this.body = World.removeBody(this.body)

    all.remove(this)
  }

  override fun render() {
    val sprite = this.item!!.getSprite()

    val a = Math.cos((this.t * 3f).toDouble()).toFloat() * 8f * sz
    val sy = (1f + Math.sin((this.t * 2f).toDouble()) / 10f).toFloat()

    Graphics.batch.end()

    val dt = Gdx.graphics.deltaTime
    this.al = MathUtils.clamp(0f, 1f, this.al + ((if (Player.instance.pickupFx != null && Player.instance.pickupFx.item === this) 1 else 0) - this.al) * dt * 10f)

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
            Graphics.render(sprite, this.x + w / 2 + xx.toFloat(), this.y + this.z + h / 2 + yy.toFloat(), a, w / 2, h / 2, false, false, 1f, sy)
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

    Graphics.render(sprite, this.x + w / 2, this.y + this.z + h / 2, a, w / 2, h / 2, false, false, 1f, sy)

    Graphics.batch.end()
    Graphics.batch.shader = null
    Graphics.batch.begin()
  }

  override fun renderShadow() {
    Graphics.shadow(this.x, this.y, this.w, this.h, this.z)
  }

  override fun onCollision(entity: Entity?) {
    super.onCollision(entity)

    if (entity is Creature) {
      Tween.to(object : Tween.Task(4f, 0.3f) {
        override fun getValue(): Float {
          return sz
        }

        override fun setValue(value: Float) {
          sz = value
        }
      })
    }
  }

  @Throws(IOException::class)
  override fun save(writer: FileWriter) {
    super.save(writer)

    writer.writeString(this.item!!.javaClass.name)
    this.item!!.save(writer)
  }

  @Throws(IOException::class)
  override fun load(reader: FileReader) {
    super.load(reader)

    val type = reader.readString()

    try {
      val clazz = Class.forName(type)
      val constructor = clazz.getConstructor()
      val `object` = constructor.newInstance()
      val item = `object` as Item

      item.load(reader)
      this.item = item
    } catch (e: Exception) {
      Dungeon.reportException(e)
    }

    this.body!!.setTransform(this.x, this.y, 0f)
  }

  companion object {
    @JvmStatic
    val all = ArrayList<ItemHolder>()
  }
}