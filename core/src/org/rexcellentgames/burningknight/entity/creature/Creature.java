package org.rexcellentgames.burningknight.entity.creature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Settings;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.buff.Buff;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.creature.buff.PoisonBuff;
import org.rexcellentgames.burningknight.entity.creature.fx.BloodFx;
import org.rexcellentgames.burningknight.entity.creature.fx.GoreFx;
import org.rexcellentgames.burningknight.entity.creature.fx.HpFx;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.mob.desert.Mummy;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.fx.BloodSplatFx;
import org.rexcellentgames.burningknight.entity.fx.GrassBreakFx;
import org.rexcellentgames.burningknight.entity.fx.SteamFx;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.weapon.Weapon;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.arrows.ArrowA;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.BulletA;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.Projectile;
import org.rexcellentgames.burningknight.entity.item.weapon.rocketlauncher.rocket.RocketA;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.game.state.InGameState;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.*;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;

public class Creature extends SaveableEntity {
	public int hw;
	public int hh;
	public float z;
	public float lz;
	public float a = 1f;
	public long lastIndex;
	public boolean invisible;
	protected boolean flying = false;
	public boolean penetrates;
	public boolean explosionBlock;
	public boolean freezed;
	public boolean poisoned;
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
	protected float mul = 0.7f;
	protected float timer;
	protected boolean flipped = false;
	public int hx;
	public int hy;
	protected HashMap<Class<? extends Buff>, Buff> buffs = new HashMap<>();
	protected float invtt;
	protected boolean shouldDie = false;
	public boolean remove;

	public float getWeaponAngle() {
		Point aim = getAim();
		return getAngleTo(aim.x, aim.y);
	}

	public boolean isFlying() {
		return this.flying;
	}

	public Body createSimpleBody(int x, int y, int w, int h, BodyDef.BodyType type, boolean sensor) {
		this.hx = x;
		this.hy = y;
		this.hw = w;
		this.hh = h;

		Body body = World.createSimpleBody(this, x, y, w, h, type, sensor);

		if (body != null) {
			body.setSleepingAllowed(false);
		}

		return body;
	}

	public void modifyDefense(int amount) {
		this.defense += amount;
	}

	public void tp(float x, float y) {
		this.x = x;
		this.y = y;

		if (this.body != null) {
			World.checkLocked(this.body).setTransform(this.x, this.y + this.z, 0);
			this.lz = this.z;
		}
	}

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
		Graphics.shadowSized(this.x, this.y, this.w, this.h, 6);
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


		if (Settings.blood) {
			for (int i = 0; i < Random.newInt(2, 3); i++) {
				BloodSplatFx fxx = new BloodSplatFx();

				fxx.sizeMod = 2;
				fxx.x = x + Random.newFloat(w) - 8;
				fxx.y = y + Random.newFloat(h) - 8;

				Dungeon.area.add(fxx);
			}

			for (int i = 0; i < Random.newInt(2, 5); i++) {
				BloodSplatFx fxx = new BloodSplatFx();

				fxx.sizeMod = 1;
				fxx.lng = true;
				fxx.a = Random.newFloat(360);
				fxx.x = x + Random.newFloat(w) - 8;
				fxx.y = y + Random.newFloat(h) - 8;

				Dungeon.area.add(fxx);
			}

			for (int i = 0; i < Random.newInt(3, 5); i++) {
				BloodSplatFx fxx = new BloodSplatFx();

				fxx.sizeMod = 1;
				fxx.lng = true;
				fxx.cntr = true;
				fxx.a = Random.newFloat(360);
				fxx.x = x + Random.newFloat(w) - 8;
				fxx.y = y + Random.newFloat(h) - 8;

				Dungeon.area.add(fxx);
			}
		}

		this.poof();

