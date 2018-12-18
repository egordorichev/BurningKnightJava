package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;
import org.rexcellentgames.burningknight.util.geometry.Rect;

public class MissingCornerRoom extends RegularRoom {
	private enum Type {
		TOP_LEFT,
		TOP_RIGHT,
		BOTTOM_LEFT,
		BOTTOM_RIGHT
	}

	private Type type;

	public MissingCornerRoom() {
		this.type = Type.values()[Random.newInt(4)];
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

		switch (this.type) {
			case TOP_LEFT:
				rect = new Rect(this.left + 1, this.top + 1, this.left + this.getWidth() / 2, this.top + this.getHeight() / 2);
				break;
			case TOP_RIGHT:
				rect = new Rect(this.right - this.getWidth() / 2 + 1, this.top + 1, this.right, this.top + this.getHeight() / 2);
				break;
			case BOTTOM_LEFT:
				rect = new Rect(this.left + 1, this.bottom - this.getHeight() / 2 + 1, this.left + this.getWidth() / 2, this.bottom);
				break;
			case BOTTOM_RIGHT:
				rect = new Rect(this.right - this.getWidth() / 2 + 1, this.bottom - this.getHeight() / 2 + 1, this.right, this.bottom);
				break;
		}

		boolean wall = Random.chance(50);

		Painter.fill(level, rect, wall ? Terrain.WALL : Terrain.CHASM);

		if (Random.chance(50)) {
			Painter.fill(level, rect, 2 + Random.newInt(3), !wall ? Terrain.WALL : Terrain.CHASM);
		}
	}

	@Override
	public boolean canConnect(Point p) {
		if ((this.type == Type.TOP_LEFT || this.type == Type.TOP_RIGHT) && p.y >= this.top + this.getHeight() / 2) {
			return false;
		}

		if ((this.type == Type.BOTTOM_LEFT || this.type == Type.BOTTOM_RIGHT) && p.y <= this.top + getHeight() / 2) {
			return false;
		}

		if ((this.type == Type.BOTTOM_LEFT || this.type == Type.TOP_LEFT) && p.x <= this.left + getWidth() / 2) {
			return false;
		}

		if ((this.type == Type.BOTTOM_RIGHT || this.type == Type.TOP_RIGHT) && p.x >= this.left + getWidth() / 2) {
			return false;
		}

		return super.canConnect(p);
	}
}