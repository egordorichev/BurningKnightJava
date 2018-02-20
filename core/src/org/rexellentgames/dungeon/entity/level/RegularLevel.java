package org.rexellentgames.dungeon.entity.level;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.creature.mob.Knight;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.Money;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;
import org.rexellentgames.dungeon.util.path.Graph;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Rect;

import java.util.ArrayList;
import java.util.Arrays;

public class RegularLevel extends Level {
	private static int MIN_ROOM_SIZE = 7;
	private static int MAX_ROOM_SIZE = 9;

	protected ArrayList<Room> rooms = new ArrayList<Room>();
	protected ArrayList<Room.Type> special;
	private ArrayList<Item> toSpawn = new ArrayList<Item>();
	private ArrayList<SaveableEntity> saveable = new ArrayList<SaveableEntity>();
	private boolean[] busy;

	protected Room entrance;
	protected Room exit;

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

		return true;
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
		return new Money().randomize();
	}

	protected void drop(int cell, Item item) {
		this.busy[cell] = true;

		item.x = cell % WIDTH * 16;
		item.y = (int) (Math.floor(cell / WIDTH) * 16);

		this.area.add(item);
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
		}
	}

	protected Mob getRandomMob() {
		// todo: random!
		return new Knight();
	}

	private void addPhysics() {
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

						poly.set(new Vector2[]{
							new Vector2(xx, yy), new Vector2(xx + 16, yy),
							new Vector2(xx, yy + 16), new Vector2(xx + 16, yy + 16)
						});

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
					this.set(door.x, door.y, Terrain.FLOOR);
					break;
				case REGULAR:
					this.set(door.x, door.y, Terrain.DOOR);
					break;
				case TUNNEL:
					this.set(door.x, door.y, Terrain.FLOOR);
					break;
				case SECRET:
					this.set(door.x, door.y, Terrain.SECRET_DOOR);
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
		int count = stream.readInt32();
	}

	@Override
	protected void writeData(FileWriter stream) throws Exception {
		stream.writeInt32(this.saveable.size());

		/*for (int i = 0; i < this.saveable.size(); i++) {
			SaveableEntity entity = this.saveable.get(i);

			stream.writeString(entity.getClass().toString());
		}*/
	}
}