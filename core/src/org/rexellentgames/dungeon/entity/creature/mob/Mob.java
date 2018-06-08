package org.rexellentgames.dungeon.entity.creature.mob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.fx.HeartFx;
import org.rexellentgames.dungeon.entity.creature.mob.boss.Boss;
import org.rexellentgames.dungeon.entity.creature.mob.prefix.Prefix;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Gold;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.BadBullet;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.entity.level.save.LevelSave;
import org.rexellentgames.dungeon.entity.pool.PrefixPool;
import org.rexellentgames.dungeon.ui.ExpFx;
import org.rexellentgames.dungeon.util.*;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.io.IOException;
import java.util.ArrayList;

public class Mob extends Creature {
	public Point lastSeen;
	public static boolean challenge;
	public static float speedMod = 1f;
	public static float shotSpeedMod = 1f;
	public Creature target;
	public static ArrayList<Mob> all = new ArrayList<>();
	public static ArrayList<Mob> every = new ArrayList<>();
	public ArrayList<Player> colliding = new ArrayList<Player>();
	protected boolean drop;
	protected int experienceDropped = 1;
	protected State ai;
	public boolean stupid = false;
	protected Mind mind;
	protected boolean hide;
	protected boolean guard;
	protected Room start;
	protected Prefix prefix;

	private static TextureRegion hideSign;
	private static TextureRegion noticeSign;
	public float noticeSignT;
	public float hideSignT;

	{
		alwaysActive = true;
	}

	protected void renderSigns() {
		Graphics.batch.setColor(1, 1, 1, this.a);
		float dt = Gdx.graphics.getDeltaTime();

		this.hideSignT = Math.max(0, this.hideSignT - dt);
		this.noticeSignT = Math.max(0, this.noticeSignT - dt);

		if (this.hideSignT > 0) {
			Graphics.render(hideSign, this.x + this.w / 2,
				(float) (this.y + this.h + Math.cos(this.hideSignT * 8f) * 3f),
				(float) Math.sin(this.hideSignT * 5f) * 15f,
				hideSign.getRegionWidth() / 2, 3,
				false, false);
		}

		if (this.noticeSignT > 0) {
			Graphics.render(noticeSign, this.x + this.w / 2,
				(float) (this.y + this.h + Math.cos(this.noticeSignT * 8f) * 3f),
				(float) Math.sin(this.noticeSignT * 5f) * 15f,
				noticeSign.getRegionWidth() / 2, 3,
				false, false);
		}
		Graphics.batch.setColor(1, 1, 1, 1);
	}

	@Override
	public void init() {
		super.init();

		if (!(this instanceof BurningKnight)) {
			all.add(this);
		}

		every.add(this);

		if (hideSign == null) {
			hideSign = Graphics.getTexture("ui (hide sign)");
			noticeSign = Graphics.getTexture("ui (notice sign)");
		}

		if (Random.chance(50)) {
			this.become("roam");
		}
	}

	public Mind getMind() {
		return this.mind;
	}

	public enum Mind {
		ATTACKER(0),
		DEFENDER(1),
		COWARD(2),
		RAT(3);

		private byte id;

		Mind(int id) {
			this.id = (byte) id;
		}

		public byte getId() {
			return this.id;
		}
	}

	public Mob generate() {
		this.mind = Mind.values()[Random.newInt(Mind.values().length)];

		if (challenge || Random.chance(20)) {
			this.generatePrefix();
		}

		return this;
	}

	public void generatePrefix() {
		if (this.prefix == null) {
			this.prefix = PrefixPool.instance.generate();
			this.prefix.apply(this);
			this.prefix.onGenerate();
		}
	}

	public static ShaderProgram shader;
	public static ShaderProgram frozen;

	static {
		shader = new ShaderProgram(Gdx.files.internal("shaders/outline.vert").readString(), Gdx.files.internal("shaders/outline.frag").readString());
		if (!shader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());
		frozen = new ShaderProgram(Gdx.files.internal("shaders/ice.vert").readString(), Gdx.files.internal("shaders/ice.frag").readString());
		if (!frozen.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + frozen.getLog());
	}

