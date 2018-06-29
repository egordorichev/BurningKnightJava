package org.rexcellentgames.burningknight.entity.creature.mob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.Buff;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.creature.buff.FreezeBuff;
import org.rexcellentgames.burningknight.entity.creature.buff.PoisonBuff;
import org.rexcellentgames.burningknight.entity.creature.fx.HeartFx;
import org.rexcellentgames.burningknight.entity.creature.mob.boss.Boss;
import org.rexcellentgames.burningknight.entity.creature.mob.boss.BurningKnight;
import org.rexcellentgames.burningknight.entity.creature.mob.prefix.Prefix;
import org.rexcellentgames.burningknight.entity.creature.npc.Npc;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Gold;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.BadBullet;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.entity.pool.PrefixPool;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.PathFinder;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;
import org.rexcellentgames.burningknight.util.geometry.Point;

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
	public ArrayList<Player> colliding = new ArrayList<>();
	protected boolean drop;
	protected State ai;
	public boolean stupid = false;
	protected Mind mind;
	protected Prefix prefix;

	private static TextureRegion hideSign;
	private static TextureRegion noticeSign;
	public float noticeSignT;
	public float hideSignT;
	public boolean nodebuffs;

	@Override
	protected boolean canHaveBuff(Buff buff) {
		if (nodebuffs && (buff instanceof FreezeBuff || buff instanceof BurningBuff || buff instanceof PoisonBuff)) {
			return false;
		}

		return super.canHaveBuff(buff);
	}

	{
		alwaysActive = true;
	}

	@Override
	public void init() {
		super.init();

		if (!(this instanceof BurningKnight) && !(this instanceof Npc)) {
			all.add(this);
		}

		every.add(this);

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

		if (challenge || Random.chance(10)) {
			this.generatePrefix();
		}

		return this;
	}

	public void generatePrefix() {
		if (this.prefix == null) {
			this.prefix = PrefixPool.instance.generate();
			this.prefix.apply(this);
			this.prefix.onGenerate(this);
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
						data.render(this.x + xx, this.y + yy + z, this.flipped);
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

		Graphics.batch.setColor(1, 1, 1, this.a);
		data.render(this.x, this.y + z, this.flipped);

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

	private float closestFraction = 1.0f;
	private Entity last;

	private RayCastCallback callback = new RayCastCallback() {
		@Override
		public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
			if (fixture.isSensor()) {
				return 1;
			}

			Entity entity = (Entity) fixture.getBody().getUserData();

			if ((entity == null && !fixture.getBody().isBullet()) || (entity instanceof Door && !((Door) entity).isOpen()) || entity instanceof Player) {
				if (fraction < closestFraction) {
					closestFraction = fraction;
					last = entity;
				}

				return fraction;
			}

			return 1;
		}
	};

	public boolean canSee(Creature player) {
		closestFraction = 1f;
		float x = this.x + this.w / 2;
		float y = this.y + 4;

		World.world.rayCast(callback, x, y, player.x + player.w / 2, player.y + 4);

		return last == player;
	}

	protected void assignTarget() {
		this.target = (Creature) this.area.getRandomEntity(this.stupid ? Mob.class : Player.class);

		if (this.target != null && this.target.invisible) {
			this.target = null;
		}
	}

	public Point getCloser(Point target) {
		int from = (int) (Math.floor((this.x + this.w / 2) / 16) + Math.floor((this.y + 12) / 16) * Level.getWidth());
		int to = (int) (Math.floor((target.x + this.w / 2) / 16) + Math.floor((target.y + 12) / 16) * Level.getWidth());

		if (!Dungeon.level.checkFor(to, Terrain.PASSABLE)) {
			to -= Level.getWidth();
		}

		if (!Dungeon.level.checkFor(from, Terrain.PASSABLE)) {
			from -= Level.getWidth();
		}

		int step = PathFinder.getStep(from, to, Dungeon.level.getPassable());

		if (step != -1) {
			Point p = new Point();

			p.x = step % Level.getWidth() * 16;
			p.y = (float) (Math.floor(step / Level.getWidth()) * 16);

			return p;
		}

		return null;
	}

	public Point getFar(Point target) {
		if (target == null) {
			return null;
		}

		int from = (int) (Math.floor((this.x + this.w / 2) / 16) + Math.floor((this.y + 12) / 16) * Level.getWidth());
		int to = (int) (Math.floor((target.x + this.w / 2) / 16) + Math.floor((target.y + 12) / 16) * Level.getWidth());

		if (!Dungeon.level.checkFor(to, Terrain.PASSABLE)) {
			to -= Level.getWidth();
		}

		if (!Dungeon.level.checkFor(from, Terrain.PASSABLE)) {
			from -= Level.getWidth();
		}

		int step = PathFinder.getStepBack(from, to, Dungeon.level.getPassable());

		if (step != -1) {
			Point p = new Point();

			p.x = step % Level.getWidth() * 16;
			p.y = (float) (Math.floor(step / Level.getWidth()) * 16);

			return p;
		}

		return null;
	}

	public float getWeight() {
		return 1;
	}

	@Override
	public void update(float dt) {
		super.update(dt * speedMod);

		if (this.freezed) {
			return;
		}

		if (this.room != null && !this.ignoreRooms) {
			this.room.numEnemies += 1;
		}

		if (this.drop) {
			if (this.prefix != null) {
				this.prefix.onDeath(this);
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
			}
		}

		if (this.ai != null) {
			this.ai.update(dt * speedMod);

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
		ArrayList<Item> items = new ArrayList<>();

		if (Random.chance(50)) {
			Gold gold = new Gold();
			gold.generate();
			items.add(gold);
		}

		return items;
	}

	@Override
	protected void die(boolean force) {
		if (!this.dead && !force) {
			this.drop = true;
		}

		if (!Player.instance.isDead() && !force) {
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

	public class GetOutState extends State {
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();
			delay = Random.newFloat(2f, 3f);
		}

		@Override
		public void update(float dt) {
			super.update(dt);
			this.checkForPlayer();
			this.moveFrom(self.lastSeen, 7f, 5f);

			if (this.t >= delay) {
				self.become("idle");
			}
		}
	}

	protected State getAi(String state) {
		switch (state) {
			case "getout": return new GetOutState();
		}

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

				if (this.canSee(player)) {
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
		} else {
			return new Point(this.x + this.w, this.y);
		}
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

		public boolean moveFrom(Point point, float s, float d) {
			if (this.nextPathPoint == null) {
				if (self.target == null) {
					self.target = Player.instance;
				}

				if (self.lastSeen == null) {
					self.lastSeen = new Point(self.target.x, self.target.y);
				}

				this.nextPathPoint = self.getFar(point);

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

					self.saw = false;
				}
			}

			if (this.target == null && force) {
				self.findTarget(true);
			}

			if (self.target != null) {
				if (!self.saw && Player.instance.room == self.room && self.canSee(self.target)) {
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
				self.saw = false;
			}
		}

		public Room target;
		
		public void findNearbyPoint() {
			if (this.targetPoint != null) {
				return;
			}
			
			if (self.room != null) {
				if (true) {
					this.target = self.room;
				} else {
					for (int i = 0; i < 10; i++) {
						this.target = self.room.neighbours.get(Random.newInt(self.room.neighbours.size()));

						if (this.target != self.lastRoom && this.target != self.room) {
							self.lastRoom = self.room;
							break;
						}

						if (i == 9) {
							self.lastRoom = self.room;
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