package org.rexcellentgames.burningknight.entity.level.rooms.boss;

import org.rexcellentgames.burningknight.util.geometry.Point;

public class CircleBossRoom extends BossRoom {
	{
		alwaysElipse = true;
	}

	@Override
	protected Point getDoorCenter() {
		return getCenter();
	}
}