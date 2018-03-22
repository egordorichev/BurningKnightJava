package org.rexellentgames.dungeon.entity.creature;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.creature.buff.Buff;
import org.rexellentgames.dungeon.entity.creature.buff.BurningBuff;
import org.rexellentgames.dungeon.entity.creature.fx.HpFx;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.net.Packets;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.MathUtils;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.Tween;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Creature extends SaveableEntity {
	public static final float INV_TIME = 0.4f;
	protected int hp;
	protected int hpMax;
	protected float speed = 10;
	protected float maxSpeed = 50;
	protected int damage = 2;
	protected int defense = 1;
	protected float invt = 0;
	protected boolean dead;
	protected boolean unhittable = false;
	protected Body body;
	protected float timer;
	protected boolean flipped = false;
	private int hx, hy, hw, hh;
	protected HashMap<Class<? extends Buff>, Buff> buffs = new HashMap<Class<? extends Buff>, Buff>();
	public float a = 1f;
	public long lastIndex;
	public boolean invisible;
	protected boolean flying = false;
	public HashMap<Long, State> states = new HashMap<Long, State>();

	public boolean isFlying() {
		return this.flying;
	}

	public static class State {
		public float x;
		public float y;
	}

	public void registerState() {
		Creature.State state = new Creature.State();

		state.x = this.x;
		state.y = this.y;

		this.states.put(Dungeon.longTime, state);
	}

	@Override
	public Body createBody(int x, int y, int w, int h, BodyDef.BodyType type, boolean sensor) {
		this.hx = x;
		this.hy = y;
		this.hw = w;
		this.hh = h;

		return super.createBody(x, y, w, h, type, sensor);
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

		if (Network.SERVER) {
			Network.server.getServerHandler().sendToAll(Packets.makeTpEntity(this.getId(), x, y));
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body.getWorld().destroyBody(this.body);
	}

	@Override
	public void init() {
		super.init();

		this.t = Random.newFloat(1024);
		this.hp = this.hpMax;
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		this.vel.mul(0.8f);

		if (this.body != null) {
			this.x = this.body.getPosition().x;
			this.y = this.body.getPosition().y;
		}

		Buff[] buffs = this.buffs.values().toArray(new Buff[] {});

		for (int i = buffs.length - 1; i >= 0; i--) {
			buffs[i].update(dt);
		}

		if (Dungeon.level != null) {
			boolean onGround = false;

			for (int x = (int) Math.floor((this.hx + this.x) / 16); x < Math.ceil((this.hx + this.x + this.hw) / 16); x++) {
				for (int y = (int) Math.floor((this.hy + this.y) / 16); y < Math.ceil((this.hy + this.y + this.hh / 3) / 16); y++) {
					if (x < 0 || y < 0 || x >= Level.getWIDTH() || y >= Level.getHEIGHT()) {
						continue;
					}

					short t = Dungeon.level.get(x, y);

					if (!Dungeon.level.checkFor(x, y, Terrain.HOLE)) {
						onGround = true;
					}

					this.onTouch(t, x, y);
				}
			}

			if (!onGround && !this.flying && !this.dead) {
				this.die(); // todo: anim....
			}
		}
	}

	protected void onTouch(short t, int x, int y) {
		if (t == Terrain.WATER && !this.flying) {
			this.buffs.remove(BurningBuff.class);
		} else if (t == Terrain.SPIKES && !this.flying) {
			this.modifyHp(-20, true);
		}
	}

	protected void common() {
		float dt = Gdx.graphics.getDeltaTime();

		this.t += dt;
		this.timer += dt;
		this.invt = Math.max(0, this.invt - dt);

		if (!this.dead) {
			if (this.vel.x < 0) {
				this.flipped = true;
			} else if (this.vel.x > 0) {
				this.flipped = false;
			}

			if (!Network.SERVER && !Network.NONE) {
				this.registerState();
				this.states.remove((long) (Dungeon.time * 60 - 60));
			}
		}

		if (this.vel.len2() > 1) {
			this.lastIndex = Dungeon.longTime;
		}

		if (this.body != null) {
			this.vel.clamp(0, this.maxSpeed);
			this.body.setLinearVelocity(this.vel.x, this.vel.y);
		}
	}

	public void modifySpeed(int amount) {
		this.speed += amount;
		this.maxSpeed += amount * 5;
	}

	public void modifyHp(int amount) {
		this.modifyHp(amount, false);
	}

	public boolean isUnhittable() {
		return unhittable;
	}

	public void modifyHp(int amount, boolean ignoreArmor) {
		if (this.dead) {
			return;
		}

		if (amount < 0) {
			if (this.unhittable) {
				return;
			}

			if (!ignoreArmor) {
				amount += this.defense;

				if (amount > 0) {
					amount = 0;
				}
			}

			if (this.invt > 0) {
				return;
			}

			this.invt = INV_TIME;
			this.onHurt();
		}

		if (!Network.SERVER) {
			this.area.add(new HpFx(this, amount));
		}

		this.hp = (int) MathUtils.clamp(0, this.hpMax, this.hp + amount);

		if (this.hp == 0) {
			this.die();
		}
	}

	public void setUnhittable(boolean unhittable) {
		this.unhittable = unhittable;
	}

	public boolean isDead() {
		return this.dead;
	}

	protected void onHurt() {

	}

	protected void die() {
		if (this.dead) {
			return;
		}

		Tween.to(new Tween.Task(0, 3f) {
			@Override
			public float getValue() {
				return a;
			}

			@Override
			public void setValue(float value) {
				a = value;
			}

			@Override
			public float function(float p) {
				return p;
			}

			@Override
			public void onEnd() {
				done = true;
			}
		});

		this.dead = true;

		if (Dungeon.level != null) {
			Dungeon.level.removeSaveable(this);
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

	public int getHpMax() {
		return this.hpMax;
	}

	public void setHpMax(int hpMax) {
		this.hpMax = hpMax;
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
				Object object = constructor.newInstance(new Object[]{});
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

	public boolean hasBuff(Class<? extends Buff> buff) {
		return this.buffs.containsKey(buff);
	}

	public Body getBody() {
		return this.body;
	}

	protected boolean canHaveBuff(Buff buff) {
		return true;
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
		}
	}

	public void removeBuff(Class<? extends Buff> buff) {
		this.buffs.remove(buff);
	}

	public boolean isFlipped() {
		return this.flipped;
	}

	public Collection<Buff> getBuffs() {
		return this.buffs.values();
	}

	public int toIndex() {
		return (int) (Math.floor(this.x / 16) + Math.floor(this.y / 16) * Level.getWIDTH());
	}
}