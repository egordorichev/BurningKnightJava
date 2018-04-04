package org.rexellentgames.dungeon.entity.creature.mob;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.UiLog;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.buff.BurningBuff;
import org.rexellentgames.dungeon.entity.creature.fx.FireRectFx;
import org.rexellentgames.dungeon.entity.creature.fx.Fireball;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.EntranceRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.ExitRoom;
import org.rexellentgames.dungeon.util.*;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.io.IOException;

public class BurningKnight extends Mob {
	private static final int DASH_DIST = 64;
	public static BurningKnight instance;
	public float rageLevel;
	private boolean inRage;
	private static float LIGHT_SIZE = 8f;
	private static Animation animations = Animation.make("actor-burning-knight");
	private float r;
	private float g;
	private float b;
	public Player target;
	private Room last;
	// private boolean[][] fx;
	private boolean sawPlayer;
	public static Point throne;
	private AnimationData idle;
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData animation;
	public int lock;
	private long sid;
	private static Sound sfx = Graphics.getSound("sfx/BK_sfx.wav");

	{
		mind = Mind.ATTACKER;
		hpMax = 430;
		damage = 10;
		w = 32;
		h = 32;
		depth = 6;
		alwaysActive = true;
		speed = 4;
		maxSpeed = 100;
		flying = true;

		idle = animations.get("idle");
		hurt = animations.get("hurt");
		killed = animations.get("dead");
	}

	public void unlockHealth() {
		this.lock++;
		this.unhittable = false;
		UiLog.instance.print("[green]Burning Knight is now hittable!");
		this.checkForRage();
	}

	public void findStartPoint() {
		if (this.sawPlayer || Dungeon.depth != 0) {
			Room room;

			do {
				room = Dungeon.level.getRandomRoom();
			} while (room instanceof EntranceRoom || room instanceof ExitRoom);

			Point center = room.getCenter();

			this.tp(center.x * 16 - 16, center.y * 16 - 16);
			this.become("idle");
		} else {
			this.become("onThrone");
			this.tp(throne.x * 16 - 8, throne.y * 16 - 8);

			Log.info("The BK is now on his throne at " + throne.x + ":" + throne.y);
		}
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		this.sawPlayer = reader.readBoolean();
		throne = new Point(reader.readInt16(), reader.readInt16());
		this.lock = reader.readInt16();

		if (!this.sawPlayer) {
			this.become("onThrone");
		}

		this.checkForRage();
	}

	public int getLock() {
		return Math.max(0, this.hpMax - this.lock * 100 - 100);
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeBoolean(this.sawPlayer);
		writer.writeInt16((short) throne.x);
		writer.writeInt16((short) throne.y);
		writer.writeInt16((short) this.lock);
	}

	public void checkForRage() {
		this.rageLevel = Math.max(25, this.hpMax - this.lock * 100 - 75);

		if (this.inRage) {
			return;
		}

		if (this.hp >= 100 && this.rageLevel >= this.hp) {
			this.inRage = true;
			this.onRageStart();
		}
	}