	public void renderWithOutline(AnimationData data) {
		if (this.prefix != null) {
			Color color = this.prefix.getColor();

			Graphics.batch.end();
			shader.begin();
			shader.setUniformf("u_a", 1f);
			shader.setUniformf("u_color", new Vector3(color.r, color.g, color.b));
			shader.end();
			Graphics.batch.setShader(shader);
			Graphics.batch.begin();

			for (int xx = -1; xx < 2; xx++) {
				for (int yy = -1; yy < 2; yy++) {
					if (Math.abs(xx) + Math.abs(yy) == 1) {
						data.render(this.x + xx, this.y + yy, this.flipped);
					}
				}
			}

			Graphics.batch.end();
			Graphics.batch.setShader(null);
			Graphics.batch.begin();
		}

		if (this.fa > 0) {
			Graphics.batch.end();
			frozen.begin();
			frozen.setUniformf("time", Dungeon.time);
			frozen.setUniformf("f", this.fa);
			frozen.setUniformf("a", this.a);
			frozen.end();
			Graphics.batch.setShader(frozen);
			Graphics.batch.begin();
		}

		data.render(this.x, this.y, this.flipped);

		if (this.freezed) {
			this.fa += (1 - this.fa) * Gdx.graphics.getDeltaTime() * 3f;
		} else {
			this.fa += (0 - this.fa) * Gdx.graphics.getDeltaTime() * 3f;
		}

		if (this.fa > 0) {
			Graphics.batch.end();
			Graphics.batch.setShader(null);
			Graphics.batch.begin();
		}
	}

	private float fa;

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		this.mind = Mind.values()[reader.readByte()];
		int id = reader.readInt16();

