package org.rexellentgames.dungeon.entity.creature;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.buff.Buff;
import org.rexellentgames.dungeon.entity.creature.buff.BurningBuff;
import org.rexellentgames.dungeon.entity.creature.fx.HpFx;
import org.rexellentgames.dungeon.entity.creature.mob.BurningKnight;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.entities.Entrance;
import org.rexellentgames.dungeon.game.state.LoadState;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.net.Packets;
import org.rexellentgames.dungeon.physics.World;
import org.rexellentgames.dungeon.util.*;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Creature extends SaveableEntity {
	protected float invmax = 0.4f;
	protected int hp;
	protected int hpMax;
	protected float speed = 10;
	protected float maxSpeed = 90;
	protected int damage = 2;
	protected int defense = 1;
	protected float invt = 0;
	protected boolean dead;
	protected boolean unhittable = false;
	protected Body body;
	protected float timer;
	protected boolean flipped = false;
	private int hx;
	private int hy;
	public int hw;
	public int hh;
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

	public Body createSimpleBody(int x, int y, int w, int h, BodyDef.BodyType type, boolean sensor) {
		this.hx = x;
		this.hy = y;
		this.hw = w;
		this.hh = h;

		return World.createSimpleBody(this, x, y, w, h, type, sensor);
	}

	public Body createSimpleCentredBody(int x, int y, int w, int h, BodyDef.BodyType type, boolean sensor) {
		this.hx = x;
		this.hy = y;
		this.hw = w;
		this.hh = h;

		return World.createSimpleCentredBody(this, x, y, w, h, type, sensor);
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

		if (this.body != null) {
			this.body = World.removeBody(this.body);
		}
	}

	public float getInvt() {
		return this.invt;
	}

	@Override
	public void init() {
		super.init();

		this.t = Random.newFloat(1024);

		if (this instanceof Mob && !(this instanceof BurningKnight)) {
			this.hp += (Dungeon.depth - 1) * 5;
		}

		this.hp = this.hpMax;
	}

	private boolean shouldDie = false;

	@Override
	public void update(float dt) {
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

		this.vel.mul(0.9f);

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
			ArrayList<Point> chasm = new ArrayList<>();

			for (int x = (int) Math.floor((this.hx + this.x) / 16); x < Math.ceil((this.hx + this.x + this.hw) / 16); x++) {
				for (int y = (int) Math.floor((this.hy + this.y + 8) / 16); y < Math.ceil((this.hy + this.y + 8 + this.hh / 3) / 16); y++) {
					if (x < 0 || y < 0 || x >= Level.getWidth() || y >= Level.getHeight()) {
						continue;
					}

					short t = Dungeon.level.get(x, y);

					if (!Dungeon.level.checkFor(x, y, Terrain.HOLE)) {
						onGround = true;
					} else {
						chasm.add(new Point(x, y));
					}

					this.onTouch(t, x, y);
				}
			}

			if (!this.flying) {
				for (Point c : chasm) {
					float dx = c.x * 16 + 8 - this.x - this.w / 2;
					float dy = c.y * 16 + 8 - this.y - this.h / 2;
					float d = (float) Math.sqrt(dx * dx + dy * dy);

					this.vel.x -= dx / d * 8;
					this.vel.y -= dy / d * 8;
				}
			}

			if (!(Dungeon.game.getState() instanceof LoadState) && !this.falling && !onGround && !this.flying && !this.dead && !(this instanceof Player && ((Player) this).dashT > 0)) {
				this.falling = true;
				this.t = 0;
			}
		}
	}

	protected boolean falling;

	@Override
	public void become(String state) {
		if (!this.falling) {
			super.become(state);
		}
	}

	protected void renderFalling(AnimationData animation) {
		if (this.dead) {
			return;
		}

		TextureRegion sprite = animation.getCurrent().frame;

		float s = 1 - this.t / 2;

		if (s <= 0) {
			if (this instanceof Player) {
				Dungeon.loadType = Entrance.LoadType.FALL_DOWN;
				Dungeon.goToLevel(Dungeon.depth + 1);
			} else {
				this.die();
			}

			return;
		}

		Graphics.render(sprite, x + sprite.getRegionWidth() / 2, y + sprite.getRegionHeight() / 2 - this.t * 8f,
			this.t * 360, sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2,
			false, false, s, s);
		Graphics.batch.setColor(1, 1, 1, 1);
	}

	protected void onTouch(short t, int x, int y) {
		if (t == Terrain.WATER && !this.flying) {
			this.buffs.remove(BurningBuff.class);
		} else if (t == Terrain.SPIKES && !this.flying) {
			this.modifyHp(-1, true);
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

	public void modifySpeed(int amount) {
		this.speed += amount;
		this.maxSpeed += amount * 7;
	}

	public void modifyHp(int amount) {
		this.modifyHp(amount, false);
	}

	public boolean isUnhittable() {
		return unhittable;
	}

	public int getDefense() {
		return this.defense;
	}

	public void modifyHp(int amount, boolean ignoreArmor) {
		if (this.falling) {
			return;
		}

		if (this.dead) {
			return;
		}

		boolean hurt = false;

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

			if (this.invt > 0 || (this instanceof Player && ((Player) this).dashT > 0)) {
				return;
			}

			this.invt = this.invmax;
			hurt = true;
		}

		if (!Network.SERVER) {
			this.area.add(new HpFx(this, amount));
		}

		this.hp = (int) MathUtils.clamp(0, this.hpMax, this.hp + amount);

		if (hurt) {
			this.onHurt();
		}

		if (this.hp == 0) {
			this.shouldDie = true;
		}
	}

	public void setUnhittable(boolean unhittable) {
		this.unhittable = unhittable;
	}

	public boolean isDead() {
		return this.dead;
	}

	protected void onHurt() {
		Graphics.delay(20);
	}

	protected void die() {
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

	private boolean remove;

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
		Buff instance = this.buffs.get(buff);

		if (instance != null) {
			instance.onEnd();
			this.buffs.remove(buff);
		}
	}

	public boolean isFlipped() {
		return this.flipped;
	}

	public Collection<Buff> getBuffs() {
		return this.buffs.values();
	}

	public int toIndex() {
		return (int) (Math.floor(this.x / 16) + Math.floor(this.y / 16) * Level.getWidth());
	}
}