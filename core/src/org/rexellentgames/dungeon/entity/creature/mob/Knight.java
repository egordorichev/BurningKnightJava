package org.rexellentgames.dungeon.entity.creature.mob;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.weapon.IronSword;
import org.rexellentgames.dungeon.entity.item.weapon.Sword;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.util.ArrayList;

public class Knight extends Mob {
	private static Animation idle = Animation.make("actor-towelknight", "idle");
	private static Animation run = Animation.make("actor-towelknight", "run");
	private static Animation hurt = Animation.make("actor-towelknight", "hurt");
	private static Animation killed = Animation.make("actor-towelknight", "dead");
	private Point point;
	private Sword sword;
	private float runDelay;
	private Point water;
	private Room currentRoom;
	private float idleTime;
	private Room target;
	private Point targetPoint;
	private Point nextPathPoint;
	private Animation animation;

	{
		hpMax = 10;
		speed = 10;
	}

	@Override
	public void init() {
		super.init();

		this.runDelay = Random.newFloat(3f, 6f);
		this.sword = new IronSword();
		this.sword.setOwner(this);
		this.body = this.createBody(1, 2, 12, 12, BodyDef.BodyType.DynamicBody, false);
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.dead) {
			super.common();
			return;
		}

		if (this.animation != null) {
			this.animation.update(dt);
		}