		if (id > 0) {
			this.prefix = PrefixPool.instance.getModifier(id - 1);
			this.prefix.apply(this);
		}
	}

	protected boolean ignoreRooms;

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeByte(this.mind.getId());

		if (this.prefix == null) {
			writer.writeInt16((short) 0);
		} else {
			writer.writeInt16((short) (this.prefix.id + 1));
		}
	}

	public boolean canSee(Creature player) {
		return canSee(player, 0);
	}

	public boolean canSee(Creature player, int flag) {
		return this.getDistanceTo(player.x + 8, player.y + 8) < 256f && Dungeon.level.canSee(
			(int) Math.floor((this.x + this.w / 2) / 16), (int) Math.floor((this.y + this.h / 2) / 16),
			(int) Math.floor((player.x + player.w / 2) / 16), (int) Math.floor((player.y + player.h / 2) / 16)
		, flag) == 0;
	}

	protected void assignTarget() {
		this.target = (Creature) this.area.getRandomEntity(this.stupid ? Mob.class : Player.class);

		if (this.target != null && this.target.invisible) {
			this.target = null;
		}
	}

	public Point getCloser(Point target) {
		int from = (int) (Math.floor((this.x + this.w / 2) / 16) + Math.floor((this.y + this.h / 2) / 16) * Level.getWidth());
		int to = (int) (Math.floor((target.x + this.w / 2) / 16) + Math.floor((target.y + this.h / 2) / 16) * Level.getWidth());

		int step = PathFinder.getStep(from, to, Dungeon.level.getPassable());

		if (step != -1) {
			Point p = new Point();

			p.x = step % Level.getWidth() * 16;
			p.y = (float) (Math.floor(step / Level.getWidth()) * 16);

			return p;
		}

		return null;
	}

	public Room room;

	public float getWeight() {
		return 1;
	}

	@Override
	public void update(float dt) {
		super.update(dt * speedMod);

		if (this.freezed) {
			return;
		}

		Room room = Dungeon.level.findRoomFor(this.x, this.y);

		if (room != null) {
			this.room = room;
		}

		if (this.room != null && !this.ignoreRooms) {
			this.room.numEnemies += 1;
		}

		if (this.drop) {
			if (this.prefix != null) {
				this.prefix.onDeath();
			}

			this.drop = false;
			ArrayList<Item> items = this.getDrops();

			for (Item item : items) {
				ItemHolder holder = new ItemHolder();

				holder.setItem(item);
				holder.x = this.x;
				holder.y = this.y;

				this.area.add(holder);
				LevelSave.add(holder);
			}
		}

		if (this.dead) {
			return;
		}

		if (this.ai == null) {
			this.ai = this.getAi(this.state);

			if (this.ai != null) {
				this.ai.self = this;
				this.ai.onEnter();
			} else {
				Log.error("No ai for " + this.state + " " + this.getClass().getSimpleName());
			}
		}

		if (this.ai != null) {
			this.ai.update(dt * speedMod);

			if (this.start == null) {
				this.ai.findCurrentRoom();
				this.start = this.ai.currentRoom;
			}

			if (this.ai != null) { // !?!?!
				this.ai.t += dt * speedMod;
			}
		}

		this.findTarget(false);

		if (this.target != null && (this.target.invisible || this.target.isDead())) {
			this.target = null;
			this.become("idle");
		}
	}

	@Override
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
			this.vel.clamp(0, this.maxSpeed);
			this.body.setLinearVelocity(this.vel.x * speedMod, this.vel.y * speedMod);
		}
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Player) {
			Player player = (Player) entity;

			if (player.isDead()) {
				return;
			}

			this.target = player;

			this.colliding.add(player);
		}
	}

	@Override
	public void onCollisionEnd(Entity entity) {
		if (entity instanceof Player) {
			Player player = (Player) entity;

			if (player.isDead()) {
				return;
			}

			this.colliding.remove(player);
		}
	}

	protected ArrayList<Item> getDrops() {
		ArrayList<Item> items = new ArrayList<Item>();

		Gold gold = new Gold();

		gold.generate();

		items.add(gold);

		return items;
	}

	@Override
	protected void die(boolean force) {
		if (!this.dead && !force) {
			this.drop = true;
		}

		if (!Player.instance.isDead() && !force) {
			for (int i = 0; i < this.experienceDropped; i++) {
				Dungeon.area.add(new ExpFx(this.x + this.w / 2 + Random.newFloat(-4, 4), this.y + this.w / 2 + Random.newFloat(-4, 4)));
			}

			for (int i = 0; i < Random.newInt(1, 2); i++) {
				HeartFx fx = new HeartFx();

				fx.x = this.x + this.w / 2 + Random.newFloat(-4, 4);
				fx.y = this.y + this.h / 2 + Random.newFloat(-4, 4);

				Dungeon.area.add(fx);
				LevelSave.add(fx);
			}
		}

		super.die(force);
	}

	@Override
	protected float getDt() {
		return super.getDt() * speedMod;
	}

	protected float moveToPoint(float x, float y, float speed) {
		float dx = x - this.x - this.w / 2;
		float dy = y - this.y - this.h / 2;
		float d = (float) Math.sqrt(dx * dx + dy * dy);

		this.vel.x += dx / d * speed;
		this.vel.y += dy / d * speed;

		return d;
	}

	@Override
	public void become(String state) {
		if (!this.state.equals(state)) {
			if (this.ai != null) {
				this.ai.onExit();
			}

			this.ai = this.getAi(state);

			if (this.ai != null) {
				this.ai.self = this;
				this.ai.onEnter();
			} else {
				Log.error("'" + state + "' ai is not found for mob " + this.getClass().getSimpleName());
			}
		}

		super.become(state);
	}

	protected State getAi(String state) {
		return null;
	}

	public void findTarget(boolean force) {
		if (this.target == null && Dungeon.level != null) {
			for (Creature player : (this.stupid ? Mob.all : Player.all)) {
				if (player.invisible || player == this) {
					continue;
				}

				if (force || this.stupid) {
					this.target = player;
					return;
				}

				Line line = new Line((int) Math.floor((this.x + 8) / 16), (int) Math.floor((this.y + 8) / 16),
					(int) Math.floor((player.x + 8) / 16), (int) Math.floor((player.y + 8) / 16));

				boolean[] passable = Dungeon.level.getPassable();
				boolean found = false;

				for (Point point : line.getPoints()) {
					int i = (int) (point.x + point.y * Level.getWidth());
					if (i < 0 || i >= Level.getSIZE() || (!passable[i] && Dungeon.level.get(i) != 13)) {
						found = true;
						break;
					}
				}

				if (!found) {
					this.target = player;
					break;
				}
			}
		}
	}

	@Override
	protected void onHurt(float a, Creature from) {
		super.onHurt(a, from);

		if (this.ai != null && !(this instanceof Boss)) {
			this.ai.checkForPlayer(true);
		}

		if (this.hp <= 0) {
			if (this.room != null) {
				this.room.numEnemies -= 1;
				all.remove(this);
				every.remove(this);
			}
		}
	}

	@Override
	public Point getAim() {
		if (this.target != null) {
			return new Point(this.target.x + this.target.w / 2, this.target.y + this.target.h / 2);
		}

		return super.getAim();
	}

	@Override
	public void destroy() {
		super.destroy();
		all.remove(this);
		every.remove(this);
	}

	public Room lastRoom;
	public boolean toWater;

	@Override
	public Item getAmmo(String type) {
		if (type.equals("bullet")) {
			return new BadBullet();
		}

		return super.getAmmo(type);
	}

	public class State<T extends Mob> {
		public T self;
		public float t;
		public Point nextPathPoint;
		public Point targetPoint;

		public void update(float dt) {
			this.t += dt;
		}

		public void onEnter() {

		}

		public void onExit() {

		}

		public boolean flyTo(Point point, float s, float ds) {
			float dx = point.x - self.x - self.w / 2;
			float dy = point.y - self.y - self.h / 2;
			float d = (float) Math.sqrt(dx * dx + dy * dy);

			if (d < ds) {
				return true;
			}

			self.vel.x += dx / d * s;
			self.vel.y += dy / d * s;

			return false;
		}

		public boolean moveTo(Point point, float s, float d) {
			if (this.nextPathPoint == null) {
				this.nextPathPoint = self.getCloser(point);

				if (this.nextPathPoint == null) {
					return false;
				}
			}

			// tile offset
			float ds = self.moveToPoint(this.nextPathPoint.x + 8, this.nextPathPoint.y , s);
			float dd = self.getDistanceTo(point.x + 8, point.y + 8);

			if (ds < 4f || dd < d) {
				this.nextPathPoint = null;
				return dd <= d;
			}

			return false;
		}

		public void checkForPlayer() {
			this.checkForPlayer(false);
		}

		public void checkForPlayer(boolean force) {
			if (self.target != null) {
				self.lastSeen = new Point(self.target.x, self.target.y);

				if (!self.canSee(self.target)) {
					self.target = null;

					Level.heat = Math.max(0, Level.heat - 1f);
					self.saw = false;
				}
			}

			if (this.target == null && force) {
				self.findTarget(true);
			}

			if (self.target != null) {
				if (Player.instance.currentRoom != null && self.canSee(self.target, Terrain.BREAKS_ENEMY_LOS)) {
					self.saw = true;

					if (self.noticeSignT <= 0) {
						self.hideSignT = 0f;
						self.noticeSignT = 2f;
					}

					if (!self.state.equals("chase") && !self.state.equals("fleeing")) {
						self.become("alerted");
					}
				} else {
					if (self.state.equals("idle") || self.state.equals("laugh")) {
						self.become("chase");
					}
				}
			} else if (self.saw) {
				Level.heat = Math.max(0, Level.heat - 1f);
				self.saw = false;
			}
		}

		public Room currentRoom;
		public Room target;

		public void findCurrentRoom() {
			this.currentRoom = Dungeon.level.findRoomFor(self.x, self.y);
		}

		public void findNearbyPoint() {
			if (this.targetPoint != null) {
				return;
			}

			this.findCurrentRoom();

			if (this.currentRoom != null) {
				if (true) {
					this.target = this.currentRoom;
				} else {
					for (int i = 0; i < 10; i++) {
						this.target = this.currentRoom.neighbours.get(Random.newInt(this.currentRoom.neighbours.size()));

						if (this.target != self.lastRoom && this.target != this.currentRoom) {
							self.lastRoom = this.currentRoom;
							break;
						}

						if (i == 9) {
							self.lastRoom = this.currentRoom;
						}
					}
				}

				boolean found = false;

				for (int i = 0; i < this.target.getWidth() * this.target.getHeight(); i++) {
					this.targetPoint = this.target.getRandomCell();

					if (Dungeon.level.isValid((int) this.targetPoint.x, (int) this.targetPoint.y) &&
						(self.toWater ? Dungeon.level.get((int) this.targetPoint.x, (int) this.targetPoint.y) == Terrain.WATER
							: Dungeon.level.checkFor(
						(int) this.targetPoint.x, (int) this.targetPoint.y, Terrain.PASSABLE))) {

						found = true;
						this.targetPoint.mul(16);
						break;
					}
				}

				if (!found) {
					this.target = null;
					this.targetPoint = null;
				} else {
					self.toWater = false;
				}
			}
		}
	}

	public boolean saw;
}