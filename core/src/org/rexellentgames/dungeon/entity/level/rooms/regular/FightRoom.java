package org.rexellentgames.dungeon.entity.level.rooms.regular;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.creature.mob.Clown;
import org.rexellentgames.dungeon.entity.creature.mob.Knight;
import org.rexellentgames.dungeon.entity.creature.mob.Mob;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.features.Door;
import org.rexellentgames.dungeon.entity.level.levels.WaveLevel;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;

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

		int count = (Dungeon.level instanceof WaveLevel ? Dungeon.depth + 1 : Dungeon.depth % 5 + 2);

		for (int i = 0; i < count; i++) {
			Point center = this.getRandomCell();
			Mob mob = Random.chance(60) ? new Knight() : new Clown();

			mob.x = center.x * 16;
			mob.y = center.y * 16;

			mob.generate();

			Dungeon.area.add(mob);
			Dungeon.level.addSaveable(mob);
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