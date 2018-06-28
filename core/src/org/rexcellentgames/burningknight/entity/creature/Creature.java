package org.rexcellentgames.burningknight.entity.creature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Settings;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.buff.Buff;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.creature.fx.BloodFx;
import org.rexcellentgames.burningknight.entity.creature.fx.GoreFx;
import org.rexcellentgames.burningknight.entity.creature.fx.HpFx;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.mob.boss.BurningKnight;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.arrows.ArrowA;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.BulletA;
import org.rexcellentgames.burningknight.entity.item.weapon.rocketlauncher.rocket.RocketA;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.*;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;

public class Creature extends SaveableEntity {
	public int hw;
	public int hh;
	public float z;
	public float a = 1f;
	public long lastIndex;
	public boolean invisible;
	public boolean flying = false;
	public boolean penetrates;
	public boolean explosionBlock;
	public boolean freezed;
	public float speed = 10f;
	public float maxSpeed = 90f;
	protected int hp;
	protected int hpMax;
	protected int damage = 2;
	protected int defense;
	protected float invt = 0;
	protected boolean dead;
	protected boolean unhittable = false;
	protected Body body;
	protected float mul = 0.9f;
	protected float timer;
	protected boolean flipped = false;
	protected int hx;
	protected int hy;
	protected HashMap<Class<? extends Buff>, Buff> buffs = new HashMap<>();
	protected float invtt;
	protected HashMap<String, ArrayList<LuaFunction>> events = new HashMap<>();
	protected boolean falling;
	private boolean shouldDie = false;
	private boolean remove;

	public int registerCallback(String name, LuaFunction runnable) {
		ArrayList<LuaFunction> e = events.computeIfAbsent(name, k -> new ArrayList<>());
		e.add(runnable);

		return e.size() - 1;
	}

	public void removeCallback(String name, int id) {
		ArrayList<LuaFunction> e = events.get(name);

		if (e == null) {
			return;
		}

		if (id >= e.size()) {
			return;
		}

		e.remove(id);
	}

	public boolean isFlying() {
		return this.flying;
	}

	public Body createSimpleBody(int x, int y, int w, int h, BodyDef.BodyType type, boolean sensor) {
		this.hx = x;
		this.hy = y;
		this.hw = w;
		this.hh = h;

		return World.createSimpleBody(this, x, y, w, h, type, sensor);
	}

	public void modifyDefense(int amount) {
		this.defense += amount;
	}

	public void tp(float x, float y) {
		this.x = x;
		this.y = y;

		if (this.body != null) {
			this.body.setTransform(x, y, 0);
		}

		this.triggerEvent("tp");
	}

	public void triggerEvent(String name) {
		ArrayList<LuaFunction> e = events.get(name);

		if (e != null) {
			for (LuaFunction event : e) {
				try {
					event.call(self);
				} catch (LuaError error) {
					Log.error("Internal mod error!");
					error.printStackTrace();
				}
			}
		}
	}

	public LuaValue self = CoerceJavaToLua.coerce(this);

	public float getInvt() {
		return this.invt;
	}

	public void setInvt(float invt) {
		this.invtt = invt;
	}

	@Override
	public void init() {
		super.init();

		this.t = Random.newFloat(1024);

		if (this instanceof Mob && !(this instanceof BurningKnight)) {
			this.hp += (Dungeon.depth - 1) * 5;
		}

		this.hp = this.hpMax;
		this.initStats();
	}

	@Override
	public void destroy() {
		super.destroy();

		if (this.body != null) {
			this.body = World.removeBody(this.body);
		}
	}

