package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class TriangleRoom extends RegularRoom {
	enum Type {
		TOP_RIGHT,
		TOP_LEFT,
		BOTTOM_RIGHT,
		BOTTOM_LEFT
	}

	private Type type;

	public TriangleRoom() {
		this.type = Type.TOP_LEFT; // Type.values()[Random.newInt(4)];
	}

	@Override
	public void paint(Level level) {
		super.paint(level);

		byte f = Terrain.WALL;

		if (Random.chance(30)) {
			f = Terrain.CHASM;
		} else if (Random.chance(25)) {
			f = Terrain.LAVA;
		}

		if (this.type == Type.BOTTOM_LEFT || this.type == Type.TOP_LEFT) {
			for (int y = this.top + 1; y < this.bottom - 1; y++) {
				Painter.drawLine(level, new Point(this.type == Type.TOP_LEFT ? this.left + 1 : this.right - 1, y),
					new Point(this.type == Type.BOTTOM_LEFT ? this.left + 2 : this.right - 2, this.top + 1), f);
			}
		} else {
			for (int x = this.left + 1; x < this.right - 1; x++) {
				Painter.drawLine(level, new Point(x, this.type == Type.BOTTOM_RIGHT ? this.top + 1 : this.bottom - 1),
					new Point(this.left + 1, this.type == Type.BOTTOM_RIGHT ? this.bottom - 2 : this.top + 2), f);
			}
		}
	}

	@Override
	public boolean canConnect(Point p) {
		if ((this.type == Type.TOP_RIGHT || this.type == Type.TOP_LEFT) && p.y >= this.bottom) {
			return false;
		}

		if ((this.type == Type.BOTTOM_RIGHT || this.type == Type.BOTTOM_LEFT) && p.y <= this.top) {
			return false;
		}

		if ((this.type == Type.TOP_RIGHT || this.type == Type.BOTTOM_RIGHT) && p.x == this.left) {
			return false;
		}

		if ((this.type == Type.TOP_LEFT || this.type == Type.BOTTOM_LEFT) && p.x == this.right) {
			return false;
		}

		return super.canConnect(p);
	}
}