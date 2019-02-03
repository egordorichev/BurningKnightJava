package org.rexcellentgames.burningknight.entity.item

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.Fixture
import org.rexcellentgames.burningknight.Dungeon
import org.rexcellentgames.burningknight.assets.Graphics
import org.rexcellentgames.burningknight.assets.Locale
import org.rexcellentgames.burningknight.entity.Entity
import org.rexcellentgames.burningknight.entity.creature.Creature
import org.rexcellentgames.burningknight.entity.creature.mob.Mob
import org.rexcellentgames.burningknight.entity.creature.player.Player
import org.rexcellentgames.burningknight.entity.item.key.Key
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase
import org.rexcellentgames.burningknight.entity.level.Level
import org.rexcellentgames.burningknight.entity.level.SaveableEntity
import org.rexcellentgames.burningknight.entity.level.Terrain
import org.rexcellentgames.burningknight.entity.level.entities.chest.Chest
import org.rexcellentgames.burningknight.entity.level.rooms.shop.ShopRoom
import org.rexcellentgames.burningknight.game.Ui
import org.rexcellentgames.burningknight.game.input.Input
import org.rexcellentgames.burningknight.game.state.InGameState
import org.rexcellentgames.burningknight.physics.World
import org.rexcellentgames.burningknight.util.CollisionHelper
import org.rexcellentgames.burningknight.util.MathUtils
import org.rexcellentgames.burningknight.util.Random
import org.rexcellentgames.burningknight.util.Tween
import org.rexcellentgames.burningknight.util.file.FileReader
import org.rexcellentgames.burningknight.util.file.FileWriter
import java.io.IOException
import java.util.*

open class ItemHolder : SaveableEntity {
  var body: Body? = null

	constructor(item: Item? = null) {
		this.item = item
	}
  
  constructor()

  var startX = 0f
  var startY = 0f

  var fake = false
  var item: Item? = null
    set(value) {
      field = value

      this.body = World.removeBody(this.body)

	    if (item == null) {
		    return
	    }

      if (this.item!!.getSprite() == null) {
        return
      }

      if (this.item is Gold) {
        Gold.all.add(this)
        this.alwaysActive = true
      }

      createBody = true

      this.w = item!!.getSprite().regionWidth.toFloat()
      this.h = item!!.getSprite().regionWidth.toFloat()
    }

  var createBody = false
  var falling: Boolean = false
  var auto: Boolean = false
  
  var last: Float = 0.toFloat()
  var price: ItemPrice? = null

  private var z = 0f
  private var hw: Int = 0
  private var hh: Int = 0
  private var added: Boolean = false

  var al: Float = 0.toFloat()
  private var sz = 1f

  fun createSimpleBody(x: Int, y: Int, w: Int, h: Int, type: BodyDef.BodyType, sensor: Boolean): Body {
    this.hw = w
    this.hh = h

    return World.createSimpleBody(this, x.toFloat(), y.toFloat(), w.toFloat(), h.toFloat(), type, sensor)
  }

