package org.rexellentgames.dungeon.entity.level;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.creature.mob.Knight;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.Money;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.features.Room;
import org.rexellentgames.dungeon.entity.level.painter.Patch;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;
import org.rexellentgames.dungeon.util.path.Graph;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Rect;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;

public class RegularLevel extends Level {
	private static final int MIN_ROOM_SIZE = 14;
	private static final int MAX_ROOM_SIZE = 18;

	private static final short[] WALLS = new short[] { 99, 67, 96, 64, 3, 35, 0, 32, 98, 66, 97, 65, 2, 34, 1, 33 };
	private static final short[] CORNERS = new short[] { 33, 128, 129, 130, 131, 160, 161, 162, 163, 192, 193, 194, 195,
		224, 225, 33 };

	private static final short[] LEFT_SLOPE = new short[] { 32, 256, 257, 258 };
	private static final short[] RIGHT_SLOPE = new short[] { 34, 288, 289, 290 };
	private static final short[] TOP_SLOPE = new short[] { 1, 320, 321, 322 };

	protected ArrayList<org.rexellentgames.dungeon.entity.level.features.Room> rooms = new ArrayList<org.rexellentgames.dungeon.entity.level.features.Room>();
	protected ArrayList<org.rexellentgames.dungeon.entity.level.features.Room.Type> special;
	private ArrayList<Item> toSpawn = new ArrayList<Item>();
	private ArrayList<SaveableEntity> saveable = new ArrayList<SaveableEntity>();
	private boolean[] busy;

	protected org.rexellentgames.dungeon.entity.level.features.Room entrance;
	protected org.rexellentgames.dungeon.entity.level.features.Room exit;

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

		this.entrance.setType(org.rexellentgames.dungeon.entity.level.features.Room.Type.ENTRANCE);
		this.exit.setType(org.rexellentgames.dungeon.entity.level.features.Room.Type.EXIT);

		ArrayList<org.rexellentgames.dungeon.entity.level.features.Room> connected = new ArrayList<org.rexellentgames.dungeon.entity.level.features.Room>();
		connected.add(this.entrance);

		Graph.buildDistanceMap(this.rooms, this.exit);
		ArrayList<org.rexellentgames.dungeon.entity.level.features.Room> path = Graph.buildPath(this.rooms, this.entrance, this.exit);

		org.rexellentgames.dungeon.entity.level.features.Room room = this.entrance;

		for (org.rexellentgames.dungeon.entity.level.features.Room next : path) {
			room.connectWithRoom(next);
			room.setPrice(this.entrance.getDistance());
			room = next;

			connected.add(room);
		}

		Graph.buildDistanceMap(this.rooms, this.exit);
		path = Graph.buildPath(this.rooms, this.entrance, this.exit);
		room = this.entrance;

		for (org.rexellentgames.dungeon.entity.level.features.Room next : path) {
			room.connectWithRoom(next);
			room = next;

			connected.add(room);
		}

		int nConnected = (int) (rooms.size() * Random.newFloat(0.7f, 0.9f));

		while (connected.size() < nConnected) {
			org.rexellentgames.dungeon.entity.level.features.Room cr = connected.get(Random.newInt(connected.size()));
			org.rexellentgames.dungeon.entity.level.features.Room or = cr.getRandomNeighbour();

			if (!connected.contains(or)) {
				cr.connectWithRoom(or);
				connected.add(or);
			}
		}

		this.special = new ArrayList<org.rexellentgames.dungeon.entity.level.features.Room.Type>(org.rexellentgames.dungeon.entity.level.features.Room.SPECIAL);

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

		Camera.instance.follow(player);
		this.addSaveable(player);

		return true;
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