	protected void onRoomChange() {

	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x, this.y, this.w, this.h, this.z / 5);
	}

	public void deathEffect(AnimationData killed) {
		LevelSave.remove(this);

		if (Settings.gore) {
			for (Animation.Frame frame : killed.getFrames()) {
				GoreFx fx = new GoreFx();

				fx.texture = frame.frame;
				fx.x = this.x + this.w / 2;
				fx.y = this.y + this.h / 2;

				Dungeon.area.add(fx);
			}
		}

		BloodFx.add(this, 20);
	}

	@Override
	public void update(float dt) {
		Buff[] buffs = this.buffs.values().toArray(new Buff[] {});

		for (int i = buffs.length - 1; i >= 0; i--) {
			buffs[i].update(dt);
		}

		Room room = Dungeon.level.findRoomFor(this.x + this.w / 2, this.y + this.h / 2);

		if (room != this.room) {
			this.room = room;
			this.onRoomChange();
			this.triggerEvent("on_room_change");
		}

		super.update(dt);

		if (this.shouldDie) {
			this.die();
			this.shouldDie = false;
		}

		if (this.hp == 0 && !this.dead) {
			this.die(true);
		}

		if (this.remove) {
			this.destroy();
			this.remove = false;
		}

		if (this.dead) {
			return;
		}

		if (this.freezed) {
			this.invt = Math.max(0, this.invt - dt);
			this.invtt = Math.max(0, this.invtt - dt);
			this.vel.x = 0;
			this.vel.y = 0;
			this.body.setLinearVelocity(this.vel);
			return;
		}

		this.vel.mul(this.mul);

		if (this.body != null) {
			this.x = this.body.getPosition().x;
			this.y = this.body.getPosition().y;
		}

		if (Dungeon.level != null) {
			for (int x = (int) Math.floor((this.hx + this.x) / 16); x < Math.ceil((this.hx + this.x + this.hw) / 16); x++) {
				for (int y = (int) Math.floor((this.hy + this.y + 8) / 16); y < Math.ceil((this.hy + this.y + 8 + this.hh / 3) / 16); y++) {
					if (x < 0 || y < 0 || x >= Level.getWidth() || y >= Level.getHeight()) {
						continue;
					}

					if (CollisionHelper.check(this.hx + this.x, this.hy + this.y, this.hw, this.hh / 3, x * 16 + 4, y * 16 - 4, 8, 8)) {
						byte t = Dungeon.level.get(x, y);
						byte tt = Dungeon.level.liquidData[Level.toIndex(x, y)];
						this.onTouch(t, x, y);
						this.onTouch(tt, x, y);
					}
				}
			}
		}
	}

	@Override
	public void become(String state) {
		if (!this.falling) {
			super.become(state);
		}
	}

	protected void onTouch(short t, int x, int y) {
		if (t == Terrain.WATER && !this.flying) {
			this.removeBuff(BurningBuff.class);
		} else if (t == Terrain.LAVA && !this.flying) {
			this.modifyHp(-1, null,true);
		}
	}

	protected void common() {
		float dt = getDt();

		this.t += dt;
		this.timer += dt;
		this.invt = Math.max(0, this.invt - dt);
		this.invtt = Math.max(0, this.invtt - dt);

		if (!this.dead) {
			if (this.vel.x < 0) {
				this.flipped = true;
			} else if (this.vel.x > 0) {
				this.flipped = false;
			}
		}

		if (this.vel.len2() > 1) {
			this.lastIndex = Dungeon.longTime;
		}

		if (this.falling) {
			this.vel.mul(0);
		}

		if (this.body != null) {
			if (!(this instanceof Player && ((Player) this).dashT > 0)) {
				this.vel.clamp(0, this.maxSpeed);
			}

			this.body.setLinearVelocity(this.vel.x, this.vel.y);
		}
	}

	protected float getDt() {
		return Gdx.graphics.getDeltaTime();
	}

	public void modifySpeed(int amount) {
		this.speed += amount;
		this.maxSpeed += amount * 7;
	}

	public HpFx modifyHp(int amount, Creature from) {
		return this.modifyHp(amount, from, false);
	}

	public HpFx modifyHp(int amount, Creature from, boolean ignoreArmor) {
		if (this.falling || this.done || this.dead || this.invtt > 0 || this.invt > 0 || (this instanceof Player && ((Player) this).dashT > 0)) {
			return null;
		} else if ((Random.chance(this.getStat("block_chance") * 10) || this.rollBlock()) && !ignoreArmor) {
			HpFx fx = new HpFx(this, 0);
			fx.block = true;
			Dungeon.area.add(fx);
			return fx;
		}

		boolean hurt = false;

		if (amount < 0) {
			if (this.unhittable) {
				return null;
			}

			if (from != null) {
				amount *= from.rollDamage();
			}

			if (!ignoreArmor) {
				amount += this.defense * this.rollDefense();

				if (amount > 0) {
					amount = -1;
				}

				if (from != null) {
					from.onHit(this);
				}
			}

			this.invt = this.getStat("inv_time");
			hurt = true;
		}

		HpFx fx = new HpFx(this, amount);
		Dungeon.area.add(fx);

		this.hp = (int) MathUtils.clamp(0, this.hpMax, this.hp + amount);

		if (hurt) {
			this.onHurt(amount, from);

			this.triggerEvent("on_hurt");
		} else {
			this.triggerEvent("on_heal");
		}

		if (this.hp == 0) {
			this.shouldDie = true;
		}

		return fx;
	}

	public boolean rollBlock() {
		return false;
	}

	public float rollDamage() {
		return 1;
	}

	public float rollDefense() {
		return 1;
	}

	public void onHit(Creature who) {
		this.triggerEvent("on_hit");
	}

	protected void onHurt(float a, Creature from) {
		Graphics.delay(20);
	}

	public boolean isUnhittable() {
		return unhittable;
	}

	public void setUnhittable(boolean unhittable) {
		this.unhittable = unhittable;
	}

	public int getDefense() {
		return this.defense;
	}

	public float getSpeed() {
		return speed;
	}

	public boolean isDead() {
		return this.dead;
	}

	public void die() {
		this.die(false);
	}

	protected void die(boolean force) {
		this.depth = -1;

		if (this.dead) {
			return;
		}

		this.triggerEvent("on_death");
		this.remove = true;
		this.dead = true;
	}

	public void onBuffRemove(Buff buff) {

	}

	public Point getAim() {
		return Input.instance.worldMouse;
	}

	public Item getAmmo(String type) {
		if (type.equals("bullet")) {
			return new BulletA();
		} else if (type.equals("rocket")) {
			return new RocketA();
		} else {
			return new ArrowA();
		}
	}

	protected void renderBuffs() {
		for (Buff buff : this.buffs.values()) {
			buff.render(this);
		}
	}

	public int getHp() {
		return this.hp;
	}

	public void heal() {
		if (!this.hasFullHealth()) {
			this.modifyHp(this.getHpMax() - this.hp, null);
		}
	}

	public int getHpMax() {
		return this.hpMax;
	}

	public boolean hasFullHealth() {
		return this.hp == this.hpMax;
	}

	public void setHpMax(int hpMax) {
		this.hpMax = Math.max(2, hpMax);
		this.hp = (int) MathUtils.clamp(0, this.hpMax, this.hp);
	}

	public void modifyHpMax(int amount) {
		this.setHpMax(this.hpMax + amount);
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeInt32(this.hp);
		writer.writeInt32(this.buffs.size());

		for (Buff buff : this.buffs.values()) {
			writer.writeString(buff.getClass().getName());
			writer.writeFloat(buff.getDuration());
		}
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.hp = reader.readInt32();
		int count = reader.readInt32();

		for (int i = 0; i < count; i++) {
			String t = reader.readString();

			Class<?> clazz;

			try {
				clazz = Class.forName(t);

				Constructor<?> constructor = clazz.getConstructor();
				Object object = constructor.newInstance();
				Buff buff = (Buff) object;

				buff.setOwner(this);
				buff.setDuration(reader.readFloat());

				this.addBuff(buff);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (this.body != null) {
			this.body.setTransform(this.x, this.y, 0);
		}
	}

	public void addBuff(Buff buff) {
		if (this.canHaveBuff(buff)) {
			Buff b = this.buffs.get(buff.getClass());

			if (b != null) {
				b.setDuration(Math.max(b.getDuration(), buff.getDuration()));
			} else {
				this.buffs.put(buff.getClass(), buff);

				buff.setOwner(this);
				buff.onStart();
			}

			this.triggerEvent("buff_added");
		}
	}

	protected boolean canHaveBuff(Buff buff) {
		return true;
	}

	public boolean hasBuff(Class<? extends Buff> buff) {
		return this.buffs.containsKey(buff);
	}

	public Body getBody() {
		return this.body;
	}

	public void knockBackFrom(Entity from, float force) {
		if (from == null) {
			return;
		}

		float a = from.getAngleTo(this.x + this.w / 2, this.y + this.h / 2);

		float knockbackMod = this.getStat("knockback");

		this.vel.x += Math.cos(a) * force * knockbackMod;
		this.vel.y += Math.sin(a) * force * knockbackMod;
	}

	public void removeBuff(Class<? extends Buff> buff) {
		Buff instance = this.buffs.get(buff);

		if (instance != null) {
			instance.onEnd();
			this.buffs.remove(buff);
			this.onBuffRemove(instance);
		}
	}

	public boolean isFlipped() {
		return this.flipped;
	}

	public Room room;

	// for lua
	public Room getRoom() {
		return room;
	}

	protected HashMap<String, Float> stats = new HashMap<>();

	public void modifyStat(String name, float val) {
		this.setStat(name, this.getStat(name) + val);
	}

	public float getStat(String name) {
		Float f = this.stats.get(name);

		if (f == null) {
			return 0;
		}

		return f;
	}

	public void initStats() {
		modifyStat("defense", 1f);
		modifyStat("damage", 1f);
		modifyStat("speed", 1f);
		modifyStat("knockback", 1f);
		modifyStat("block_chance", 0.1f);
		modifyStat("inv_max", 0.4f);
		modifyStat("crit_chance", 0.04f);
	}

	public void setStat(String name, float val) {
		stats.put(name, val);
	}
}