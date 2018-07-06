package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;
import org.rexcellentgames.burningknight.util.geometry.Rect;

public class DoubleCornerRoom extends RegularRoom {
	private enum Type {
		TOP_LEFT,
		BOTTOM_RIGHT
	}

	private Type type;

	public DoubleCornerRoom() {
		this.type = Type.values()[Random.newInt(2)];
	}

	@Override
	public void paint(Level level) {
		super.paint(level);

		if (Random.chance(50)) {
			Painter.fillEllipse(level, this, 2 + Random.newInt(3), Terrain.randomFloor());
		} else {
			Painter.fill(level, this, 2 + Random.newInt(3), Terrain.randomFloor());
		}

		Rect rect = null;
		Rect rect2 = null;

		switch (this.type) {
			case TOP_LEFT:
				rect = new Rect(this.left + 1, this.bottom - this.getHeight() / 2 + 1, this.left + this.getWidth() / 2, this.bottom);
				rect2 = new Rect(this.right - this.getWidth() / 2 + 1, this.top + 1, this.right, this.top + this.getHeight() / 2);

				break;
			case BOTTOM_RIGHT:
				rect = new Rect(this.right - this.getWidth() / 2 + 1, this.bottom - this.getHeight() / 2 + 1, this.right, this.bottom);
				rect2 = new Rect(this.left + 1, this.top + 1, this.left + this.getWidth() / 2, this.top + this.getHeight() / 2);

				break;
		}

		boolean wall = Random.chance(50);

		Painter.fill(level, rect, wall ? Terrain.WALL : Terrain.CHASM);
		Painter.fill(level, rect2, wall ? Terrain.WALL : Terrain.CHASM);

		if (Random.chance(50)) {
			Painter.fill(level, rect, 1, !wall ? Terrain.WALL : Terrain.CHASM);
		}

		if (Random.chance(50)) {
			Painter.fill(level, rect2, 1, !wall ? Terrain.WALL : Terrain.CHASM);
		}

		/*
		if (Random.chance(50)) {
			Painter.fill(level, this, this.getWidth() / 2 - 2, Terrain.CHASM);
		}*/

		int x = this.left + this.getWidth() / 2;
		int y = this.top + this.getHeight() / 2;
		int m = 1; // Random.newInt(1, 4);

		rect = new Rect(x - m, y - m, x + m + 1, y + m + 1);

		Painter.fill(level, rect,  Terrain.randomFloor());

		if (Random.chance(50)) {
			Painter.fill(level, rect, 1, Random.chance(50) ? Terrain.CHASM : Terrain.WALL);
		}
	}

	@Override
	public boolean canConnect(Point p) {
		/*
					case TOP_LEFT:
				rect = new Rect(this.left + 1, this.bottom - this.getHeight() / 2 + 1, this.left + this.getWidth() / 2, this.bottom);
				rect2 = new Rect(this.right - this.getWidth() / 2 + 1, this.top + 1, this.right, this.top + this.getHeight() / 2);

				break;
			case BOTTOM_RIGHT:
				rect = new Rect(this.right - this.getWidth() / 2 + 1, this.bottom - this.getHeight() / 2 + 1, this.right, this.bottom);
				rect2 = new Rect(this.left + 1, this.top + 1, this.left + this.getWidth() / 2, this.top + this.getHeight() / 2);

				break;
		 */

		if ((this.type == Type.TOP_LEFT) && (p.x <= this.left + getWidth() / 2 && p.y >= this.top + this.getHeight() / 2)) {
			return false;
		}

		if ((this.type == Type.TOP_LEFT) && (p.x >= this.left + getWidth() / 2 && p.y <= this.top + getHeight() / 2)) {
			return false;
		}

		if ((this.type == Type.BOTTOM_RIGHT) && (p.x <= this.left + getWidth() / 2 && p.y <= this.top + this.getHeight() / 2)) {
			return false;
		}

		if ((this.type == Type.BOTTOM_RIGHT) && (p.x >= this.left + getWidth() / 2 && p.y >= this.top + getHeight() / 2)) {
			return false;
		}

		return super.canConnect(p);
	}
}