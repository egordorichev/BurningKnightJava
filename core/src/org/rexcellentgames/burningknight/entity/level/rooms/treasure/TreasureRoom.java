package org.rexcellentgames.burningknight.entity.level.rooms.treasure;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Chest;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Mimic;
import org.rexcellentgames.burningknight.entity.level.rooms.special.LockedRoom;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class TreasureRoom extends LockedRoom {
	public boolean weapon;

	protected void placeChest(Point center) {
		if (Random.chance(Mimic.chance)) {
			Mimic chest = new Mimic();

			chest.x = center.x * 16;
			chest.y = center.y * 16;
			chest.weapon = weapon;

			Dungeon.area.add(chest);
			LevelSave.add(chest);
		} else {
			Chest chest = Chest.random();

			chest.x = center.x * 16;
			chest.y = center.y * 16;
			chest.weapon = weapon;
			chest.setItem(chest.generate());

			Dungeon.area.add(chest);
			LevelSave.add(chest);
		}
	}
}