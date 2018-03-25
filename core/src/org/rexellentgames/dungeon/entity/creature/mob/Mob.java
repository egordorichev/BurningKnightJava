package org.rexellentgames.dungeon.entity.creature.mob;

import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.ui.ExpFx;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.util.Line;
import org.rexellentgames.dungeon.util.PathFinder;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.util.ArrayList;

public class Mob extends Creature {
	protected Creature target;
	protected ArrayList<Player> colliding = new ArrayList<Player>();
	protected boolean drop;
	protected boolean noticed = false;
	protected int experienceDropped = 1;

	public Mob() {
		if (Dungeon.level != null) {
			Level.heat += 1;
		}
	}

	public void notice(Player player) {
		this.noticed = true;
		this.target = player;
	}

	public int getExperienceDropped() {
		return this.experienceDropped;
	}

	protected boolean canSee(Player player) {
		return this.getDistanceTo(player.x + 8, player.y + 8) < 256f && Dungeon.level.canSee(
			(int) Math.floor((this.x + this.w / 2) / 16), (int) Math.floor((this.y + this.h / 2) / 16),
			(int) Math.floor((player.x + player.w / 2) / 16), (int) Math.floor((player.y + player.h / 2) / 16)
		);
	}

	protected void assignTarget() {
		this.target = (Creature) this.area.getRandomEntity(Player.class);

		if (this.target != null && this.target.invisible) {
			this.target = null;
		}
	}

	public Point getCloser(Point target) {
		int from = (int) (Math.floor((this.x + 8) / 16) + Math.floor((this.y + 8) / 16) * Level.getWidth());
		int to = (int) (Math.floor((target.x + 8) / 16) + Math.floor((target.y + 8) / 16) * Level.getWidth());

		int step = PathFinder.getStep(from, to, Dungeon.level.getPassable());

		if (step != -1) {
			Point p = new Point();

			p.x = step % Level.getWidth() * 16;
			p.y = (float) (Math.floor(step / Level.getWidth()) * 16);

			return p;
		}

		return null;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.drop) {
			this.drop = false;
			ArrayList<Item> items = this.getDrops();

			for (Item item : items) {
				ItemHolder holder = new ItemHolder();

				holder.setItem(item);
				holder.x = this.x;
				holder.y = this.y;

				this.area.add(holder);
			}
		}

		if (this.dead) {
			return;
		}

		if (!this.noticed && this.t % 0.25f <= 0.017f && Player.instance != null && !Player.instance.invisible && Dungeon.level != null) {
			Player player = Player.instance;

			Line line = new Line((int) Math.floor((this.x + 8) / 16), (int) Math.floor((this.y + 8) / 16),
				(int) Math.floor((player.x + 8) / 16), (int) Math.floor((player.y + 8) / 16));

			boolean[] passable = Dungeon.level.getPassable();
			boolean found = false;

			for (Point point : line.getPoints()) {
				int i = (int) (point.x + point.y * Level.getWidth());
				if (i < 0 || i >= Level.getSIZE() || (!passable[i] && Dungeon.level.get(i) != 13)) {
					found = true;
					break;
				}
			}

			if (!found) {
				this.notice(Player.instance);
			}
		}

		if (this.target != null && this.target.invisible) {
			this.target = null;
		}

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

			if (!this.noticed) {
				this.notice(player);
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

	protected ArrayList<Item> getDrops() {
		return new ArrayList<Item>();
	}

	@Override
	protected void die() {
		if (!this.dead) {
			this.drop = true;
		}

		if (!Player.instance.isDead()) {
			for (int i = 0; i < this.experienceDropped; i++) {
				Dungeon.ui.add(new ExpFx(this.x + this.w / 2, this.y + this.w / 2));
			}
		}

		super.die();
	}

	protected float moveToPoint(float x, float y, float speed) {
		float dx = x - this.x - this.w / 2;
		float dy = y - this.y - this.h / 2;
		float d = (float) Math.sqrt(dx * dx + dy * dy);

		this.vel.x += dx / d * speed;
		this.vel.y += dy / d * speed;

		return d;
	}

	protected float getDistanceTo(float x, float y) {
		float dx = x - this.x - this.w / 2;
		float dy = y - this.y - this.h / 2;
		return (float) Math.sqrt(dx * dx + dy * dy);
	}
}