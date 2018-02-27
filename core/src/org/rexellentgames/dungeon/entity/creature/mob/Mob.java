package org.rexellentgames.dungeon.entity.creature.mob;

import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.RegularLevel;
import org.rexellentgames.dungeon.util.PathFinder;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.util.ArrayList;

public class Mob extends Creature {
	protected Creature target;
	protected float knockback = 32f;
	protected ArrayList<Player> colliding = new ArrayList<Player>();

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

	@Override
	public void update(float dt) {
		super.update(dt);

		for (Player player : this.colliding) {
			player.modifyHp(-this.damage);
		}
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Player) {
			Player player = (Player) entity;

			if (player.isDead()) {
				return;
			}

			this.colliding.add(player);
		}
	}

	@Override
	public void onCollisionEnd(Entity entity) {
		if (entity instanceof Player) {
			Player player = (Player) entity;

			if (player.isDead()) {
				return;
			}

			this.colliding.remove(player);
		}
	}
}