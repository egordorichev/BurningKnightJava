package org.rexcellentgames.burningknight.entity.level.rooms.secret;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.connection.ConnectionRoom;
import org.rexcellentgames.burningknight.entity.level.save.GameSave;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.entity.pool.MobHub;
import org.rexcellentgames.burningknight.entity.pool.MobPool;
import org.rexcellentgames.burningknight.entity.pool.room.SecretRoomPool;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class SecretRoom extends Room {
	@Override
	public void paint(Level level) {
		hidden = true;

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.FLOOR_D);

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.SECRET);
		}
	}

	private float spawnMob(Mob mob, Room room, float weight) {
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

		return weight;
	}

	protected void addEnemies() {
		if (Random.chance(30) && GameSave.runId > 10) {
			MobPool.instance.initForFloor();

			Log.info("Spawn modifier is x" + Player.mobSpawnModifier);
			float weight = ((Random.newFloat(1f, 3f) + this.getWidth() * this.getHeight() / 128) * Player.mobSpawnModifier);

			while (weight > 0) {
				MobHub mobs = MobPool.instance.generate();

				for (Class<? extends Mob> mob : mobs.types) {
					try {
						weight = spawnMob(mob.newInstance(), this, weight);
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@Override
	public int getMinWidth() {
		return 8;
	}

	public int getMaxWidth() {
		return 12;
	}

	@Override
	public int getMinHeight() {
		return 8;
	}

	public int getMaxHeight() {
		return 12;
	}


	@Override
	public int getMaxConnections(Connection side) {
		return 1;
	}

	@Override
	public int getMinConnections(Connection side) {
		if (side == Connection.ALL) {
			return 1;
		}

		return 0;
	}

	public static SecretRoom create() {
		return SecretRoomPool.instance.generate();
	}

	@Override
	public boolean canConnect(Room r) {
		return !(r instanceof ConnectionRoom) && super.canConnect(r);
	}
}