package org.rexcellentgames.burningknight.entity;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.connection.ConnectionRoom;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class Teleporter extends Item {
	{
		useTime = 30f;
	}

	@Override
	public void use() {
		super.use();

		for (int i = 0; i < 64; i++) {
			Room room = Dungeon.level.getRandomRoom();

			if (room instanceof ConnectionRoom) {
				continue;
			}

			for (int j = 0; j < 32; j++) {
				Point point = room.getRandomFreeCell();

				if (point != null) {
					Player.instance.poof();
					Player.instance.tp(point.x * 16, point.y * 16);
					Player.instance.poof();
					Player.instance.playSfx("grenade_launcher");

					return;
				}
			}
		}
	}
}