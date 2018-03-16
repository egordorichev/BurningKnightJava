package org.rexellentgames.dungeon.entity.creature.mob;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.EntranceRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.ExitRoom;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.io.IOException;

public class BurningKnight extends Mob {
	{
		hpMax = 10000;
		damage = 10;
		w = 32;
		h = 32;
		depth = 6;
		alwaysActive = true;
		flying = true;
	}

	private static float LIGHT_SIZE = 8f;

	public static BurningKnight instance;

	private static Animation idle = Animation.make(Graphics.sprites, 0.08f, 32, 160, 162,
		164, 166, 168, 170, 172, 174, 176, 178, 180, 182);
	private static Animation hurt = Animation.make(Graphics.sprites, 0.1f, 32, 184, 186);
	private static Animation killed = Animation.make(Graphics.sprites, 0.1f, 32, 188);

	public void findStartPoint() {
		Room room;

		do {
			room = Dungeon.level.getRandomRoom();
		} while (room instanceof EntranceRoom || room instanceof ExitRoom);

		Point center = room.getCenter();

		this.tp(center.x * 16 - 16, center.y * 16 - 16);
		this.state = "idle";
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
	}

	@Override
	public void init() {
		instance = this;
		super.init();

		this.t = 0;

		this.body = this.createBody(8, 3, 16, 18, BodyDef.BodyType.DynamicBody, true);
	}

	private float r;
	private float g;
	private float b;

	@Override
	public void update(float dt) {
		super.update(dt);

		if (Dungeon.level != null) {
			Dungeon.level.addLightInRadius(this.x + 16, this.y + 16, this.r, this.g, this.b, 0.5f, LIGHT_SIZE, true);
		}

		if (this.target == null) {
			this.assignTarget();
		}

		if (this.dead) {
			super.common();
			return;
		}

		if (this.invt > 0) {
			this.common();
			return;
		}

		this.ai(dt);
		super.common();
	}

	@Override
	protected void onHurt() {
		this.vel.mul(0f);
	}

	private Player target;

	@Override
	public void render() {
		Graphics.batch.setColor(1, 1, 1, this.a);

		Animation animation;

		if (this.dead) {
			animation = killed;
		} else if (this.invt > 0) {
			animation = hurt;
		} else {
			animation = idle;
		}

		animation.render(this.x, this.y, this.t, this.flipped);
	}


	private void ai(float dt) {
		if (this.state.equals("idle")) {
			this.idle(dt);
		} else if (this.state.equals("roam")) {
			this.roam(dt);
		} else if (this.state.equals("alerted")) {
			this.alerted(dt);
		} else if (this.state.equals("chase")) {
			this.chase(dt);
		} else if (this.state.equals("dash")) {
			this.dash(dt);
		} else if (this.state.equals("preattack")) {
			this.preattack(dt);
		} else if (this.state.equals("attack")) {
			this.attack(dt);
		}
	}

	private float idleTime;

	private void idle(float dt) {
		this.r = 0; this.g = 0; this.b = 0;

		if (this.idleTime == 0) {
			this.idleTime = Random.newFloat(5f, 10f);
		} else if (this.t >= this.idleTime) {
			this.become("roam");
			this.idleTime = 0;
		}

		this.checkForTarget();
	}

	private Point roomToVisit;
	private Room last;
	private float roamTime;

	private void checkForTarget() {
		for (Player player : Player.all) {
			if (player.invisible) {
				continue;
			}

			float dx = player.x - this.x - 8;
			float dy = player.y - this.y - 8;
			float d = (float) Math.sqrt(dx * dx + dy * dy);

			if (d < (player.getLightSize() + LIGHT_SIZE - 3) * 16) {
				this.target = player;
				this.roamTime = 0;
				this.idleTime = 0;
				this.become("alerted");
				return;
			}
		}
	}