  fun randomVelocity() {
    val a = Random.newFloat((Math.PI * 2).toFloat()).toDouble()
		val f = Random.newFloat(60f, 150f)

    this.velocity.x = (Math.cos(a) * f).toFloat()
    this.velocity.y = (Math.sin(a) * f).toFloat()
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

  override fun shouldCollide(entity: Any?, contact: Contact?, fixture: Fixture?): Boolean {
    if (entity is Chest) {
      return false
    }

    if (item is Gold && entity !is Player) {
      return false
    }

    return super.shouldCollide(entity, contact, fixture)
  }

  override fun update(dt: Float) {
	  if (falling) {
		  fall -= dt

		  if (fall <= 0) {
			  done = true
		  }
	  }

    if (createBody) {
      if (!fake) {
        this.body = this.createSimpleBody(-2, -2, item!!.getSprite().regionWidth + 4, item!!.getSprite().regionHeight + 4, BodyDef.BodyType.DynamicBody, false)
        this.body!!.setTransform(this.x, this.y + this.z, 0f)
      }

      createBody = false
    }

    if (this.item == null) {
      return
    }

    if (this.item!!.shop && !added) {
      added = true
      
      val price = ItemPrice()

      this.item!!.price = this.item!!.getPrice()
	    this.item!!.price = Math.max(1, this.item!!.price + Random.newInt(-3, 3))

      price.x = this.x + this.w / 2
      price.y = this.y - 6f - (16 - this.h) / 2
      price.price = this.item!!.price
      price.sale = this.item!!.sale

      this.price = price
      
      Dungeon.area.add(price)
    }

    if (!this.item!!.shop && !falling) {
      var found = false
      var x = Math.floor(((this.x) / 16).toDouble()).toInt() - 1

      while (x < Math.ceil(((this.x + this.hw.toFloat() + 8) / 16).toDouble())) {
        var y = Math.floor(((this.y) / 16).toDouble()).toInt() - 1

        while (y < Math.ceil(((this.y + 16f + this.hh.toFloat()) / 16).toDouble())) {
          if (x < 0 || y < 0 || x >= Level.getWidth() || y >= Level.getHeight()) {
            y++
            continue
          }

          if (CollisionHelper.check(this.x, this.y, w, h, x * 16f, y * 16f - 8f, 32f, 32f)) {
            val i = Level.toIndex(x, y)
            val l = Dungeon.level.data[i]

            if (l == Terrain.FLOOR_A || l == Terrain.FLOOR_B || l == Terrain.FLOOR_C || l == Terrain.FLOOR_D) {
              found = true
              break
            }
          }

          if (found) {
            break
          }

          y++
        }

        if (found) {
          break
        }

        x++
      }

	    if (!found) {
		    falling = true
	    }
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
      if (this.item!!.shop) {
        World.checkLocked(this.body).setTransform(this.x, this.y + this.z, 0f)
      } else {
        this.x = this.body!!.position.x
        this.y = this.body!!.position.y - this.z
      }
    }

    this.velocity.mul(0.9f)

    if (!InGameState.dark && item is Gold && item!!.autoPickup && !done) {
      val room = Dungeon.level.findRoomFor(this.x + this.w / 2, this.y + this.h / 2)

      if (room != null && room !is ShopRoom && room == Player.instance.room && !room.hidden) {
        val dx = Player.instance.x + Player.instance.w / 2 - this.x - this.w / 2
        val dy = Player.instance.y + Player.instance.h / 2 - this.y - this.h / 2
        val d = Math.sqrt((dx * dx + dy * dy).toDouble())
        val f = 20f
        this.velocity.x += (dx / d).toFloat() * f
        this.velocity.y += (dy / d).toFloat() * f
      }
    }

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

    /*
    if (this.item is BurningKey) {
      this.lst += dtthat

      if (this.lst > 0.2f) {
        Dungeon.level.setOnFire(Level.toIndex((Math.floor(((this.x + this.w / 2) / 16).toDouble()).toInt()), (Math.floor(((this.y + this.h / 2) / 16).toDouble())).toInt()), true)
        Dungeon.area.add(FlameFx(this))
        lst = 0f
      }
    }*/
  }

  // private var lst = 0f

	private var collided = false

  override fun init() {
    super.init()

    startX = x
    startY = y

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

	private var fall = 1f

  override fun render() {
    if (this.item == null) {
      return
    }

    val sprite = this.item!!.getSprite()

    val a = Math.cos((this.t * 3f).toDouble()).toFloat() * 8f * sz
    val sy = (1f + Math.sin((this.t * 2f).toDouble()) / 10f).toFloat() * fall

    Graphics.batch.end()

    val dt = Gdx.graphics.deltaTime
    this.al = MathUtils.clamp(0f, 1f, this.al + ((if (Player.instance.pickupFx != null && Player.instance.pickupFx.item === this) 1 else 0) - this.al) * dt * 10f)

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
            Graphics.render(sprite, this.x + w / 2 + xx.toFloat(), this.y + this.z + h / 2 + yy.toFloat(), a, w / 2, h / 2, false, false, fall, sy)
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

    Graphics.render(sprite, this.x + w / 2, this.y + this.z + h / 2, a, w / 2, h / 2, false, false, fall, sy)

    Graphics.batch.end()
    Graphics.batch.shader = null
    Graphics.batch.begin()
  }

  override fun renderShadow() {
    Graphics.shadow(this.x, this.y, this.w * fall, this.h * fall, this.z)
  }

  override fun onCollision(entity: Entity?) {
    super.onCollision(entity)

    if (t < 0.01f) {
      // return;
    }

    if (item is Key && !collided && Dungeon.depth == -3 && Ui.controls.size == 0) {
      Ui.ui.addControl("[white]" + Input.instance.getMapping("interact") + " [gray]" + Locale.get("interact"))
      collided = true
    }

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

    writer.writeBoolean(this.item != null);

    if (this.item != null) {
      writer.writeString(this.item!!.javaClass.name)
      this.item!!.save(writer)
    }
  }

  @Throws(IOException::class)
  override fun load(reader: FileReader) {
    super.load(reader)

    if (reader.readBoolean()) {
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
    }

	  fake = false
    this.body?.setTransform(this.x, this.y, 0f)
  }

  companion object {
    @JvmStatic
    val all = ArrayList<ItemHolder>()
  }
}