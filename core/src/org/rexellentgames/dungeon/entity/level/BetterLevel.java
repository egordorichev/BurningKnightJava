package org.rexellentgames.dungeon.entity.level;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.creature.mob.BurningKnight;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.ChangableRegistry;
import org.rexellentgames.dungeon.entity.level.builders.Builder;
import org.rexellentgames.dungeon.entity.level.builders.RegularBuilder;
import org.rexellentgames.dungeon.entity.level.levels.WaveLevel;
import org.rexellentgames.dungeon.entity.level.painters.Painter;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.entity.level.rooms.connection.ConnectionRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.*;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.CastleEntranceRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.CastleExitRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.EntranceRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.ExitRoom;
import org.rexellentgames.dungeon.entity.level.rooms.special.HealthBlockRoom;
import org.rexellentgames.dungeon.entity.level.rooms.special.SpecialRoom;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.util.ArrayList;
import java.util.Collections;

public abstract class BetterLevel extends Level {

	@Override
	public void generate() {
		Level.GENERATED = true;

		this.build();
		this.paint();

		ChangableRegistry.generate();

		this.loadPassable();

		this.spawnLevelEntities();
		this.spawnEntities();

		Log.info("Done!");
	}

	protected void spawnLevelEntities() {
		this.free = new boolean[this.getSIZE()];

		Log.info("Adding items...");

		this.spawnItems();
		this.spawnCreatures();
	}

	protected void spawnEntities() {
		Log.info("Adding entities...");

		if (Player.instance == null && !Network.SERVER) {
			Player player = new Player();
			Dungeon.area.add(player);
			this.addPlayerSaveable(player);
			player.generate();

			if (this.entrance != null) {
				Point point;

				if (this.entrance instanceof CastleEntranceRoom) {
					point = ((CastleEntranceRoom) this.entrance).spawn;
				} else {
					point = this.entrance.getRandomCell();
				}

				Log.info("Setting player spawn to " + (int) point.x + ":" + (int) point.y + "...");
				player.tp(point.x * 16, point.y * 16);
			}
		}
	}

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
		ArrayList<Room> rooms = new ArrayList<Room>();

		this.entrance = new EntranceRoom();
		this.exit = new ExitRoom();

		rooms.add(this.entrance);
		rooms.add(this.exit);

		if (Dungeon.depth > 0) {
			rooms.add(new HealthBlockRoom());
		}

		if (Dungeon.depth == 0) {
			rooms.add(new LampRoom());
		}

		int regular = this.getNumRegularRooms();
		int special = this.getNumSpecialRooms();
		int connection = this.getNumConnectionRooms();

		for (int i = 0; i < regular; i++) {
			RegularRoom room;

			do {
				room = RegularRoom.create();
			} while (!room.setSize(0, regular - i));

			i += room.getSize().roomValue - 1;
			rooms.add(room);
		}

		// todo: shop

		for (int i = 0; i < special; i++) {
			rooms.add(SpecialRoom.create());
		}

		for (int i = 0; i < connection; i++) {
			rooms.add(ConnectionRoom.create());
		}

		// todo: secret

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
		return 0;
	}

	protected int getNumConnectionRooms() {
		return 0;
	}
}