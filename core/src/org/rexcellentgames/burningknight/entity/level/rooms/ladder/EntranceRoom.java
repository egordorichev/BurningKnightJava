package org.rexcellentgames.burningknight.entity.level.rooms.ladder;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.entities.Entrance;
import org.rexcellentgames.burningknight.entity.level.entities.Exit;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.creature.npc.Shopkeeper;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.entities.Entrance;
import org.rexcellentgames.burningknight.entity.level.entities.Exit;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class EntranceRoom extends LadderRoom {
	public boolean exit;

	@Override
	public void paint(Level level) {
		super.paint(level);
		this.place(level, this.getCenter());
	}

	@Override
	public boolean canConnect(Room r) {
		return !(r instanceof EntranceRoom) && super.canConnect(r);
	}

	protected void place(Level level, Point point) {
		if (this.exit) {
			Painter.set(level, (int) point.x, (int) point.y, Terrain.FLOOR_B);

			Exit exit = new Exit();

			exit.x = point.x * 16;
			exit.y = point.y * 16 - 8;

			level.set((int) point.x, (int) point.y, Terrain.EXIT);
			LevelSave.add(exit);
			Dungeon.area.add(exit);
		} else {
			Entrance entrance = new Entrance();

			entrance.x = point.x * 16;
			entrance.y = point.y * 16;

		  LevelSave.add(entrance);
			Dungeon.area.add(entrance);
		}
	}
}