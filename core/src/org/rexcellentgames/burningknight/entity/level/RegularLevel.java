package org.rexcellentgames.burningknight.entity.level;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Bomb;
import org.rexcellentgames.burningknight.entity.item.ChangableRegistry;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.consumable.scroll.ScrollOfUpgrade;
import org.rexcellentgames.burningknight.entity.level.builders.*;
import org.rexcellentgames.burningknight.entity.level.entities.AnswerButton;
import org.rexcellentgames.burningknight.entity.level.entities.Coin;
import org.rexcellentgames.burningknight.entity.level.entities.Entrance;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Chest;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Mimic;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.rooms.HandmadeRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.connection.ConnectionRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.entrance.BossEntranceRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.entrance.EntranceRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.regular.LampRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.regular.RegularRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.special.*;
import org.rexcellentgames.burningknight.entity.level.rooms.treasure.TreasureRoom;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.entity.level.save.PlayerSave;
import org.rexcellentgames.burningknight.entity.pool.MobPool;
import org.rexcellentgames.burningknight.entity.pool.room.*;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.ArrayList;
import java.util.Collections;

public abstract class RegularLevel extends Level {
	public static Entrance ladder;
	protected boolean isBoss;

	public RegularLevel setBoss(boolean boss) {
		isBoss = boss;
		return this;
	}

	@Override
	public void generate() {
		Player.all.clear();
		Mob.all.clear();
		ItemHolder.getAll().clear();
		Chest.all.clear();
		Mimic.all.clear();

		PlayerSave.all.clear();
		LevelSave.all.clear();

		Dungeon.area.destroy();
		Dungeon.area.add(Dungeon.level);

		Level.GENERATED = true;

		this.itemsToSpawn.clear();

		if (Dungeon.depth > 0) {
			for (int i = 0; i < Random.newInt(4); i++) {
				this.itemsToSpawn.add(new Bomb());
			}
		}

		this.build();
		this.paint();

		if (this.rooms == null) {
			Log.error("NO ROOMS!");
		}

		Log.info("Done painting");

		ChangableRegistry.generate();

		this.loadPassable();

		Log.info("Spawning entities...");

		this.spawnLevelEntities();
		this.spawnEntities();

		Log.info("Done!");
	}

	protected void spawnLevelEntities() {
		this.free = new boolean[getSize()];

		if (Dungeon.depth > 0) {
			MobPool.instance.initForFloor();

			Log.info("Spawn modifier is x" + Player.mobSpawnModifier);

			for (Room room : this.rooms) {
				if (room instanceof RegularRoom && !(room instanceof BossEntranceRoom) || (room instanceof TreasureRoom && Random.chance(20))) {
					float weight = ((Random.newFloat(1f, 3f) + room.getWidth() * room.getHeight() / 64) * Player.mobSpawnModifier);

					while (weight > 0) {
						Mob mob = MobPool.instance.generate();

						weight -= mob.getWeight();

						Point point;
						int i = 0;

						do {
							point = room.getRandomCell();

							if (i++ > 40) {
								Log.error("Failed to place " + mob.getClass() + " in room " + room.getClass());
								break;
							}
						} while (!Dungeon.level.checkFor((int) point.x, (int) point.y, Terrain.PASSABLE));

						if (i <= 40) {
							mob.generate();

							Dungeon.area.add(mob);
							LevelSave.add(mob);

							mob.tp(point.x * 16, point.y * 16);
						}
					}
				}
			}
		}

		for (Item item : this.itemsToSpawn) {
			Point point = null;

			while (point == null) {
				point = this.getRandomFreePoint(RegularRoom.class);
			}

			ItemHolder holder = new ItemHolder(item);

			holder.getItem().generate();
			holder.x = point.x * 16 + Random.newInt(-4, 4);
			holder.y = point.y * 16 + Random.newInt(-4, 4);

			holder.add();
			this.area.add(holder);
		}

		this.itemsToSpawn.clear();
	}

	protected void spawnEntities() {

	}

