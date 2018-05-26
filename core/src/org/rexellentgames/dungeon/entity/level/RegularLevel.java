package org.rexellentgames.dungeon.entity.level;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.creature.mob.BurningKnight;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.ChangableRegistry;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.level.builders.Builder;
import org.rexellentgames.dungeon.entity.level.builders.RegularBuilder;
import org.rexellentgames.dungeon.entity.level.entities.Entrance;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.entity.level.rooms.connection.ConnectionRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.*;
import org.rexellentgames.dungeon.entity.level.rooms.ladder.EntranceRoom;
import org.rexellentgames.dungeon.entity.level.rooms.special.SpecialRoom;
import org.rexellentgames.dungeon.entity.pool.MobPool;
import org.rexellentgames.dungeon.entity.pool.room.EntranceRoomPool;
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

		this.build();
		this.paint();

		ChangableRegistry.generate();

		this.loadPassable();

		this.spawnLevelEntities();
		this.spawnEntities();

		if (Dungeon.type == Dungeon.Type.REGULAR && BurningKnight.instance == null && Dungeon.depth > 0) {
			BurningKnight knight = new BurningKnight();

			Dungeon.area.add(knight);
			Dungeon.level.addPlayerSaveable(knight);

			knight.findStartPoint();
		}

		Log.info("Done!");
	}

	protected void spawnLevelEntities() {
		this.free = new boolean[this.getSIZE()];

		if (Dungeon.depth > 0) {
			MobPool.instance.initForFloor();

			for (Room room : this.rooms) {
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
						Dungeon.level.addSaveable(mob);


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

			this.addSaveable(holder);
			this.area.add(holder);
		}

		this.itemsToSpawn.clear();
	}

	protected void spawnEntities() {
		Log.info("Adding entities...");

		if (Player.instance == null) {
			Player player = new Player();
			Dungeon.area.add(player);
			this.addPlayerSaveable(player);
			player.generate();

			if (this.ladder != null) {
				player.tp(ladder.x, ladder.y - 2);
			} else {
				Log.error("No entrance found!");
			}
		}
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

		Log.info("Creating " + regular + " " + special + " " + connection + " rooms");

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

		return rooms;
	}

	protected Builder getBuilder() {
		return new RegularBuilder();
	}

	protected abstract Painter getPainter();

	protected int getNumRegularRooms() {
		return 8;
	}

	protected int getNumSpecialRooms() {
		return Dungeon.depth == 0 ? 0 : 3;
	}

	protected int getNumConnectionRooms() {
		return 2;
	}
}