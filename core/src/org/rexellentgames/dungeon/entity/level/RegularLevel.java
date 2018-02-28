package org.rexellentgames.dungeon.entity.level;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.mob.BurningKnight;
import org.rexellentgames.dungeon.entity.creature.mob.Knight;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Gold;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.item.consumable.potion.PotionRegistry;
import org.rexellentgames.dungeon.entity.item.weapon.Dagger;
import org.rexellentgames.dungeon.entity.item.weapon.Sword;
import org.rexellentgames.dungeon.entity.level.entities.Entrance;
import org.rexellentgames.dungeon.entity.level.entities.Exit;
import org.rexellentgames.dungeon.entity.level.entities.Torch;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.features.Room;
import org.rexellentgames.dungeon.entity.level.painter.Patch;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;
import org.rexellentgames.dungeon.util.geometry.Rect;
import org.rexellentgames.dungeon.util.path.Graph;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;

public class RegularLevel extends Level {
	public static RegularLevel instance;

	private static final int MIN_ROOM_SIZE = 13;
	private static final int MAX_ROOM_SIZE = 11;

	private static final short[] WALLS = new short[] { 99, 67, 96, 64, 3, 35, 0, 32, 98, 66, 97, 65, 2, 34, 1, 33 };
	private static final short[] CORNERS = new short[] { 33, 128, 129, 130, 131, 160, 161, 162, 163, 192, 193, 194, 195,
		224, 225, 33 };

	private static final short[] LEFT_SLOPE = new short[] { 32, 256, 257, 258 };
	private static final short[] RIGHT_SLOPE = new short[] { 34, 288, 289, 290 };
	private static final short[] TOP_SLOPE = new short[] { 1, 320, 321, 322 };

	protected ArrayList<Room> rooms = new ArrayList<Room>();
	protected ArrayList<Room.Type> special;
	private ArrayList<Item> toSpawn = new ArrayList<Item>();
	private ArrayList<SaveableEntity> saveable = new ArrayList<SaveableEntity>();
	private boolean[] busy;

	protected Room entrance;
	protected Room exit;

	public RegularLevel() {
		instance = this;
	}

	@Override
	public boolean generate() {
		if (!this.initRooms()) {
			return false;
		}

		Arrays.fill(this.data, Terrain.WALL);

		int distance;
		int retry = 0;
		int minDistance = (int) Math.sqrt(this.rooms.size());

		do {
			// Make sure that the entrance is big enough
			do {
				this.entrance = this.rooms.get(Random.newInt(this.rooms.size()));
			} while (this.entrance.getWidth() < 4 && this.entrance.getHeight() < 4);

			// Make sure that the exit is big enough
			do {
				this.exit = this.rooms.get(Random.newInt(this.rooms.size()));
			} while (this.exit.getWidth() < 4 && this.exit.getHeight() < 4);

			Graph.buildDistanceMap(this.rooms, this.exit);
			distance = this.entrance.getDistance();

			if (retry++ > 10) {
				Log.error("To many attempts");
				return false;
			}
		} while (distance < minDistance);

		Log.info("The distance is " + distance);

		this.entrance.setType(Room.Type.ENTRANCE);
		this.exit.setType(Room.Type.EXIT);

		ArrayList<Room> connected = new ArrayList<Room>();
		connected.add(this.entrance);

		Graph.buildDistanceMap(this.rooms, this.exit);
		ArrayList<Room> path = Graph.buildPath(this.rooms, this.entrance, this.exit);

		Room room = this.entrance;
		
		for (Room next : path) {
			room.connectWithRoom(next);
			room.setPrice(this.entrance.getDistance());
			room = next;

			connected.add(room);
		}

		Graph.buildDistanceMap(this.rooms, this.exit);
		path = Graph.buildPath(this.rooms, this.entrance, this.exit);
		room = this.entrance;

		for (Room next : path) {
			room.connectWithRoom(next);
			room = next;

			connected.add(room);
		}

		int nConnected = (int) (rooms.size() * Random.newFloat(0.7f, 0.9f));

		while (connected.size() < nConnected) {
			Room cr = connected.get(Random.newInt(connected.size()));
			Room or = cr.getRandomNeighbour();

			if (!connected.contains(or)) {
				cr.connectWithRoom(or);
				connected.add(or);
			}
		}

		this.special = new ArrayList<Room.Type>(Room.SPECIAL);

		this.assignRooms();
		this.paint();
		this.paintGrass();
		this.paintWater();

		this.busy = new boolean[SIZE];

		this.spawnItems();
		this.spawnMobs();

		this.addPhysics();

		this.tileUp();

		Player player = (Player) this.area.add(new Player());
		player.getBody().setTransform(this.spawn.x * 16, this.spawn.y * 16, 0);

		ItemHolder sword = new ItemHolder();
		sword.setItem(new Dagger());
		player.getInventory().add(sword);

		Camera.instance.follow(player);
		this.addSaveable(player);

		PotionRegistry.generate();

		return true;
	}