	protected void paint() {
		Log.info("Painting...");

		if (Dungeon.depth > 0) {
			if (Random.chance(75)) {
				this.itemsToSpawn.add(new Coin());

				while (Random.chance(25)) {
					this.itemsToSpawn.add(new Coin());
				}
			}
		}

		Painter painter = this.getPainter();

		if (painter != null) {
			painter.paint(this, this.rooms);
		} else {
			Log.error("No painter!");
		}

		for (int i = this.rooms.size() - 1; i >= 0; i--) {
			Room room = this.rooms.get(i);

			if (room instanceof HandmadeRoom && ((HandmadeRoom) room).data.sub.size() > 0) {
				this.rooms.remove(i);
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void build() {
		Builder builder = this.getBuilder();

		ArrayList<Room> rooms = this.createRooms();

		if (Dungeon.depth != -2) {
			Collections.shuffle(rooms);
		}

		do {
			Log.info("Generating...");

			for (Room room : rooms) {
				room.getConnected().clear();
				room.getNeighbours().clear();
			}

			this.rooms = builder.build((ArrayList<Room>) rooms.clone());

			if (this.rooms == null) {
				Log.error("Failed!");

				Player.all.clear();
				Mob.all.clear();
				ItemHolder.getAll().clear();
				Chest.all.clear();
				Mimic.all.clear();
				AnswerButton.all.clear();

				PlayerSave.all.clear();
				LevelSave.all.clear();

				Dungeon.area.destroy();
				Dungeon.area.add(Dungeon.level);

				Level.GENERATED = true;

				this.itemsToSpawn.clear();
			}
		} while (this.rooms == null);

		if (Dungeon.depth > 0) {
			itemsToSpawn.add(new ScrollOfUpgrade());
		}
	}

	protected ArrayList<Room> createRooms() {
		ArrayList<Room> rooms = new ArrayList<>();

		if (Dungeon.depth > -1) {
			this.entrance = EntranceRoomPool.instance.generate();
			this.exit = BossRoomPool.instance.generate(); // : EntranceRoomPool.instance.generate();
			((EntranceRoom) this.exit).exit = true;
			rooms.add(new BossEntranceRoom());

			rooms.add(this.entrance);
			rooms.add(this.exit);
		}

		if (Dungeon.depth == 0) {
			rooms.add(new LampRoom());
		} else if (Dungeon.depth == -2) {
			rooms.add(new HandmadeRoom("shops"));
		} else if (Dungeon.depth == -1) {
			rooms.add(new HandmadeRoom("hub"));
		} else if (Random.chance(50)) {
			rooms.add(ItemRoomPool.instance.generate());
		}

		if (Dungeon.depth > 0) {
			if (GlobalSave.isFalse("all_npcs_saved") && (Random.chance(25))) {
				rooms.add(new NpcSaveRoom());
			}
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

				if (room instanceof ButtonPuzzleRoom) {
					ButtonAnswerRoom r = new ButtonAnswerRoom();
					r.room = (ButtonPuzzleRoom) room;

					rooms.add(r);
				} else if (room instanceof LeverPuzzleRoom) {
					LeverAnswerRoom r = new LeverAnswerRoom();
					rooms.add(r);
				}
			}
		}

		if (Dungeon.depth > 0) {
			TreasureRoom room = TreasureRoomPool.instance.generate();
			room.weapon = true;

			rooms.add(room);
			rooms.add(TreasureRoomPool.instance.generate());

			if (Random.chance(75)) {
				Log.info("Adding shop");
				rooms.add(ShopRoomPool.instance.generate());
			}
		}

		for (int i = 0; i < connection; i++) {
			rooms.add(ConnectionRoom.create());
		}

		for (int i = 0; i < secret; i++) {
			rooms.add(SecretRoomPool.instance.generate());
		}

		ArrayList<HandmadeRoom> handmadeRooms = new ArrayList<>();

		for (Room room : rooms) {
			if (room instanceof HandmadeRoom && ((HandmadeRoom) room).data.sub.size() > 0) {
				handmadeRooms.add((HandmadeRoom) room);
			}
		}

		for (HandmadeRoom room : handmadeRooms) {
			room.addSubRooms(rooms);
		}

		return rooms;
	}

	protected abstract Painter getPainter();

	protected Builder getBuilder() {
		if (Dungeon.depth <= -1) {
			return new SingleRoomBuilder();
		} else if (Dungeon.depth == 0) {
			return new LineBuilder();
		} else {
			/*switch (Random.newInt(4)) {
				case 0:
				case 1: return new CastleBuilder();
				case 2:
				case 3: default:*/
					return new LoopBuilder().setShape(2,
						Random.newFloat(0.4f, 0.7f),
						Random.newFloat(0f, 0.5f)).setPathLength(Random.newFloat(0.3f, 0.8f), new float[]{1, 1, 1});
			// }
		}
	}

	protected int getNumRegularRooms() {
		return Dungeon.depth <= 0 ? 0 : Random.newInt(6, 8);
	}

	protected int getNumSpecialRooms() {
		return 0; // Dungeon.depth <= 0 ? 0 : Random.newInt(1, 4);
	}

	protected int getNumSecretRooms() {
		return Dungeon.depth <= 0 ? 0 : Random.newInt(1, 3);
	}

	protected int getNumConnectionRooms() {
		return 0;
	}
}