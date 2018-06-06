package org.rexellentgames.dungeon.entity.level.rooms.special;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.entities.WeaponAltar;
import org.rexellentgames.dungeon.entity.level.save.LevelSave;
import org.rexellentgames.dungeon.util.geometry.Point;

public class WeaponAltarRoom extends SpecialRoom {
	@Override
	public void paint(Level level) {
		super.paint(level);

		Point center = this.getCenter();
		WeaponAltar altar = new WeaponAltar();

		altar.x = center.x * 16 - 8;
		altar.y = center.y * 16;

		Dungeon.area.add(altar);
		LevelSave.add(altar);
	}
}