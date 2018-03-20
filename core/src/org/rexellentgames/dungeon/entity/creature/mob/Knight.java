package org.rexellentgames.dungeon.entity.creature.mob;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.weapon.IronSword;
import org.rexellentgames.dungeon.entity.item.weapon.Sword;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.PathFinder;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.geometry.Point;
import org.rexellentgames.dungeon.util.path.Graph;

import java.io.IOException;
import java.util.ArrayList;

public class Knight extends Mob {
	{
		hpMax = 10;
		speed = 10;
	}

	private static Animation idle = Animation.make(Graphics.sprites, 0.08f, 16, 224, 225, 226, 227,
		228, 229, 230, 231);

	private static Animation run = Animation.make(Graphics.sprites, 0.08f, 16, 232, 233, 234, 235,
		236, 237, 238, 239);

	private static Animation hurt = Animation.make(Graphics.sprites, 0.1f, 16, 240, 241);
	private static Animation killed = Animation.make(Graphics.sprites, 1f, 16, 242);

	private Point point;
	private Sword sword;
	private float runDelay;

	@Override
	public void init() {
		super.init();

		this.runDelay = Random.newFloat(3f, 6f);
		this.sword = new IronSword();
		this.sword.setOwner(this);
		this.body = this.createBody(1, 2, 12, 12, BodyDef.BodyType.DynamicBody, false);
		this.body.setTransform(this.x, this.y, 0);
	}

	private void attack() {
		if (this.sword.getDelay() == 0 && this.timer % 0.1 <= 0.017f) {
			this.sword.use();
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.dead) {
			super.common();
			return;
		}

		this.sword.update(dt);

		this.ai(dt);

		super.common();
	}

	@Override
	public void render() {
		Graphics.batch.setColor(1, 1, 1, this.a);

		Animation animation;

		float v = Math.abs(this.vel.x) + Math.abs(this.vel.y);

		if (this.dead) {
			animation = killed;
		} else if (this.invt > 0) {
			animation = hurt;
		} else if (v > 9.9) {
			animation = run;
		} else {
			animation = idle;
		}

		animation.render(this.x, this.y, this.t, this.flipped);
		Graphics.batch.setColor(1, 1, 1, this.a);
		this.sword.render(this.x, this.y, this.flipped);
		Graphics.batch.setColor(1, 1, 1, 1);

		/*
		if (this.nextPathPoint != null) {
			Graphics.batch.end();
			Graphics.shape.setColor(1, 0, 1, 1);
			Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);
			Graphics.shape.line(this.x + 8, this.y + 8, this.nextPathPoint.x + 8, this.nextPathPoint.y + 8);
			Graphics.shape.end();
			Graphics.shape.setColor(1, 1, 1, 1);
			Graphics.batch.begin();
		}

		if (this.targetPoint != null) {
			Graphics.batch.end();
			Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);
			Graphics.shape.line(this.x + 8, this.y + 8, this.targetPoint.x * 16 + 8, this.targetPoint.y * 16 + 8);
			Graphics.shape.end();
			Graphics.batch.begin();
		}*/
	}

	@Override
	protected ArrayList<Item> getDrops() {
		ArrayList<Item> items = super.getDrops();

		if (Random.chance(5)) {
			items.add(this.sword);
		}

		return items;
	}

	/*
Some ideas for generic enemy behaviour (e.g. towel knights at the moment)

Trying to address the kiting problem i.e. the situation in which you trigger an enemy but can never shake off, resulting in you kiting the entire level after a while.

**TERMS**
- **enemyStrengthPool:** a value we use to broadly track the general combined strength of enemies currently spawned on the level (i.e. difficulty, when to spawn more etc.)
- **gobboHeat:** when gobbo alerts enemies, his heat counter starts to slowly increase. Certain actions may also increase this number quicker e.g. attacking.
This value determines how many enemies gives pursuit of the gobbo. The goal is, that when you just stumble on a group of enemies, only one or a few will give chase first. When gobbo is not alerting any enemies this number degrades.

**IDLE**
- enter: not seeing the gobbo for N seconds
- action: stand around for N seconds deciding what to do

**GO RELAX**
- enter: in same room with spa available
- action: move towards spa tiles

**RELAX**
- enter: being in a pool for N seconds in IDLE state
- action: same as IDLE just a different sprite

**SPAWN**
- enter: various
- action: appear in an unoccupied spa tile

**ALERTED**
- enter: gobbo OR pusuing enemy enters line of sight
- action: display mark

**CHASING**
- enter: in ALERTED state + gobbo OR pursuing enemy is in line of sight for N seconds + number of pursuers is less than gobboHeat value

**TIRED**
- enter: in chasing state for N seconds
- action: stops to catch breath for N seconds (to let gobbo slip away)

**ATTACKING**
- enter : in CHASING state AND gobbo enters attack radius
- action: perform attack
- note: enemy may have multiple attacks with different radii. attacks may have telegraph phase. attacks may have recovery phase. this may require further states to handle

**FLEEING**
- enter: affected by fear effect OR near death
- action: moves to adjacent room

**ROAM**
- enter: was IDLE state in N seconds
- action: moves to adjacent room
- note: slower movement
	 */

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
			this.roam(dt);
		}
	}

	private Point water;
	private Room currentRoom;

	private void checkForSpa() {
		this.findCurrentRoom();

		for (int i = 0; i < this.currentRoom.getWidth() * this.currentRoom.getHeight(); i++) {
			Point point = this.currentRoom.getRandomCell();
			short tile = Dungeon.level.get((int) point.x, (int) point.y);

			if (Level.tileIsWater(tile)) {
				this.water = point;
				this.become("toRelax");
			}
		}
	}

	private void findCurrentRoom() {
		this.currentRoom = Dungeon.level.findRoomFor(Math.round(this.x / 16), Math.round(this.y / 16));
	}

	private float idleTime;

	private void idle(float dt) {
		if (this.idleTime == 0) {
			this.idleTime = Random.newFloat(3f, 10f);
		}

		if (this.t >= this.idleTime) {
			this.idleTime = 0;
			this.become("roam");
		}
	}

	private void toRelax(float dt) {

	}

	private void relax(float dt) {

	}

	private void alerted(float dt) {

	}

	private void chasing(float dt) {

	}

	private void tired(float dt) {

	}

	private void attacking(float dt) {

	}

	private void fleeing(float dt) {

	}

	private Room target;
	private Point targetPoint;
	private Point nextPathPoint;

	private void roam(float dt) {
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
				d = this.moveToPoint(this.nextPathPoint.x + 8, this.nextPathPoint.y + 8, 10);
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
		} else {
			Log.info("no target");
		}
	}
}