		this.sword.update(dt);
		this.ai(dt);
		super.common();
	}

	@Override
	public void render() {
		Graphics.batch.setColor(1, 1, 1, this.a);

		float v = Math.abs(this.vel.x) + Math.abs(this.vel.y);

		if (this.dead) {
			this.animation = killed;
		} else if (this.invt > 0) {
			this.animation = hurt;
		} else if (v > 9.9) {
			this.animation = run;
		} else {
			this.animation = idle;
		}

		this.animation.render(this.x, this.y, this.flipped);
		Graphics.batch.setColor(1, 1, 1, this.a);
		this.sword.render(this.x, this.y, this.flipped);
		Graphics.batch.setColor(1, 1, 1, 1);
	}

	@Override
	protected ArrayList<Item> getDrops() {
		ArrayList<Item> items = super.getDrops();

		if (Random.chance(5)) {
			items.add(this.sword);
		}

		return items;
	}

	public float flee;

	private void ai(float dt) {
		if (this.state.equals("idle")) {
			this.idle(dt);
		} else if (this.state.equals("toRelax")) {
			this.toRelax(dt);
		} else if (this.state.equals("relax")) {
			this.relax(dt);
		} else if (this.state.equals("alerted")) {
			this.alerted(dt);
		} else if (this.state.equals("chasing")) {
			this.chasing(dt);
		} else if (this.state.equals("tired")) {
			this.tired(dt);
		} else if (this.state.equals("attacking")) {
			this.attacking(dt);
		} else if (this.state.equals("fleeing")) {
			this.fleeing(dt);
		} else if (this.state.equals("roam")) {
			this.roam(dt, 4);
		}
	}

	private void checkForSpa() {
		this.findCurrentRoom();

		for (int i = 0; i < this.currentRoom.getWidth() * this.currentRoom.getHeight(); i++) {
			Point point = this.currentRoom.getRandomCell();

			if (Dungeon.level.isWater((int) point.x, (int) point.y, false)) {
				this.water = new Point(point.x * 16, point.y * 16);
				this.become("toRelax");
			}
		}
	}

	private Player player;
	private Point lastSeen;
	private float noticeTime;

	@Override
	protected void onHurt() {
		super.onHurt();

		if (this.player != null) {
			this.flee += 0.5f;
			this.become(this.flee >= 1f || this.hp < 3 ? "fleeing" : "chasing");
		}
	}

	private void checkForPlayer(float dt) {
		if (this.flee >= 1f) {
			this.become("fleeing");
			return;
		}

		if (this.player != null) {
			if (this.hp < 3) {
				this.become("fleeing");
				return;
			}

			this.lastSeen = new Point(this.player.x, this.player.y);
			this.noticeTime += dt;
			this.player.heat += dt * 4;

			if (!this.canSee(this.player)) {
				this.player = null;
				return;
			}

			if (this.player.heat > Level.noticed && this.noticeTime > 3f) {
				Level.noticed += 1;
				this.become("chasing");
			}

			return;
		}

		for (Player player : Player.all) {
			if (this.canSee(player)) {
				this.player = player;
				this.become("alerted");
				return;
			}
		}
	}

	private void findCurrentRoom() {
		this.currentRoom = Dungeon.level.findRoomFor(Math.round(this.x / 16), Math.round(this.y / 16));
	}

	private void idle(float dt) {
		if (this.idleTime == 0) {
			this.idleTime = Random.newFloat(3f, 10f);
		}

		this.checkForPlayer(dt);

		if (this.t >= this.idleTime && this.player == null) {
			this.idleTime = 0;
			this.become("roam");
			this.checkForSpa();
		}
	}

	private void toRelax(float dt) {
		if (this.nextPathPoint == null) {
			this.nextPathPoint = this.getCloser(this.water);

			if (this.nextPathPoint == null) {
				this.target = null;
			}
		}

		float d = 16f;

		if (this.nextPathPoint != null) {
			d = this.moveToPoint(this.nextPathPoint.x + 8, this.nextPathPoint.y + 8, 6);
		}

		if (d < 4f) {
			this.nextPathPoint = null;

			if (this.getDistanceTo(this.water.x + 8, this.water.y + 8) < 16f) {
				this.become("relax");
			}
		}
	}

	private void relax(float dt) {
		this.checkForPlayer(dt);
	}

	private void alerted(float dt) {
		this.checkForPlayer(dt);
	}

	private float chaseT;
	private float tm;

	private void chasing(float dt) {
		this.checkForPlayer(dt);

		this.tm += dt;

		if (this.lastSeen == null) {
			this.chaseT = 0;
			this.tm = 0;

			return;
		}

		if (this.chaseT == 0) {
			this.chaseT = Random.newFloat(8f, 10f);
		}

		if (this.nextPathPoint == null) {
			this.nextPathPoint = this.getCloser(new Point(this.lastSeen.x, this.lastSeen.y));

			if (this.nextPathPoint == null) {
				this.chaseT = 0;
				this.tm = 0;
				this.lastSeen = null;
			}
		}

		if (this.chaseT <= this.tm) {
			this.chaseT = 0;
			this.tm = 0;

			this.become("tired");

			return;
		}

		float d = 16f;

		if (this.nextPathPoint != null) {
			d = this.moveToPoint(this.nextPathPoint.x + 8, this.nextPathPoint.y + 8, 10);
		}

		if (d < 4f) {
			this.nextPathPoint = null;

			if (this.getDistanceTo(this.lastSeen.x + 8, this.lastSeen.y + 8) < 16f) {
				if (this.player == null) {
					this.become("idle");
					// todo: question mark?
				} else {
					this.become("attacking");
				}

				this.chaseT = 0;
				this.tm = 0;
			}
		}
	}

	private float waitT;

	private void tired(float dt) {
		this.tm += dt;

		if (this.waitT == 0) {
			this.waitT = Random.newFloat(5f, 10f);
		}

		if (this.tm >= this.waitT) {
			this.waitT = 0;
			this.tm = 0;
			this.become(this.lastSeen == null ? "idle" : "chase");
		}
	}

	private void attacking(float dt) {
		this.checkForPlayer(dt);

		if (this.sword.getDelay() == 0) {
			if (this.getDistanceTo(this.lastSeen.x + 8, this.lastSeen.y + 8) < 32f) {
				this.become("chasing");
			} else {
				this.sword.use();
			}
		}

	}

	private void fleeing(float dt) {
		this.flee = 0;
		this.roam(dt, 10);
	}

	private void roam(float dt, float speed) {
		if (!this.state.equals("fleeing")) {
			this.checkForPlayer(dt);
		}

		if (this.target == null) {
			this.findCurrentRoom();

			if (this.currentRoom != null) {
				this.target = this.currentRoom.neighbours.get(Random.newInt(this.currentRoom.neighbours.size()));
				boolean found = false;

				for (int i = 0; i < this.target.getWidth() * this.target.getHeight(); i++) {
					this.targetPoint = this.target.getRandomCell();

					if (Dungeon.level.checkFor((int) this.targetPoint.x, (int) this.targetPoint.y, Terrain.PASSABLE)) {
						found = true;
						break;
					}

					if (!found) {
						this.targetPoint = null;
					}
				}
			}
		}

		if (this.target != null) {
			if (this.nextPathPoint == null) {
				this.nextPathPoint = this.getCloser(new Point(this.targetPoint.x * 16, this.targetPoint.y * 16));

				if (this.nextPathPoint == null) {
					this.target = null;
				}
			}

			float d = 16f;

			if (this.nextPathPoint != null) {
				d = this.moveToPoint(this.nextPathPoint.x + 8, this.nextPathPoint.y + 8, speed);
			}

			if (d < 4f) {
				this.nextPathPoint = null;

				if (this.getDistanceTo(this.targetPoint.x * 16 + 8, this.targetPoint.y * 16 + 8) < 16f) {
					this.target = null;
					this.targetPoint = null;
					this.become("idle");
					this.checkForSpa();
				}
			}
		}
	}
}