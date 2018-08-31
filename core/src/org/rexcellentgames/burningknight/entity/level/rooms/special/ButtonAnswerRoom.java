package org.rexcellentgames.burningknight.entity.level.rooms.special;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;

public class ButtonAnswerRoom extends SpecialRoom {
	public ButtonPuzzleRoom room;

	@Override
	public void paint(Level level) {
		super.paint(level);

		this.room.initData();

		int w = this.room.getWidth() - 2;

		for (int y = 0; y < this.room.getHeight() - 2; y++) {
			for (int x = 0; x < w; x++) {
				byte t = this.room.data[x + y * (w)];

				if (t > 0) {
					Dungeon.level.set(this.left + x, this.top + y, t == 1 ? Terrain.CHASM : Terrain.WALL);
				}
			}
		}

		if (Random.chance(50)) {
			if (Random.chance(50)) {
				Painter.fill(level, this, 4, Random.chance(50) ? Terrain.WALL : Terrain.CHASM);
			} else {
				Painter.fillEllipse(level, this, 4, Random.chance(50) ? Terrain.WALL : Terrain.CHASM);
			}

			if (Random.chance(80)) {
				if (Random.chance(50)) {
					Painter.fill(level, this, 5, Random.chance(30) ? Terrain.WALL : (Random.chance(50) ? Terrain.randomFloor() : Terrain.CHASM));
				} else {
					Painter.fillEllipse(level, this, 5, Random.chance(30) ? Terrain.WALL : (Random.chance(50) ? Terrain.randomFloor() : Terrain.CHASM));
				}
			}
		}
	}

	@Override
	protected int validateWidth(int w) {
		return this.room.validateWidth(w);
	}

	@Override
	protected int validateHeight(int h) {
		return this.room.validateHeight(h);
	}
}