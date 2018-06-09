package org.rexellentgames.dungeon.entity.level;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.creature.mob.BurningKnight;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.item.Bomb;
import org.rexellentgames.dungeon.entity.item.ChangableRegistry;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.level.builders.Builder;
import org.rexellentgames.dungeon.entity.level.builders.CastleBuilder;
import org.rexellentgames.dungeon.entity.level.builders.LineBuilder;
import org.rexellentgames.dungeon.entity.level.builders.LoopBuilder;
import org.rexellentgames.dungeon.entity.level.entities.Entrance;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.entity.level.rooms.connection.ConnectionRoom;
import org.rexellentgames.dungeon.entity.level.rooms.ladder.EntranceRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.LampRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.RegularRoom;
import org.rexellentgames.dungeon.entity.level.rooms.special.SpecialRoom;
import org.rexellentgames.dungeon.entity.level.save.LevelSave;
import org.rexellentgames.dungeon.entity.level.save.PlayerSave;
import org.rexellentgames.dungeon.entity.pool.MobPool;
import org.rexellentgames.dungeon.entity.pool.room.EntranceRoomPool;
import org.rexellentgames.dungeon.entity.pool.room.SecretRoomPool;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.util.ArrayList;
import java.util.Collections;

public abstract class RegularLevel extends Level {
	@Override
	public void generate() {
		Level.GENERATED = true;

		this.itemsToSpawn.clear();

		if (Dungeon.depth > 0) {
			for (int i = 0; i < Random.newInt(4); i++) {
				this.itemsToSpawn.add(new Bomb());
			}
		}

		this.build();
		this.paint();

		Log.info("Done painting");

		ChangableRegistry.generate();

		this.loadPassable();

		Log.info("Spawning entities...");

		this.spawnLevelEntities();
		this.spawnEntities();

		if (Dungeon.type == Dungeon.Type.REGULAR && BurningKnight.instance == null && Dungeon.depth > 0) {
			Log.info("Adding BK...");

			BurningKnight knight = new BurningKnight();

			Dungeon.area.add(knight);
			PlayerSave.add(knight);

			knight.findStartPoint();
		}

		Log.info("Done!");
	}

	protected void spawnLevelEntities() {
		this.free = new boolean[this.getSIZE()];

		if (Dungeon.depth > 0) {
			MobPool.instance.initForFloor();

			for (Room room : this.rooms) {
				// todo: remove from here
				if (room instanceof RegularRoom) {
					float weight = Random.newFloat(1f, 2f);

					while (weight > 0) {
						Mob mob = MobPool.instance.generate();
						weight -= mob.getWeight();

						Point point;

						do {
							point = room.getRandomCell();
						} while (!Dungeon.level.checkFor((int) point.x, (int) point.y, Terrain.PASSABLE));

						mob.generate();

						Dungeon.area.add(mob);
						LevelSave.add(mob);


						mob.tp(point.x * 16, point.y * 16);
					}
				}
			}
		}

		for (Item item : this.itemsToSpawn) {
			Point point = null;

			while (point == null) {
				point = this.getRandomFreePoint(RegularRoom.class);
			}

			ItemHolder holder = new ItemHolder();

			item.generate();
			holder.setItem(item);
			holder.x = point.x * 16 + Random.newInt(-4, 4);
			holder.y = point.y * 16 + Random.newInt(-4, 4);

			holder.add();
			this.area.add(holder);
		}

		this.itemsToSpawn.clear();
	}

	protected void spawnEntities() {

	}

	public static Entrance ladder;

	protected void paint() {
		Log.info("Painting...");

		Painter painter = this.getPainter();

		if (painter != null) {
			painter.paint(this, this.rooms);
			painter.draw(this, this.rooms);
		} else {
			Log.error("No painter!");
		}
	}

	protected void build() {
		Builder builder = this.getBuilder();

		ArrayList<Room> rooms = this.createRooms();
		Collections.shuffle(rooms);

		do {
			Log.info("Generating...");

			for (Room room : rooms) {
				room.getConnected().clear();
				room.getNeighbours().clear();
			}

			this.rooms = builder.build((ArrayList<Room>) rooms.clone());

			if (this.rooms == null) {
				Log.error("Failed!");
			}
		} while (this.rooms == null);
	}

	protected ArrayList<Room> createRooms() {
		ArrayList<Room> rooms = new ArrayList<>();

		this.entrance = EntranceRoomPool.instance.generate();
		this.exit = EntranceRoomPool.instance.generate();
		((EntranceRoom) this.exit).exit = true;

		rooms.add(this.entrance);
		rooms.add(this.exit);

		if (Dungeon.depth == 0) {
			rooms.add(new LampRoom());
		}

		int regular = this.getNumRegularRooms();
		int special = this.getNumSpecialRooms();
		int connection = this.getNumConnectionRooms();
		int secret = this.getNumSecretRooms();

		Log.info("Creating " + regular + " " + special + " " + connection + " " + secret + " rooms");

		for (int i = 0; i < regular; i++) {
			RegularRoom room;

			do {
				room = RegularRoom.create();
			} while (!room.setSize(0, regular - i));

			i += room.getSize().roomValue - 1;
			rooms.add(room);
		}

		SpecialRoom.init();

		for (int i = 0; i < special; i++) {
			SpecialRoom room = SpecialRoom.create();

			if (room != null) {
				rooms.add(room);
			}
		}

		for (int i = 0; i < connection; i++) {
			rooms.add(ConnectionRoom.create());
		}

		for (int i = 0; i < secret; i++) {
			rooms.add(SecretRoomPool.instance.generate());
		}

		return rooms;
	}

	protected abstract Painter getPainter();

	protected Builder getBuilder() {
		if (Dungeon.depth == 0) {
			return new LineBuilder();
		} else {
			switch (Random.newInt(5)) {
				case 0: case 3: default: return new CastleBuilder();
				case 1: return new LineBuilder();
				case 2: case 4: return new LoopBuilder().setShape(2,
					Random.newFloat(0.4f, 0.7f),
					Random.newFloat(0f, 0.5f)).setPathLength(0.3f, new float[]{1,1,1});
			}
		}
	}

	protected int getNumRegularRooms() {
		return Dungeon.depth == 0 ? 0 : Random.newInt((int) (Dungeon.depth % 5 * 1.4f + 2f), (int) (Dungeon.depth % 5 * 2.5f + 3));
	}

	protected int getNumSpecialRooms() {
		return Dungeon.depth == 0 ? 0 : 3;
	}

	protected int getNumSecretRooms() {
		return Dungeon.depth == 0 ? 0 : Random.newInt(1, 3);
	}

	protected int getNumConnectionRooms() {
		return 0;
	}
}