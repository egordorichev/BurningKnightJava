package org.rexellentgames.dungeon.entity.level.rooms.special;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.entities.MagicWell;
import org.rexellentgames.dungeon.util.geometry.Point;

public class WellRoom extends SpecialRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Point center = this.getCenter();
		MagicWell well = new MagicWell();

		well.x = center.x * 16 - 8;
		well.y = center.y * 16 - 11;

		Dungeon.area.add(well);
		Dungeon.level.addSaveable(well);
	}
}