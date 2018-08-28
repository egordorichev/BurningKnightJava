package org.rexcellentgames.burningknight.entity.level.rooms.item;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;
import org.rexcellentgames.burningknight.util.geometry.Rect;

public class BrokeLineItemRoom extends ItemRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		byte f = Terrain.randomFloor();
		byte fl = Random.chance(50) ? Terrain.WALL : Terrain.CHASM;

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.randomFloor());
		Painter.fill(level, this, 2, fl);

		boolean el = Random.chance(50);

		if (el) {
			Painter.fillEllipse(level, this, 3, f);
		} else {
			Painter.fill(level, this, 3, f);
		}

		boolean s = false;
		boolean all = true; // Random.chance(50);

		if (all || Random.chance(50)) {
			Painter.set(level, new Point(this.getWidth() / 2 + this.left, this.top + 2), f);
			s = true;
		}

		if (all || Random.chance(50)) {
			Painter.set(level, new Point(this.getWidth() / 2 + this.left, this.bottom - 2), f);
			s = true;
		}

		if (all || Random.chance(50)) {
			Painter.set(level, new Point(this.left + 2, this.getHeight() / 2 + this.top), f);
			s = true;
		}

		if (all || Random.chance(50) || !s) {
			Painter.set(level, new Point(this.right - 2, this.getHeight() / 2 + this.top), f);
		}

		if (Random.chance(50)) {
			byte flr = Terrain.randomFloor();

			if (Random.chance(50)) {
				Painter.fill(level, new Rect(this.left + 1, this.top + 1, this.left + 4, this.top + 4), flr);
				Painter.fill(level, new Rect(this.right - 3, this.top + 1, this.right, this.top + 4), flr);
				Painter.fill(level, new Rect(this.left + 1, this.bottom - 3, this.left + 4, this.bottom), flr);
				Painter.fill(level, new Rect(this.right - 3, this.bottom - 3, this.right, this.bottom), flr);
			} else {
				Painter.fillEllipse(level, new Rect(this.left + 1, this.top + 1, this.left + 4, this.top + 4), flr);
				Painter.fillEllipse(level, new Rect(this.right - 3, this.top + 1, this.right, this.top + 4), flr);
				Painter.fillEllipse(level, new Rect(this.left + 1, this.bottom - 3, this.left + 4, this.bottom), flr);
				Painter.fillEllipse(level, new Rect(this.right - 3, this.bottom - 3, this.right, this.bottom), flr);
			}
		}

		placeItem(getCenter());
	}
}