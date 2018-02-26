package org.rexellentgames.dungeon.entity.creature.mob;

import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.RegularLevel;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.PathFinder;
import org.rexellentgames.dungeon.util.geometry.Point;

public class Mob extends Creature {
	protected Creature target;

	protected void assignTarget() {
		this.target = (Creature) this.area.getRandomEntity(Player.class);
	}

	public Point getCloser(Creature target) {
		int from = (int) (Math.floor(this.x / 16) + Math.floor(this.y / 16) * Level.WIDTH);
		int to = (int) (Math.floor((target.x) / 16) + Math.floor((target.y ) / 16) * Level.WIDTH);

		int step = PathFinder.getStep(from, to, RegularLevel.instance.getPassable());

		if (step != -1) {
			Point p = new Point();

			p.x = step % Level.WIDTH * 16;
			p.y = (float) (Math.floor(step / Level.WIDTH) * 16);

			return p;
		}

		return null;
	}
}