	private void roam(float dt) {
		this.r = 0; this.g = 0.0f; this.b = 0.3f;

		if (this.roamTime == 0) {
			this.roamTime = Random.newFloat(30f, 60f);
		} else if (this.t > this.roamTime) {
			this.become("idle");
			this.findStartPoint(); // todo: delay here
			return;
		}

		this.checkForTarget();

		if (this.roomToVisit == null) {
			Room room;
			float d;
			int attempts = 0;

			do {
				room = Dungeon.level.getRandomRoom();
				Point point = room.getCenter();

				float dx = point.x * 16 - this.x;
				float dy = point.y * 16 - this.y;
				d = (float) Math.sqrt(dx * dx + dy * dy);

				attempts++;

				if (attempts > 40) {
					room = Dungeon.level.getRandomRoom();
					break;
				}
			} while(d > 400f && (this.last == null || last != room));

			this.roomToVisit = room.getCenter();
			this.roomToVisit.mul(16);
			this.last = room;
		}

		float dx = this.roomToVisit.x - this.x;
		float dy = this.roomToVisit.y - this.y;
		float d = (float) Math.sqrt(dx * dx + dy * dy);

		if (d < 16) {
			if (Random.chance(25)) {
				this.become("idle");
				this.roamTime = 0;
			} else {
				this.roomToVisit = null;
			}

			return;
		}

		this.vel.x += dx / d * 3;
		this.vel.y += dy / d * 3;
	}

	private void alerted(float dt) {
		this.r = 0.8f; this.g = 0; this.b = 0.4f;

		if (this.t >= 1f) {
			this.become("chase");
		}
	}

	private Point lastTargetPosition = new Point();
	private float dashWait;

	private void chase(float dt) {
		float dx = 0;
		float dy = 0;
		float d = 1000;

		if (this.dashWait == 0) {
			this.dashWait = Random.newFloat(5f, 7f);
		}

		if (this.target != null) {
			dx = this.target.x - this.x - 8;
			dy = this.target.y - this.y - 8;
			d = (float) Math.sqrt(dx * dx + dy * dy);
		}

		if (this.target == null || d > (this.target.getLightSize() + LIGHT_SIZE - 3) * 16) {
			this.target = null;
			this.r = 0.8f; this.g = 0.0f; this.b = 0.8f;

			dx = this.lastTargetPosition.x - this.x - 8;
			dy = this.lastTargetPosition.y - this.y - 8;
			d = (float) Math.sqrt(dx * dx + dy * dy);

			if (d < 16f) {
				this.become("idle");
				this.dashWait = 0;
				return;
			}
		} else {
			this.r = 1; this.g = 0.2f; this.b = 0;

			this.lastTargetPosition.x = this.target.x;
			this.lastTargetPosition.y = this.target.y;
		}

		if (d < ATTACK_DIST && this.target != null) {
			this.dashWait = 0;
			this.become("preattack");
			return;
		}

		this.vel.x += dx / d * 4;
		this.vel.y += dy / d * 4;

		if (this.t > this.dashWait && d > DASH_DIST) {
			this.dashWait = 0;
			this.become("dash");
		}
	}

	private static final int DASH_DIST = 64;
	private static final int ATTACK_DIST = 32;

	private void dash(float dt) {
		float dx = 0;
		float dy = 0;
		float d = 1000;

		if (this.target != null) {
			dx = this.target.x - this.x - 8;
			dy = this.target.y - this.y - 8;
			d = (float) Math.sqrt(dx * dx + dy * dy);
		}

		if (this.target == null || d > (this.target.getLightSize() + LIGHT_SIZE - 3) * 16) {
			this.target = null;
			this.r = 0.8f; this.g = 0.0f; this.b = 0.8f;

			dx = this.lastTargetPosition.x - this.x - 8;
			dy = this.lastTargetPosition.y - this.y - 8;
			d = (float) Math.sqrt(dx * dx + dy * dy);

			if (d < 16f) {
				this.become("idle");
				return;
			}
		} else {
			this.r = 1; this.g = 0.2f; this.b = 0;

			this.lastTargetPosition.x = this.target.x;
			this.lastTargetPosition.y = this.target.y;
		}

		if (d < ATTACK_DIST && this.target != null) {
			this.dashWait = 0;
			this.become("preattack");
			return;
		}

		this.vel.x += dx / d * 10;
		this.vel.y += dy / d * 10;

		if (this.t > 2f) {
			this.become("chase");
		}
	}

	private void preattack(float dt) {
		this.r = 1f; this.g = 0f; this.b = 0f;

		if (this.t > 0.5f) {
			this.become("attack");
		}
	}

	private void attack(float dt) {
		this.r = 0f; this.g = 1f; this.b = 0f;

		if (this.t == 0) {

		}

		if (this.t > 0.5f) {
			this.become("chase");
		}
	}
}