		BloodFx.add(this, 20);
	}

	public void poof() {
		for (int i = 0; i < 10; i++) {
			PoofFx fx = new PoofFx();

			fx.x = this.x + this.w / 2;
			fx.y = this.y + this.h / 2;

			Dungeon.area.add(fx);
		}
	}

	protected boolean ignorePos;
	protected Point acceleration = new Point();
	protected boolean touches[] = new boolean[Terrain.SIZE];
	protected boolean ignoreAcceleration;

	@Override
	public void update(float dt) {
		Arrays.fill(touches, false);

		for (int x = (int) Math.floor((this.hx + this.x) / 16); x < Math.ceil((this.hx + this.x + this.hw) / 16); x++) {
			for (int y = (int) Math.floor((this.hy + this.y + 8) / 16); y < Math.ceil((this.hy + this.y + 8 + this.hh / 3) / 16); y++) {
				if (x < 0 || y < 0 || x >= Level.getWidth() || y >= Level.getHeight()) {
					continue;
				}

				if (CollisionHelper.check(this.hx + this.x, this.hy + this.y, this.hw, this.hh / 3, x * 16, y * 16 - 8, 16, 16)) {
					int i = Level.toIndex(x, y);
					int info = Dungeon.level.getInfo(i);
					byte t = Dungeon.level.get(i);
					this.onTouch(t, x, y, info);
					touches[t] = true;

					byte l = Dungeon.level.liquidData[i];

					if (l > 0) {
						Rectangle r = Level.COLLISIONS[Dungeon.level.liquidVariants[i]];

						if (CollisionHelper.check(this.hx + this.x, this.hy + this.y, this.hw, this.hh / 3,
							x * 16 + r.x, y * 16 - 8 + r.y, r.width, r.height)) {
							touches[l] = true;
							this.onTouch(l, x, y, info);
						}
					}
				}
			}
		}

		if (!ignoreAcceleration) {
			this.acceleration.x = 0;
			this.acceleration.y = 0;
		}

		Buff[] buffs = this.buffs.values().toArray(new Buff[] {});
		int sx = (int) (this.x + this.w / 2);
		int sy = (int) (this.y + this.h / 2);

		for (int i = buffs.length - 1; i >= 0; i--) {
			if (buffs[i] instanceof BurningBuff) {
				InGameState.burning = true;

				if (!this.isFlying()) {
					Dungeon.level.setOnFire(Level.toIndex(sx / 16, sy / 16), true, false);
				}
			}

			buffs[i].update(dt);
		}

		Room room = Dungeon.level.findRoomFor(sx, sy);

		if (room != this.room) {
			this.room = room;
			this.onRoomChange();
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
			this.velocity.x = 0;
			this.velocity.y = 0;
			this.body.setLinearVelocity(new Vector2(this.velocity.x + this.knockback.x, this.velocity.y + this.knockback.y));
			return;
		}

		if (!this.isFlying() && this.touches[Terrain.WATER] && !ignoreWater() && (!(this instanceof Player) || !((Player) this).isRolling())) {
			this.velocity.y -= dt * 600;
		}

		if (this instanceof Player && ((Player) this).isRolling()) {
			this.velocity.mul(this.mul);
		} else {
			this.velocity.mul(
				(this.touches[Terrain.COBWEB] && !this.isFlying() ? 0.5f :
					(iceResitant == 0 && this.touches[Terrain.ICE] && !this.isFlying() ? 0.95f : this.mul))
			);
		}

		this.knockback.mul(0.9f);

		if (this.body != null && !ignorePos) {
			this.x = this.body.getPosition().x;
			this.y = this.body.getPosition().y - this.lz;
		}
	}

	protected boolean ignoreWater() {
		return false;
	}

	public int slowLiquidResist = 0;

	protected void onTouch(short t, int x, int y, int info) {
		if (t == Terrain.WATER && !this.isFlying()) {
			if (this.hasBuff(BurningBuff.class)) {
				this.removeBuff(BurningBuff.class);

				for (int i = 0; i < 20; i++) {
					SteamFx fx = new SteamFx();

					fx.x = this.x + Random.newFloat(this.w);
					fx.y = this.y + Random.newFloat(this.h);

					Dungeon.area.add(fx);
				}
			}
		} else {
			if (!this.isFlying() && BitHelper.isBitSet(info, 0) && !this.hasBuff(BurningBuff.class)) {
				this.addBuff(new BurningBuff());
			}

			if (t == Terrain.LAVA && !this.isFlying()) {
				if (this instanceof Mob) {
					this.die();
				} else {
					this.addBuff(new BurningBuff());
				}
			} else if (!this.isFlying() && (t == Terrain.HIGH_GRASS || t == Terrain.HIGH_DRY_GRASS)) {
				Dungeon.level.set(x, y, t == Terrain.HIGH_GRASS ? Terrain.GRASS : Terrain.DRY_GRASS);

				for (int i = 0; i < 10; i++) {
					GrassBreakFx fx = new GrassBreakFx();

					fx.x = x * 16 + Random.newFloat(16);
					fx.y = y * 16 + Random.newFloat(16) - 8;

					Dungeon.area.add(fx);
				}
			} else if (!this.isFlying() && t == Terrain.VENOM) {
				this.addBuff(new PoisonBuff());
			}
		}
	}

	protected void doVel() {
		float fr = (iceResitant > 0 && this.touches[Terrain.ICE] && !this.isFlying()) ? 1.3f : (this.touches[Terrain.ICE] && !this.isFlying() ? 0.2f : (this.slowLiquidResist == 0 && (this.touches[Terrain.LAVA]) && !this.isFlying() ? 0.55f : 1f));
		this.velocity.x += this.acceleration.x * fr;
		this.velocity.y += this.acceleration.y * fr;
	}

	protected void common() {
		float dt = getDt();
		this.doVel();

		this.t += dt;
		this.timer += dt;
		this.invt = Math.max(0, this.invt - dt);
		this.invtt = Math.max(0, this.invtt - dt);

		if (!this.dead) {
			if (this.velocity.x < 0) {
				this.flipped = true;
			} else if (this.velocity.x > 0) {
				this.flipped = false;
			}
		}

		if (this.velocity.len2() > 1) {
			this.lastIndex = Dungeon.longTime;
		}

		if (this.body != null) {
			// this.velocity.clamp(0, this.maxSpeed);
			this.body.setLinearVelocity(new Vector2(this.velocity.x + this.knockback.x, this.velocity.y + this.knockback.y));
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

	public HpFx modifyHp(int amount, Entity from, boolean ignoreArmor) {
		if (this.isUnhittable() && amount < 0) {
			return null;
		}

		if (this.done || this.dead || this.invtt > 0 || (this.invt > 0 && !(this instanceof Mob))) {
			return null;
		}/* else if (amount < 0 && !this.touches[Terrain.COBWEB] && !this.hasBuff(FreezeBuff.class)) {
			// (((Random.chance(this.getStat("block_chance") * 100) || this.rollBlock()) && !ignoreArmor) || this.touches[Terrain.OBSIDIAN] ||
			//(!ignoreArmor && this instanceof Player && Random.newFloat(100) < this.defense * 10 * rollDefense())))

			this.playSfx("block");
			this.invt = this.getStat("inv_time");
			return (HpFx) Dungeon.area.add(new HpFx(this, 0, true));
		}*/

		boolean hurt = false;

		if (amount < 0) {
			if (this.unhittable) {
				return null;
			}

			if (from instanceof Creature) {
				amount *= ((Creature)from).rollDamage();
			} else if (from instanceof Projectile) {
				if (((Projectile)from).owner != null) {
					amount *= ((Projectile) from).owner.rollDamage();
				}
			}

			if (!ignoreArmor) {
				amount += this.defense * this.rollDefense();

				if (amount > 0) {
					amount = -1;
				}

				if (from instanceof Creature) {
					((Creature) from).onHit(this);
				} else if (from instanceof Projectile) {
					if (((Projectile)from).owner != null) {
						((Projectile) from).owner.onHit(this);
					}
				}
			}

			if (this instanceof Player) {
				amount = amount < -7 ? -2 : -1;
			}

			this.invt = this.getStat("inv_time");
			hurt = true;
		}

		if (Player.showStats) {
			HpFx fx = new HpFx(this, amount, false);
			Dungeon.area.add(fx);
		}

		if (hurt) {
			this.doHurt(amount);
		} else {
			this.hp = (int) MathUtils.clamp(0, this.hpMax, this.hp + amount);
		}

		if (hurt) {
			this.onHurt(amount, from);

			if (Settings.blood) {
				for (int i = 0; i < Random.newInt(2, 3); i++) {
					BloodSplatFx fxx = new BloodSplatFx();

					fxx.sizeMod = 1;
					fxx.x = x + Random.newFloat(w) - 8;
					fxx.y = y + Random.newFloat(h) - 8;

					Dungeon.area.add(fxx);
				}

				if (Random.chance(50)) {
					for (int i = 0; i < Random.newInt(1, 3); i++) {
						BloodSplatFx fxx = new BloodSplatFx();

						fxx.sizeMod = 1;
						fxx.lng = true;
						fxx.cntr = true;
						fxx.a = Random.newFloat(360);
						fxx.x = x + Random.newFloat(w) - 8;
						fxx.y = y + Random.newFloat(h) - 8;

						Dungeon.area.add(fxx);
					}
				}

				BloodSplatFx fxx = new BloodSplatFx();

				fxx.sizeMod = 1;
				fxx.lng = true;
				fxx.a = from == null ? Random.newFloat(360) : (float) (Math.toDegrees(this.getAngleTo(from.x + from.w, from.y + from.h) - Math.PI));
				fxx.x = x + Random.newFloat(w) - 8;
				fxx.y = y + Random.newFloat(h) - 8;

				Dungeon.area.add(fxx);
			}

			BloodFx.add(this, 10);
		}

		this.checkDeath();

		return null;
	}

	protected void checkDeath() {
		if (this.hp == 0) {
			this.shouldDie = true;
		}
	}

	protected void doHurt(int a) {
		this.hp = Math.max(0, this.hp + a);
	}

	public boolean rollBlock() {
		return false;
	}

	public float rollDamage() {
		return this.touches[Terrain.ICE] ? 2 : 1;
	}

	public boolean isTouching(byte t) {
		return touches[t];
	}

	public int iceResitant;

	public float rollDefense() {
		return 1;
	}

	public void onHit(Creature who) {

	}

	protected void onHurt(int a, Entity from) {
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

	public void renderBuffs() {
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

		writer.writeInt16((short) this.hp);
		writer.writeInt16((short) this.hpMax);
		writer.writeInt32(this.buffs.size());

		for (Buff buff : this.buffs.values()) {
			writer.writeString(buff.getClass().getName());
			writer.writeFloat(buff.getDuration());
		}
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.hp = reader.readInt16();
		this.hpMax = reader.readInt16();
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
			World.checkLocked(this.body).setTransform(this.x, this.y + this.z, 0);
			this.lz = this.z;
		}
	}

	public void addBuff(Buff buff) {
		if (this.canHaveBuff(buff)) {
			Buff b = this.buffs.get(buff.getClass());

			if (b != null) {
				// b.setDuration(Math.max(b.getDuration(), buff.getDuration()));
			} else {
				this.buffs.put(buff.getClass(), buff);

				buff.setOwner(this);
				buff.onStart();
			}

			if (buff instanceof PoisonBuff) {
				this.removeBuff(BurningBuff.class);
			}
		}
	}

	protected boolean canHaveBuff(Buff buff) {
		if (this.unhittable) {
			if (buff instanceof BurningBuff || buff instanceof PoisonBuff) {
				return false;
			}
		}

		return true;
	}

	public boolean hasBuff(Class<? extends Buff> buff) {
		return this.buffs.containsKey(buff);
	}

	public Body getBody() {
		return this.body;
	}

	public void knockBackFrom(Entity from, float force) {
		if (from == null || this.unhittable) {
			return;
		}

		force *= 100;
		float a = from.getAngleTo(this.x + this.w / 2, this.y + this.h / 2);

		float knockbackMod = this.getStat("knockback");

		this.knockback.x += Math.cos(a) * force * knockbackMod;
		this.knockback.y += Math.sin(a) * force * knockbackMod;
	}

	public Point knockback = new Point();

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
		modifyStat("inv_time", 0.4f);
		modifyStat("crit_chance", 0.04f);
	}

	public void setStat(String name, float val) {
		stats.put(name, val);
	}

	@Override
	public boolean shouldCollide(Object entity, Contact contact, Fixture fixture) {
		if (this.isFlying() && entity instanceof Level) {
			return false;
		} else if (entity instanceof Creature) {
			if (this.hasBuff(BurningBuff.class)) {
				((Creature) entity).addBuff(new BurningBuff());
			}

			if (!(this instanceof Player && entity instanceof Mummy)) {
				return false;
			}
		} else if (entity instanceof ItemHolder) {
			return false;
		} else if (entity instanceof Weapon && ((Weapon) entity).getOwner() == this) {
			return false;
		}

		return super.shouldCollide(entity, contact, fixture);
	}

	public void setFlying(boolean flying) {
		this.flying = flying;
	}
}