package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class CenterStructRoom extends RegularRoom {
	@Override
	public void paint(Level level) {
		for (Door door : this.connected.values()) {
			door.setType(Door.Type.REGULAR);
		}

		boolean el = Random.chance(50);

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.CHASM);

		int m = Random.newInt(2, 4);

		Painter.fill(level, this, m, Terrain.randomFloor());

		m += Random.newInt(1, 3);

		boolean before = Random.chance(50);

		if (before) {
			this.paintTunnel(level, Terrain.randomFloor());
		}

		Painter.fill(level, this, m, getSolid());

		byte f = Terrain.randomFloor();
		boolean s = false;

		if (Random.chance(50)) {
			Painter.set(level, new Point(this.getWidth() / 2 + this.left, this.top + m), f);
			s = true;
		}

		if (Random.chance(50)) {
			Painter.set(level, new Point(this.getWidth() / 2 + this.left, this.bottom - m), f);
			s = true;
		}

		if (Random.chance(50)) {
			Painter.set(level, new Point(this.left + m, this.getHeight() / 2 + this.top), f);
			s = true;
		}

		if (Random.chance(50) || !s) {
			Painter.set(level, new Point(this.right - m, this.getHeight() / 2 + this.top), f);
		}

		el = el || Random.chance(50);

		if (el) {
			m ++;
			Painter.fillEllipse(level, this, m, f);
		} else {
			Painter.fill(level, this, m, f);
		}

		if (Random.chance(50)) {
			m += 1f;

			if (el) {
				m += 1f;
				Painter.fillEllipse(level, this, m, getSolid());
			} else {
				Painter.fill(level, this, m, getSolid());
			}
		}

		if (!before) {
			this.paintTunnel(level, Terrain.randomFloor());
		}
	}

	@Override
	protected Point getDoorCenter() {
		return getCenter();
	}

	private byte getSolid() {
		return Random.chance(50) ? Terrain.CHASM : Terrain.WALL;
	}

	@Override
	public int getMinWidth() {
		return 12;
	}

	@Override
	public int getMinHeight() {
		return 12;
	}
}