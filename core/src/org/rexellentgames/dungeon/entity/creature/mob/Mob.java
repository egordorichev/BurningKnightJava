package org.rexellentgames.dungeon.entity.creature.mob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.item.consumable.potion.HealingPotion;
import org.rexellentgames.dungeon.entity.item.consumable.potion.SunPotion;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.ui.ExpFx;
import org.rexellentgames.dungeon.util.Line;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.PathFinder;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.io.IOException;
import java.util.ArrayList;

public class Mob extends Creature {
	public float flee;
	public Point lastSeen;
	public Player target;
	protected ArrayList<Player> colliding = new ArrayList<Player>();
	protected boolean drop;
	protected int experienceDropped = 1;
	protected State ai;
	protected Mind mind;
	protected boolean hide;

	private static TextureRegion hideSign;
	private static TextureRegion noticeSign;
	public float noticeSignT;
	public float hideSignT;

	{
		alwaysActive = true;
	}

	@Override
	public void renderTop() {
		this.renderSigns();
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
		return this;
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		this.mind = Mind.values()[reader.readByte()];
	}

	protected boolean ignoreRooms;

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeByte(this.mind.getId());
	}

	public int getExperienceDropped() {
		return this.experienceDropped;
	}

	protected boolean canSee(Player player) {
		return this.getDistanceTo(player.x + 8, player.y + 8) < 256f && Dungeon.level.canSee(
			(int) Math.floor((this.x + this.w / 2) / 16), (int) Math.floor((this.y + this.h / 2) / 16),
			(int) Math.floor((player.x + player.w / 2) / 16), (int) Math.floor((player.y + player.h / 2) / 16)
		) == 0;
	}

	protected void assignTarget() {
		this.target = (Player) this.area.getRandomEntity(Player.class);

		if (this.target != null && this.target.invisible) {
			this.target = null;
		}
	}

	public Point getCloser(Point target) {
		int from = (int) (Math.floor((this.x + 8) / 16) + Math.floor((this.y + 8) / 16) * Level.getWidth());
		int to = (int) (Math.floor((target.x + 8) / 16) + Math.floor((target.y + 8) / 16) * Level.getWidth());

		int step = PathFinder.getStep(from, to, Dungeon.level.getPassable(), this.mind == Mind.COWARD || this.mind == Mind.RAT || this.hide);

		if (step != -1) {
			Point p = new Point();

			p.x = step % Level.getWidth() * 16;
			p.y = (float) (Math.floor(step / Level.getWidth()) * 16);

			return p;
		}

		return null;
	}

	private Room room;

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.room != null && !this.ignoreRooms) {
			this.room.numEnemies -= 1;
		}

		Room room = Dungeon.level.findRoomFor(this.x, this.y);

		if (room != null) {
			this.room = room;
		}

		if (this.room != null && !this.ignoreRooms) {
			this.room.numEnemies += 1;
		}

		if (this.drop) {
			this.drop = false;
			ArrayList<Item> items = this.getDrops();

			for (Item item : items) {
				ItemHolder holder = new ItemHolder();

				holder.setItem(item);
				holder.x = this.x;
				holder.y = this.y;

				this.area.add(holder);
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
			this.ai.update(dt);

			if (this.ai != null) { // !?!?!
				this.ai.t += dt;
			}
		}

		if (this.target != null && this.canSee(this.target)) {
			this.target.heat += dt;
		}

		if (this.target == null && Dungeon.level != null) {
			for (Player player : Player.all) {
				if (player.invisible) {
					continue;
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

		if (this.target != null && this.target.invisible) {
			this.target = null;
		}

		for (Player player : this.colliding) {
			// player.modifyHp(-this.damage);
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

		if (Random.chance(10)) {
			items.add(new HealingPotion());
		}

		if (Random.chance(5)) {
			items.add(new SunPotion());
		}

		return items;
	}

	@Override
	protected void die() {
		if (!this.dead) {
			this.drop = true;
		}

		if (!Player.instance.isDead()) {
			for (int i = 0; i < this.experienceDropped; i++) {
				Dungeon.ui.add(new ExpFx(this.x + this.w / 2, this.y + this.w / 2));
			}
		}

		super.die();
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

	@Override
	protected void onHurt() {
		super.onHurt();

		this.become("alerted");

		if (this.ai != null) {
			this.ai.checkForPlayer(true);
		}

		if (this.hp <= 0) {
			if (this.room != null) {
				this.room.numEnemies -= 1;
			}
		}
	}

	public Room lastRoom;

	public class State<T extends Mob> {
		public T self;
		public float t;
		public Point nextPathPoint;
		public Point targetPoint;

		public void checkForFlee() {
			if (self.flee >= (self.mind == Mind.COWARD ? 0.5f : (self.mind == Mind.ATTACKER ? 1.5f : 1f))
				|| self.saw && self.hp < (self.mind == Mind.COWARD ? self.hpMax / 3 * 2 : (self.mind == Mind.ATTACKER ? self.hpMax / 4 : self.hpMax / 3))) {

				if (Dungeon.world.isLocked()) {
					Log.error("World is locked!");
				} else {
					self.become("fleeing");
				}
			}
		}

		public void update(float dt) {
			this.checkForFlee();
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

		public boolean moveTo(Point point, float s) {
			return this.moveTo(point, s, 4f);
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

			if (self.target != null) {
				if (!self.saw && (force || self.target.heat / 3 > Level.heat + 1) && self.canSee(self.target)) {
					Level.heat += 1f;
					self.saw = true;
					self.hideSignT = 0f;
					self.noticeSignT = 2f;
					this.checkForFlee();

					if (!self.state.equals("chase")) {
						self.become("alerted");
					}
				}
			} else {
				self.saw = false;
			}
		}

		public Room currentRoom;
		public Point water;
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

				boolean found = false;

				for (int i = 0; i < this.target.getWidth() * this.target.getHeight(); i++) {
					this.targetPoint = this.target.getRandomCell();

					if (Dungeon.level.isValid((int) this.targetPoint.x, (int) this.targetPoint.y) && Dungeon.level.checkFor((int) this.targetPoint.x, (int) this.targetPoint.y, Terrain.PASSABLE)) {
						found = true;
						this.targetPoint.mul(16);
						break;
					}
				}

				if (!found) {
					Log.error("Not found target point");

					this.target = null;
					this.targetPoint = null;
				}
			}
		}
	}

	public boolean saw;
}