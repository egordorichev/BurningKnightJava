package org.rexcellentgames.burningknight.entity.level.rooms.special;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.AnswerLever;
import org.rexcellentgames.burningknight.util.Random;

public class LeverPuzzleRoom extends SpecialRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		for (int x = 2; x < this.getWidth() - 2; x += 2) {
			AnswerLever lever = new AnswerLever();

			lever.shouldBeOn = Random.chance(50);
			lever.on = Random.chance(50);
			lever.x = (this.left + x) * 16;
			lever.y = (this.top + this.getHeight() / 2) * 16 - 8;

			Dungeon.area.add(lever.add());
		}
	}

	@Override
	public int getMaxWidth() {
		return 10;
	}

	@Override
	public int getMinWidth() {
		return 7;
	}
}