	public void loadPassable() {
		this.passable = new boolean[SIZE];
		this.low = new boolean[SIZE];

		for (int i = 0; i < SIZE; i++) {
			this.passable[i] = this.checkFor(i, Terrain.PASSABLE);
			this.low[i] = this.passable[i] || this.checkFor(i, Terrain.LOW);
		}
	}

	private void tileUp() {
		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				if (this.isAWall(x, y)) {
					int count = 0;

					if (this.isAWall(x, y + 1)) { count += 1; }
					if (this.isAWall(x + 1, y)) { count += 2; }
					if (this.isAWall(x, y - 1)) { count += 4; }
					if (this.isAWall(x - 1, y)) { count += 8; }

					int tile;

					if (count == 15) {
						count = 0;

						if (!this.isAWall(x + 1, y + 1)) { count += 1; }
						if (!this.isAWall(x + 1, y - 1)) { count += 2; }
						if (!this.isAWall(x - 1, y - 1)) { count += 4; }
						if (!this.isAWall(x - 1, y + 1)) { count += 8; }

						tile = CORNERS[count];
					} else if (count == 7) {
						count = 0;

						if (!this.isAWall(x + 1, y + 1)) { count += 1; }
						if (!this.isAWall(x + 1, y - 1)) { count += 2; }

						tile = LEFT_SLOPE[count];
					} else if (count == 13) {
						count = 0;

						if (!this.isAWall(x - 1, y + 1)) {
							count += 1;
						}

						if (!this.isAWall(x - 1, y - 1)) {
							count += 2;
						}

						tile = RIGHT_SLOPE[count];
					} else if (count == 14) {
						count = 0;

						if (!this.isAWall(x + 1, y - 1)) {
							count += 1;
						}
						if (!this.isAWall(x - 1, y - 1)) {
							count += 2;
						}

						tile = TOP_SLOPE[count];
					} else if (count == 6) {
						tile = (this.isAWall(x + 1, y - 1) ? 0 : 227);
					} else if (count == 12) {
						tile = (this.isAWall(x - 1, y - 1) ? 2 : 259);
					} else if (count == 11 && Random.newFloat() > 0.9) {
						tile = Terrain.DECO;
						Torch torch = new Torch(x * 16, y * 16);

						this.area.add(torch);
						this.addSaveable(torch);
					} else {
						tile = WALLS[count];
					}

					this.set(x, y, (short) tile);
				} else if (this.isWater(x, y)) {
					int count = 0;

					if (this.isWaterOrFall(x, y + 1)) { count += 1; }
					if (this.isWaterOrFall(x + 1, y)) { count += 2; }
					if (this.isWaterOrFall(x, y - 1)) { count += 4; }
					if (this.isWaterOrFall(x - 1, y)) { count += 8; }

					this.set(x, y, (short) (WALLS[count] + 352));
				} else {
					int tile = this.get(x, y);

					if (tile == Terrain.FLOOR) {
						int replace = Random.newInt(7);

						if (replace != 0 && Random.newFloat() > 0.5) {
							this.set(x, y, (short) (6 + replace));
						}
					} else if (tile == Terrain.DOOR) {
						this.set(x, y, Terrain.FLOOR);
						this.addSaveable((SaveableEntity) this.area.add(new org.rexellentgames.dungeon.entity.level.entities.Door(
							x, y, this.checkFor(x, y + 1, Terrain.SOLID)
						)));
					} else if (tile == Terrain.EMPTY) {
						if (this.checkFor(x, y + 1, Terrain.PASSABLE))  {
							this.set(x, y, this.isWater(x, y + 1) ? Terrain.WATER_FALL : Terrain.FALL);
						}
					} else if (tile == Terrain.EXIT) {
						Exit exit = new Exit();

						exit.x = x * 16;
						exit.y = y * 16;

						this.area.add(exit);
						this.addSaveable(exit);
					} else if (tile == Terrain.ENTRANCE) {
						Entrance entrance = new Entrance();

						entrance.x = x * 16;
						entrance.y = y * 16;

						this.area.add(entrance);
						this.addSaveable(entrance);
						this.set(x, y, Terrain.FLOOR);
					}
				}
			}
		}
	}

	public void addItemToSpawn(Item item) {
		this.toSpawn.add(item);
	}

	private void spawnItems() {
		int n = 32;

		while (Random.newFloat() < 0.4f) {
			n++;
		}

		for (int i = 0; i < n; i++) {
			this.drop(this.getRandomCell(), this.getRandomItem());
		}
	}

	protected int getRandomCell() {
		while (true) {
			Room room = this.getRandomRoom(Room.Type.REGULAR);

			if (room == null) {
				continue;
			}

			int cell = room.getRandomCell();

			if (!this.busy[cell] && this.checkFor(cell, Terrain.PASSABLE)) {
				return cell;
			}
		}
	}

	public Room getRandomRoom(Room.Type type) {
		for (int i = 0; i < 10; i++) {
			Room room = this.rooms.get(Random.newInt(this.rooms.size()));

			if (room.getType() == type) {
				return room;
			}
		}

		return null;
	}

	protected Item getRandomItem() {
		// todo: random!
		return (Random.newFloat() > 0.5 ? new Gold().randomize() : new Sword());
	}

	protected void drop(int cell, Item item) {
		this.busy[cell] = true;
		ItemHolder holder = new ItemHolder();

		holder.setItem(item);
		holder.x = cell % WIDTH * 16;
		holder.y = (int) (Math.floor(cell / WIDTH) * 16);

		this.area.add(holder);
		this.addSaveable(holder);
	}

	public void addSaveable(SaveableEntity thing) {
		thing.setLevel(this);
		this.saveable.add(thing);
	}

	public void removeSaveable(SaveableEntity thing) {
		this.saveable.remove(thing);
	}

	protected int getNumberOfMobsToSpawn() {
		return 5 + 3 * Dungeon.level % 5 + Random.newInt(10);
	}

	private void spawnMobs() {
		int n = this.getNumberOfMobsToSpawn();

		for (int i = 0; i < n; i++) {
			Mob mob = this.getRandomMob();
			int cell = this.getRandomCell();

			mob.x = cell % WIDTH * 16;
			mob.y = (int) (Math.floor(cell / WIDTH) * 16);

			this.area.add(mob);
			this.addSaveable(mob);
		}

		BurningKnight knight = new BurningKnight();

		this.addSaveable(knight);
		this.area.add(knight);
	}

	protected Mob getRandomMob() {
		// todo: random!
		return new Knight();
	}

	@Override
	protected void addPhysics() {
		World world = this.area.getState().getWorld();

		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.StaticBody;

		Body body = world.createBody(def);

		ArrayList<Vector2> marked = new ArrayList<Vector2>();

		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				if (this.checkFor(x, y, Terrain.SOLID)) {
					int total = 0;

					for (Vector2 vec : NEIGHBOURS8V) {
						Vector2 v = new Vector2(x + vec.x, y + vec.y);

						if (v.x >= 0 && v.y >= 0 && v.x < WIDTH && v.y < WIDTH) {
							if (this.checkFor((int) v.x, (int) v.y, Terrain.SOLID)) {
								total++;
							}
						}
					}

					if (total < 8) {
						PolygonShape poly = new PolygonShape();
						int xx = x * 16;
						int yy = y * 16;


						if (yy == 0 || this.checkFor(x, y - 1, Terrain.SOLID)) {
							poly.set(new Vector2[]{
								new Vector2(xx, yy), new Vector2(xx + 16, yy),
								new Vector2(xx, yy + 16), new Vector2(xx + 16, yy + 16)
							});
						} else {
							poly.set(new Vector2[]{
								new Vector2(xx, yy + 8), new Vector2(xx + 16, yy + 8),
								new Vector2(xx, yy + 16), new Vector2(xx + 16, yy + 16)
							});
						}

						FixtureDef fixture = new FixtureDef();

						fixture.shape = poly;
						fixture.friction = 0;

						body.createFixture(fixture);

						poly.dispose();

						if (this.get(x, y) == Terrain.WALL) {
							marked.add(new Vector2(x, y));
						}
					}
				}
			}
		}
	}

	protected void assignRooms() {
		int specialRooms = 0;

		for (Room room : rooms) {
			if (room.getType() == Room.Type.NULL &&
				room.getConnected().size() == 1) {

				if (this.special.size() > 0 &&
					room.getWidth() > 3 && room.getHeight() > 3 &&
					Random.newInt(specialRooms * specialRooms + 2) == 0) {

					int n = this.special.size();
					Room.Type type = this.special.get(Math.min(Random.newInt(n), Random.newInt(n)));

					room.setType(type);

					this.special.remove(type);
					specialRooms++;
				}
			}
		}

		int count = 0;

		for (Room room : this.rooms) {
			int connected = room.getConnected().size();

			if (room.getType() == Room.Type.NULL && connected > 0) {
				if (Random.newInt(connected * connected) == 0) {
					room.setType(Room.Type.REGULAR);
					count++;
				} else {
					room.setType(Room.Type.TUNNEL);
				}
			}
		}

		// Make sure, we have enough rooms
		while (count < 4) {
			Room room = this.randomRoom(Room.Type.TUNNEL, 1);

			if (room != null) {
				room.setType(Room.Type.TUNNEL);
				count++;
			}
		}
	}

	public Room randomRoom(Room.Type type, int attempts) {
		for (int i = 0; i < attempts; i++) {
			Room room = this.rooms.get(Random.newInt(this.rooms.size()));

			if (room.getType() == type) {
				return room;
			}
		}

		return null;
	}

	protected void paint() {
		for (Room room : this.rooms) {
			if (room.getType() != Room.Type.NULL) {
				this.placeDoors(room);
				room.getType().paint(this, room);
			}
		}

		for (Room room : this.rooms) {
			this.paintDoors(room);
		}
	}

	protected void placeDoors(Room room) {
		for (Room n : room.getConnected().keySet()) {
			Door door = room.getConnected().get(n);

			if (door == null) {
				Rect i = room.intersect(n);

				if (i.getWidth() == 0) {
					door = new Door(i.left, Random.newInt(i.top + 1, i.bottom));
				} else {
					door = new Door(Random.newInt(i.left + 1, i.right), i.top);
				}

				room.getConnected().put(n, door);
				n.getConnected().put(room, door);
			}
		}
	}

	protected void paintDoors(Room room) {
		for (Room other : room.getConnected().keySet()) {
			Door door = room.getConnected().get(other);

			// todo: proper tiles here
			switch (door.getType()) {
				case EMPTY:
					this.set((int) door.x, (int) door.y, Terrain.FLOOR);
					break;
				case REGULAR:
					this.set((int) door.x, (int) door.y, Terrain.DOOR);
					break;
				case TUNNEL:
					this.set((int) door.x, (int) door.y, Terrain.FLOOR);
					break;
				case SECRET:
					// Todo
					// this.set(door.x, door.y, Terrain.SECRET_DOOR);
					break;
			}
		}
	}

	protected boolean[] getGrass() {
		return Patch.generate(0.5f, 5);
	}

	protected boolean[] getWater() {
		return Patch.generate(0.5f, 4);
	}

	protected void paintGrass() {
		boolean[] grass = this.getGrass();

		for (int i = WIDTH + 1; i < SIZE - WIDTH - 1; i++) {
			if (grass[i] && this.get(i) == Terrain.FLOOR) {
				int count = 1;

				for (int n : NEIGHBOURS8) {
					if (grass[i + n]) {
						count++;
					}
				}

				this.set(i, (Random.newFloat() < count / 12f) ? Terrain.GRASS : Terrain.LOW_GRASS);
			}
		}
	}

	protected void paintWater() {
		boolean[] grass = this.getWater();

		for (int i = WIDTH + 1; i < SIZE - WIDTH - 1; i++) {
			if (grass[i] && this.get(i) == Terrain.FLOOR) {
				this.set(i, Terrain.WATER);
			}
		}
	}

	protected boolean initRooms() {
		this.rooms.clear();
		this.split(new Rect(0, 0, WIDTH - 1, HEIGHT - 1));

		if (this.rooms.size() < 8) {
			Log.error("Not enough rooms generated");
			return false;
		}

		for (int i = 0; i < this.rooms.size() - 1; i++) {
			for (int j = i + 1; j < this.rooms.size(); j++) {
				rooms.get(i).connectTo(rooms.get(j));
			}
		}

		return true;
	}

	private void split(Rect rect) {
		int w = rect.getWidth();
		int h = rect.getHeight();

		if (w > MAX_ROOM_SIZE && h < MIN_ROOM_SIZE) {
			int vw = Random.newInt(rect.left + 3, rect.right - 3);

			this.split(new Rect(rect.left, rect.top, vw, rect.bottom));
			this.split(new Rect(vw, rect.top, rect.right, rect.bottom));
		} else if (h > MAX_ROOM_SIZE && w < MIN_ROOM_SIZE) {
			int vh = Random.newInt(rect.top + 3, rect.bottom - 3);

			this.split(new Rect(rect.left, rect.top, rect.right, vh));
			this.split(new Rect(rect.left, vh, rect.right, rect.bottom));
		} else if ((Math.random() <= (MIN_ROOM_SIZE * MIN_ROOM_SIZE /
			(rect.getWidth() * rect.getHeight())) && w <= MAX_ROOM_SIZE && h <= MAX_ROOM_SIZE)
			|| w < MIN_ROOM_SIZE || h < MIN_ROOM_SIZE) {

			this.rooms.add((Room) new Room().set(rect));
		} else {
			if (Random.newFloat() < (float) (w - 2) / (w + h - 4)) {
				int vw = Random.newInt(rect.left + 3, rect.right - 3);

				this.split(new Rect(rect.left, rect.top, vw, rect.bottom));
				this.split(new Rect(vw, rect.top, rect.right, rect.bottom));
			} else {
				int vh = Random.newInt(rect.top + 3, rect.bottom - 3);

				this.split(new Rect(rect.left, rect.top, rect.right, vh));
				this.split(new Rect(rect.left, vh, rect.right, rect.bottom));
			}
		}
	}

	@Override
	protected void loadData(FileReader stream) throws Exception {
		PotionRegistry.load(stream);
		int count = stream.readInt32();

		for (int i = 0; i < count; i++) {
			String type = stream.readString();

			Class<?> clazz = Class.forName(type);
			Constructor<?> constructor = clazz.getConstructor();
			Object object = constructor.newInstance(new Object[] { });

			SaveableEntity entity = (SaveableEntity) object;

			entity.setLevel(this);

			this.area.add(entity);
			this.saveable.add(entity);

			entity.load(stream);
		}
	}

	@Override
	protected void writeData(FileWriter stream) throws Exception {
		PotionRegistry.save(stream);
		stream.writeInt32(this.saveable.size());

		for (int i = 0; i < this.saveable.size(); i++) {
			SaveableEntity entity = this.saveable.get(i);

			stream.writeString(entity.getClass().getName());
			entity.save(stream);
		}
	}
}