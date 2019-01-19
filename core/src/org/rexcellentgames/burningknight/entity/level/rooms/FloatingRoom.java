package org.rexcellentgames.burningknight.entity.level.rooms;

import com.badlogic.gdx.math.Vector2;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.entities.Entrance;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.rooms.regular.RegularRoom;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;
import org.rexcellentgames.burningknight.util.geometry.Rect;

public class FloatingRoom extends RegularRoom {
	@Override
	public int getMinWidth() {
		return 64;
	}

	@Override
	public int getMaxWidth() {
		return 65;
	}

	@Override
	public int getMinHeight() {
		return 64;
	}

	@Override
	public int getMaxHeight() {
		return 65;
	}

	private Level level;

	@Override
	public void paint(Level level) {
		this.level = level;
		Painter.fill(level, this, Terrain.WALL);
		newDigger(31, 31);

		Entrance entrance = new Entrance();

		entrance.x = 31 * 16 + 1;
		entrance.y = 31 * 16 - 6;

		LevelSave.add(entrance);
		Dungeon.area.add(entrance);
	}

	private void dig(Vector2 pos) {
		Painter.set(level, new Point(pos.x + left, pos.y + top), Terrain.randomFloor());
	}

	private void dig(Vector2 pos, int size) {
		int x = (int) pos.x;
		int y = (int) pos.y;
		Painter.fill(level, new Rect(x + left - (int) Math.ceil(size / 2f), y + top - (int) Math.ceil(size / 2f), x + left + size / 2, y + top + size / 2), Terrain.randomFloor());
	}

	private float turnChance = 20;
	private float chance2 = 30;
	private float chance3 = 10;
	private int numDiggers;
	private float diggerChance = 5;

	private void newDigger(int x, int y) {
		numDiggers ++;

		Vector2 dir = randomDir();
		Vector2 pos = new Vector2(x, y);

		dig(pos, 3);

		for (int i = 0; i < Random.newInt(64, 128); i++) {
			if (Random.chance(turnChance)) {
				dir = randomDir(dir);
			}

			pos.x += dir.x;
			pos.y += dir.y;

			if (numDiggers < 6 && Random.chance(diggerChance)) {
				newDigger((int) pos.x, (int) pos.y);
			}

			if (Random.chance(chance2)) {
				dig(pos, 2);
			} else if (Random.chance(chance3)) {
				dig(pos, 3);
			} else {
				dig(pos);
			}
		}
	}

	private static Vector2[] dirs = new Vector2[] {
		new Vector2(0, 1), new Vector2(0, -1), new Vector2(1, 0), new Vector2(-1, 0)
	};

	private Vector2 randomDir() {
		return dirs[Random.newInt(4)];
	}

	private Vector2 randomDir(Vector2 last) {
		do {
			Vector2 dir = randomDir();

			if (last != dir) {
				return dir;
			}
		} while (true);
	}
}