	@Override
	public void init() {
		this.sid = sfx.loop();

		instance = this;
		super.init();

		this.t = 0;

		if (Dungeon.depth == -1 && !this.sawPlayer) {
			this.done = true;
			BurningKnight.instance = null;
		}

		this.body = this.createBody(8, 3, 16, 18, BodyDef.BodyType.DynamicBody, true);
	}

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity instanceof Player && !this.isDead()) {
			((Player) entity).addBuff(new BurningBuff().setDuration(10));
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (Dungeon.level != null) {
			Dungeon.level.addLightInRadius(this.x + 16, this.y + 16, this.r, this.g, this.b, 0.5f, LIGHT_SIZE, true);
		}

		if (this.onScreen) {
			float dx = this.x + 8 - Player.instance.x;
			float dy = this.y + 8 - Player.instance.y;
			float d = (float) Math.sqrt(dx * dx + dy * dy);

			sfx.setVolume(sid, MathUtils.clamp(0, 1, (100 - d) / 100f));
		} else {
			sfx.setVolume(sid, 0);
		}

		if (this.target == null) {
			this.assignTarget();
		}

		if (this.dead) {
			super.common();
			return;
		}

		if (this.inRage && this.rageLevel < this.hp) {
			this.inRage = false;
			this.onRageEnd();
		}

		if (this.lock < 3 && Dungeon.depth == 4) {
			this.lock = 3;
			this.checkForRage();
		}

		int v = this.hpMax - this.lock * 100 - 100;

		if (!this.unhittable && this.hp <= v) {
			this.unhittable = true;
			Log.info("Now BK is unhittable!");
			Log.error(this.hp + " hp and " + v + " val");
			UiLog.instance.print("[red]Burning Knight is now unhittable!");
		}


		if (this.invt > 0) {
			this.common();
			return;
		}

		if (this.animation != null) {
			this.animation.update(dt);
		}

		super.common();
	}

	@Override
	protected void onHurt() {
		this.checkForRage();
	}

	public void onRageStart() {
		Log.error(this.hp + " hp and " + this.rageLevel +" rl");

		this.modifyDefense(3 + Dungeon.depth * 2);
		this.modifySpeed(4);

		this.damage += 5;

		UiLog.instance.print("[orange]Burning Knight is now raging!");
		Log.info("BK entered rage state");
	}

	public void onRageEnd() {
		this.modifyDefense(-3 - Dungeon.depth * 2);
		this.modifySpeed(-4);

		this.damage -= 5;

		Log.info("BK exited rage state");
		UiLog.instance.print("[green]Burning Knight exited his rage");
	}

	public boolean isInRage() {
		return this.inRage;
	}

	@Override
	public void render() {
		Graphics.batch.setColor(1, 1, 1, Math.max(0, this.a - 0.3f));

		if (this.dead) {
			this.animation = killed;
		} else if (this.invt > 0) {
			this.animation = hurt;
		} else {
			this.animation = idle;
		}

		this.animation.render(this.x, this.y, this.flipped);
	}

	public class BKState extends State<BurningKnight> {
		@Override
		public void update(float dt) {
			if (self.target != null) {
				self.lastSeen = new Point(self.target.x, self.target.y);
			}

			super.update(dt);
		}
	}

	public class IdleState extends BKState {
		public float delay;

		@Override
		public void onEnter() {
			super.onEnter();

			self.r = 0;
			self.g = 0;
			self.b = 0;

			this.delay = Random.newFloat(5f, 10f);
		}

		@Override
		public void update(float dt) {
			if (this.t >= this.delay) {
				self.become("roam");
				return;
			}

			if (self.inRage && Random.chance(1) && self.getHp() < self.getHpMax()) {
				self.modifyHp(1);
			}

			self.checkForTarget();
			super.update(dt);
		}
	}

	public class RoamState extends BKState {
		public float delay;
		public Point roomToVisit;

		@Override
		public void onEnter() {
			super.onEnter();
			this.delay = Random.newFloat(30f, 60f);

			self.r = 0;
			self.g = 0.0f;
			self.b = 0.3f;
		}

		@Override
		public void update(float dt) {
			if (this.t >= this.delay) {
				self.become("fadeOut");
				return;
			}

			self.checkForTarget();

			if (this.roomToVisit == null) {
				Room room;
				float d;
				int attempts = 0;

				do {
					room = Dungeon.level.getRandomRoom();
					Point point = room.getCenter();

					float dx = point.x * 16 - self.x;
					float dy = point.y * 16 - self.y;
					d = (float) Math.sqrt(dx * dx + dy * dy);

					attempts++;

					if (attempts > 40) {
						room = Dungeon.level.getRandomRoom();
						break;
					}
				} while (d > 400f && (self.last == null || self.last != room));

				this.roomToVisit = room.getCenter();
				this.roomToVisit.mul(16);
				self.last = room;
			}

			if (this.roomToVisit != null) {
				if (this.flyTo(this.roomToVisit, self.speed, 32f)) {
					if (Random.chance(25)) {
						self.become("idle");
						return;
					} else {
						this.roomToVisit = null;
					}
				}
			} else {
				Log.error("No room");
			}

			super.update(dt);
		}
	}

	public class AlertedState extends BKState {
		public static final float DELAY = 1f;

		@Override
		public void onEnter() {
			super.onEnter();

			self.r = 0.8f;
			self.g = 0;
			self.b = 0.4f;
		}

		@Override
		public void update(float dt) {
			if (this.t >= DELAY) {
				self.become("chase");
				return;
			}

			super.update(dt);
		}
	}

	public class ChaseState extends BKState {
		public float delay;

		@Override
		public void onEnter() {
			super.onEnter();

			self.r = 1;
			self.g = 0.2f;
			self.b = 0;

			this.delay = Random.newFloat(5f, 7f);
		}

		@Override
		public void update(float dt) {
			float d = self.getDistanceTo(self.lastSeen.x + 8, self.lastSeen.y + 8);

			if (this.flyTo(self.lastSeen, self.speed * 1.2f, 64f)) {
				self.become("preattack");
				return;
			} else if ((self.lastSeen == null || (self.target != null && d > (self.target.getLightSize() + LIGHT_SIZE) * 16) && (Dungeon.depth > 0 || !self.sawPlayer)) || self.target.invisible) {
				self.r = 0.8f;
				self.g = 0.0f;
				self.b = 0.8f;

				self.target = null;
				self.become("idle");
				self.noticeSignT = 0f;
				self.hideSignT = 2f;
				return;
			}

			if (d > 40f && self.t >= this.delay) {
				self.become("dash");
				return;
			}

			super.update(dt);
		}
	}

	@Override
	public void destroy() {
		super.destroy();

		sfx.stop(this.sid);
	}

	public class DashState extends BKState {
		public float delay;

		@Override
		public void onEnter() {
			super.onEnter();

			self.r = 1;
			self.g = 0.2f;
			self.b = 0;

			this.delay = Random.newFloat(1f, 3f);
		}

		@Override
		public void update(float dt) {
			float d = self.getDistanceTo(self.lastSeen.x + 8, self.lastSeen.y + 8);

			if (this.flyTo(self.lastSeen, self.speed * 3f, 32f)) {
				self.become("preattack");
				return;
			} else if ((self.lastSeen == null || d > (self.target.getLightSize() + LIGHT_SIZE) * 16) && (Dungeon.depth > 0 || !self.sawPlayer)) {
				self.r = 0.8f;
				self.g = 0.0f;
				self.b = 0.8f;

				self.target = null;
				self.become("idle");
				return;
			}

			if (self.t >= this.delay) {
				self.become("chase");
				return;
			}

			super.update(dt);
		}
	}

	public class PreattackState extends BKState {
		public final static float DELAY = 1f;

		@Override
		public void onEnter() {
			super.onEnter();

			self.r = 1f;
			self.g = 0f;
			self.b = 0f;
		}

		@Override
		public void update(float dt) {
			if (this.t >= DELAY) {
				self.become("attack");
				return;
			}

			super.update(dt);
		}
	}

	public class AttackState extends BKState {
		public boolean attacked;

		@Override
		public void onEnter() {
			super.onEnter();

			self.r = 0f;
			self.g = 1f;
			self.b = 0f;
		}

		@Override
		public void update(float dt) {
			if (!this.attacked) {
				this.attacked = true;
				float r = Random.newFloat();

				if (r < 0.25f) {
					for (int i = 0; i < 4; i++) {
						Fireball ball = new Fireball();

						ball.a = (float) (i * Math.PI / 2);
						ball.x = self.x + 12;
						ball.y = self.y + 12;

						Dungeon.area.add(ball);
					}
				} else if (r < 0.5f) {
					for (int i = 0; i < 4; i++) {
						Fireball ball = new Fireball();

						ball.a = (float) ((i * Math.PI / 2) + Math.PI / 4);
						ball.x = self.x + 12;
						ball.y = self.y + 12;

						Dungeon.area.add(ball);
					}
				} else if (r < 0.6f) {
					for (int i = 0; i < Random.newInt(10, 20); i++) {
						Fireball ball = new Fireball();

						float d = Random.newFloat(16f, 64f);
						float a = Random.newFloat((float) (Math.PI * 2));

						ball.x = (float) (self.target.x + 8 + Math.cos(a) * d);
						ball.y = (float) (self.target.y + 8 + Math.sin(a) * d);
						ball.noMove = true;

						Dungeon.area.add(ball);
					}
				} else {
					Fireball ball = new Fireball();

					ball.target = self.target;
					ball.x = self.x + 12;
					ball.y = self.y + 12;
					Dungeon.area.add(ball);
				}

			} else if (this.t > 3f) {
				self.become("chase");
			}

			super.update(dt);
		}
	}

	public class OnThroneState extends BKState {
		private boolean did;

		@Override
		public void update(float dt) {
			if (!this.did) {
				this.findCurrentRoom();

				if (this.currentRoom != null) {
					Camera.instance.clamp.add(this.currentRoom.left * 16 + 16);
				}

				this.did = true;
			}

			self.checkForTarget();
			super.update(dt);
		}
	}

	public class FadeInState extends BKState {
		@Override
		public void onEnter() {
			Tween.to(new Tween.Task(0, 0.3f) {
				@Override
				public float getValue() {
					return self.a;
				}

				@Override
				public void setValue(float value) {
					self.a = value;
				}

				@Override
				public void onEnd() {
					self.become(self.attackTp ? "chase" : "idle");
				}
			});
		}
	}

	public boolean attackTp;

	public class FadeOutState extends BKState {
		@Override
		public void onEnter() {
			super.onEnter();

			Tween.to(new Tween.Task(0, 0.3f) {
				@Override
				public float getValue() {
					return self.a;
				}

				@Override
				public void setValue(float value) {
					self.a = value;
				}

				@Override
				public void onEnd() {
					if (!self.attackTp) {
						self.findStartPoint();
					}

					self.become("fadeIn");
				}
			});
		}
	}

	@Override
	protected State getAi(String state) {
		if (state.equals("idle")) {
			return new IdleState();
		} else if (state.equals("roam")) {
			return new RoamState();
		} else if (state.equals("alerted")) {
			return new AlertedState();
		} else if (state.equals("chase")) {
			return new ChaseState();
		} else if (state.equals("dash")) {
			return new DashState();
		} else if (state.equals("preattack")) {
			return new PreattackState();
		} else if (state.equals("attack")) {
			return new AttackState();
		} else if (state.equals("onThrone")) {
			return new OnThroneState();
		} else if (state.equals("fadeIn")) {
			return new FadeInState();
		} else if (state.equals("fadeOut")) {
			return new FadeOutState();
		}

		return super.getAi(state);
	}

	@Override
	protected void onTouch(short t, int x, int y) {
		super.onTouch(t, x, y);

		if (t == Terrain.WATER) {
			this.vel.mul(1.2f);
		}
	}

	private void checkForTarget() {
		for (Player player : Player.all) {
			if (player.invisible) {
				continue;
			}

			float dx = player.x - this.x - 8;
			float dy = player.y - this.y - 8;
			float d = (float) Math.sqrt(dx * dx + dy * dy);

			if (d < (player.getLightSize() + LIGHT_SIZE - 3) * 16 && (this.sawPlayer || this.canSee(player))) {
				this.target = player;
				this.become("alerted");
				this.noticeSignT = 2f;
				this.hideSignT = 0f;

				if (!this.sawPlayer) {
					Log.info("BK NOTICED YOU! BE CAREFUL!");
					this.sawPlayer = true;
				}

				return;
			}
		}
	}
}