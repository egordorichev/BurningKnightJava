package org.rexcellentgames.burningknight.entity.level.rooms.regular;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.features.Door;
import org.rexcellentgames.burningknight.entity.level.painters.Painter;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.entity.trap.RollingSpike;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.geometry.Point;
import org.rexcellentgames.burningknight.util.geometry.Rect;

public class VerticalSpikeTrapRoom extends TrapRoom {
	@Override
	public void paint(Level level) {
		byte f = Terrain.randomFloor();

		Painter.fill(level, this, Terrain.WALL);

		if (Random.chance(50)) {
			Painter.fill(level, this, 1, Terrain.CHASM);
		}

		Painter.fill(level, new Rect(this.left + 1, this.top + 1, this.right, this.top + 4), f);
		Painter.fill(level, new Rect(this.left + 1, this.bottom - 3, this.right, this.bottom), f);

		int y = this.left + Random.newInt(1, this.getWidth() - 1);

		Painter.drawLine(level, new Point(y, this.top + 1),
			new Point(y, this.bottom - 1), f);

		RollingSpike spike = new RollingSpike();

		spike.x = y * 16 + 1;
		spike.y = (this.top + 1) * 16 + 1;
		spike.velocity = new Point(0f, 20f);

		Dungeon.area.add(spike);
		LevelSave.add(spike);

		for (Door door : connected.values()) {
			door.setType(Door.Type.REGULAR);
		}
	}

	@Override
	public boolean canConnect(Point p) {
		if (p.y != this.top && p.y != this.bottom) {
			return false;
		}

		return super.canConnect(p);
	}

	@Override
	public int getMinHeight() {
		return 10;
	}
}