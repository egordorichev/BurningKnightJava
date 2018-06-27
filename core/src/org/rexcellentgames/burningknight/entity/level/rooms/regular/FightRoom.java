package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.creature.mob.hall.Knight;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.util.geometry.Point;

//
// BROKEN
//

public class FightRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		for (Door door : this.connected.values()) {
			door.setType(Door.Type.ENEMY);
		}

		if (Dungeon.type == Dungeon.Type.INTRO) {
			Point center = this.getRandomCell();
			Mob mob = new Knight();

			mob.x = center.x * 16;
			mob.y = center.y * 16;

			mob.generate();

			Dungeon.area.add(mob);
			LevelSave.add(mob);

			mob.modifyHp(-4, null);
		}
	}

	protected float[] getSizeChance() {
		return new float[]{0,0,1};
	}

	@Override
	public int getMinWidth() {
		return 10;
	}

	@Override
	public int getMinHeight() {
		return 10;
	}

	@Override
	public int getMaxWidth() {
		return 16;
	}

	@Override
	public int getMaxHeight() {
		return 16;
	}
}