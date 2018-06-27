package org.rexcellentgames.burningknight.entity.level.rooms.special;

import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.MagicWell;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.util.geometry.Point;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.entities.MagicWell;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.util.geometry.Point;

public class WellRoom extends SpecialRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Point center = this.getCenter();
		MagicWell well = new MagicWell();

		well.x = center.x * 16 - 8;
		well.y = center.y * 16 - 11;

		Dungeon.area.add(well);
		LevelSave.add(well);
	}
}