					int tile = 0;

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
					} else {
						tile = WALLS[count];
					}

					this.set(x, y, (short) tile);
				} else if (this.isWater(x, y)) {
					int count = 0;

					if (this.isWater(x, y + 1)) { count += 1; }
					if (this.isWater(x + 1, y)) { count += 2; }
					if (this.isWater(x, y - 1)) { count += 4; }
					if (this.isWater(x - 1, y)) { count += 8; }

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
					}
				}
			}
		}
	}

	private boolean isAWall(int x, int y) {
		if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
			return true;
		}

		int tile = this.get(x, y);
		int xx = tile % 32;
		int yy = (int) (Math.floor(tile / 32));

		return xx < 4 && yy < 11;
	}

	private boolean isWater(int x, int y) {
		if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) {
			return true;
		}

		int tile = this.get(x, y);
		int xx = tile % 32;
		int yy = (int) (Math.floor(tile / 32));

		return (xx < 3 && yy > 10 && yy < 15) || this.isAWall(x, y);
	}

	public void addItemToSpawn(Item item) {
		this.toSpawn.add(item);
	}

	private void spawnItems() {
		int n = 3;

		while (Random.newFloat() < 0.4f) {
			n++;
		}

		for (int i = 0; i < n; i++) {
			this.drop(this.getRandomCell(), this.getRandomItem());
		}
	}

	protected int getRandomCell() {
		while (true) {
			org.rexellentgames.dungeon.entity.level.features.Room room = this.getRandomRoom(org.rexellentgames.dungeon.entity.level.features.Room.Type.REGULAR);

			if (room == null) {
				continue;
			}

			int cell = room.getRandomCell();

			if (!this.busy[cell] && this.checkFor(cell, Terrain.PASSABLE)) {
				return cell;
			}
		}
	}

	public org.rexellentgames.dungeon.entity.level.features.Room getRandomRoom(org.rexellentgames.dungeon.entity.level.features.Room.Type type) {
		for (int i = 0; i < 10; i++) {
			org.rexellentgames.dungeon.entity.level.features.Room room = this.rooms.get(Random.newInt(this.rooms.size()));

			if (room.getType() == type) {
				return room;
			}
		}

		return null;
	}

	protected Item getRandomItem() {
		// todo: random!
		return new Money().randomize();
	}

	protected void drop(int cell, Item item) {
		this.busy[cell] = true;

		item.x = cell % WIDTH * 16;
		item.y = (int) (Math.floor(cell / WIDTH) * 16);

		this.area.add(item);
		this.addSaveable(item);
	}

	public void addSaveable(SaveableEntity thing) {
		this.saveable.add(thing);
	}

	protected int getNumberOfMobsToSpawn() {
		return 2 + Dungeon.level % 5 + Random.newInt(3);
	}

	private void spawnMobs() {
		int n = this.getNumberOfMobsToSpawn();

		for (int i = 0; i < n; i++) {
			Mob mob = this.getRandomMob();
			int cell = this.getRandomCell();

			mob.x = cell % WIDTH * 16;
			mob.y = (int) (Math.floor(cell / WIDTH) * 16);

			this.addSaveable(mob);
		}
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

	public boolean checkFor(int i, int flag) {
		return (Terrain.flags[this.get(i)] & flag) == flag;
	}

	public boolean checkFor(int x, int y, int flag) {
		return (Terrain.flags[this.get(x, y)] & flag) == flag;
	}

	protected void assignRooms() {
		int specialRooms = 0;

		for (org.rexellentgames.dungeon.entity.level.features.Room room : rooms) {
			if (room.getType() == org.rexellentgames.dungeon.entity.level.features.Room.Type.NULL &&
				room.getConnected().size() == 1) {

				if (this.special.size() > 0 &&
					room.getWidth() > 3 && room.getHeight() > 3 &&
					Random.newInt(specialRooms * specialRooms + 2) == 0) {

					int n = this.special.size();
					org.rexellentgames.dungeon.entity.level.features.Room.Type type = this.special.get(Math.min(Random.newInt(n), Random.newInt(n)));

					room.setType(type);

					this.special.remove(type);
					specialRooms++;
				}
			}
		}

		int count = 0;

		for (org.rexellentgames.dungeon.entity.level.features.Room room : this.rooms) {
			int connected = room.getConnected().size();

			if (room.getType() == org.rexellentgames.dungeon.entity.level.features.Room.Type.NULL && connected > 0) {
				if (Random.newInt(connected * connected) == 0) {
					room.setType(org.rexellentgames.dungeon.entity.level.features.Room.Type.REGULAR);
					count++;
				} else {
					room.setType(org.rexellentgames.dungeon.entity.level.features.Room.Type.TUNNEL);
				}
			}
		}

		// Make sure, we have enough rooms
		while (count < 4) {
			org.rexellentgames.dungeon.entity.level.features.Room room = this.randomRoom(org.rexellentgames.dungeon.entity.level.features.Room.Type.TUNNEL, 1);

			if (room != null) {
				room.setType(org.rexellentgames.dungeon.entity.level.features.Room.Type.TUNNEL);
				count++;
			}
		}
	}

	public org.rexellentgames.dungeon.entity.level.features.Room randomRoom(org.rexellentgames.dungeon.entity.level.features.Room.Type type, int attempts) {
		for (int i = 0; i < attempts; i++) {
			org.rexellentgames.dungeon.entity.level.features.Room room = this.rooms.get(Random.newInt(this.rooms.size()));

			if (room.getType() == type) {
				return room;
			}
		}

		return null;
	}

	protected void paint() {
		for (org.rexellentgames.dungeon.entity.level.features.Room room : this.rooms) {
			if (room.getType() != org.rexellentgames.dungeon.entity.level.features.Room.Type.NULL) {
				this.placeDoors(room);
				room.getType().paint(this, room);
			}
		}

		for (org.rexellentgames.dungeon.entity.level.features.Room room : this.rooms) {
			this.paintDoors(room);
		}
	}

	protected void placeDoors(org.rexellentgames.dungeon.entity.level.features.Room room) {
		for (org.rexellentgames.dungeon.entity.level.features.Room n : room.getConnected().keySet()) {
			org.rexellentgames.dungeon.entity.level.features.Door door = room.getConnected().get(n);

			if (door == null) {
				Rect i = room.intersect(n);

				if (i.getWidth() == 0) {
					door = new org.rexellentgames.dungeon.entity.level.features.Door(i.left, Random.newInt(i.top + 1, i.bottom));
				} else {
					door = new org.rexellentgames.dungeon.entity.level.features.Door(Random.newInt(i.left + 1, i.right), i.top);
				}

				room.getConnected().put(n, door);
				n.getConnected().put(room, door);
			}
		}
	}

	protected void paintDoors(org.rexellentgames.dungeon.entity.level.features.Room room) {
		for (org.rexellentgames.dungeon.entity.level.features.Room other : room.getConnected().keySet()) {
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
		return Patch.generate(0.4f, 5);
	}

	protected boolean[] getWater() {
		return Patch.generate(0.4f, 4);
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

			this.rooms.add((org.rexellentgames.dungeon.entity.level.features.Room) new Room().set(rect));
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
		int count = stream.readInt32();

		for (int i = 0; i < count; i++) {
			String type = stream.readString();

			Class<?> clazz = Class.forName(type);
			Constructor<?> constructor = clazz.getConstructor();
			Object object = constructor.newInstance(new Object[] {});

			SaveableEntity entity = (SaveableEntity) object;

			this.area.add(entity);
			this.saveable.add(entity);

			entity.load(stream);
		}
	}

	@Override
	protected void writeData(FileWriter stream) throws Exception {
		stream.writeInt32(this.saveable.size());

		for (int i = 0; i < this.saveable.size(); i++) {
			SaveableEntity entity = this.saveable.get(i);

			stream.writeString(entity.getClass().getName());
			entity.save(stream);
		}
	}
}