package org.rexellentgames.dungeon.entity.creature.mob;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.UiLog;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.buff.Buff;
import org.rexellentgames.dungeon.entity.creature.buff.BurningBuff;
import org.rexellentgames.dungeon.entity.creature.fx.Fireball;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Lamp;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.entity.level.rooms.regular.BKRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.EntranceRoom;
import org.rexellentgames.dungeon.entity.level.rooms.regular.ladder.ExitRoom;
import org.rexellentgames.dungeon.entity.plant.Plant;
import org.rexellentgames.dungeon.util.*;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.io.IOException;

public class BurningKnight extends Mob {
	public static BurningKnight instance;
	public float rageLevel;
	private boolean inRage;
	public static float LIGHT_SIZE = 12f;
	private static Animation animations = Animation.make("actor_burning_knight");
	private Room last;
	public static Point throne;
	private AnimationData idle;
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData animation;
	public int lock;
	private long sid;
	private static Sound sfx;

	{
		mind = Mind.ATTACKER;
		hpMax = 430;
		damage = 10;
		w = 36;
		h = 35;
		ignoreRooms = true;
		depth = 6;
		alwaysActive = true;
		speed = 2;
		maxSpeed = 100;
		flying = true;

		idle = animations.get("idle");
		hurt = animations.get("hurt");
		killed = animations.get("dead");
	}

	public void unlockHealth() {
		this.lock++;
		this.unhittable = false;

		Tween.to(new Tween.Task(1f, 0.3f) {
			@Override
			public float getValue() {
				return a;
			}

			@Override
			public void setValue(float value) {
				a = value;
			}
		});

		UiLog.instance.print("[green]Burning Knight is now hittable!");
		this.checkForRage();
	}

	public void findStartPoint() {
		if (this.attackTp) {
			float a = Random.newFloat(0, (float) (Math.PI * 2));
			this.tp((float) Math.cos(a) * 64 + Player.instance.x - Player.instance.w / 2 + this.w / 2,
				(float) Math.sin(a) * 64 + Player.instance.y - Player.instance.h / 2 + this.h / 2);

			return;
		}

		Room room;
		Point center;

		int attempts = 0;

		do {
			room = Dungeon.level.getRandomRoom();
			center = room.getCenter();

			if (attempts++ > 40) {
				Log.info("Too many");
				break;
			}
		} while (room instanceof EntranceRoom || room instanceof ExitRoom);

		this.tp(center.x * 16 - 16, center.y * 16 - 16);

		if (!this.state.equals("unactive")) {
			this.become("idle");
		}
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		throne = new Point(reader.readInt16(), reader.readInt16());
		this.lock = reader.readInt16();
	}

	public int getLock() {
		return Math.max(0, this.hpMax - this.lock * 100 - 100);
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeInt16((short) (throne != null ? throne.x : 0));
		writer.writeInt16((short) (throne != null ? throne.y : 0));
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
		this.sfx = Graphics.getSound("bk");
		this.sid = this.sfx.loop(Graphics.playSfx("bk"));

		this.sfx.setVolume(this.sid, 0);

		instance = this;
		super.init();

		this.t = 0;
		this.body = this.createSimpleBody(7, 10, 21, 18, BodyDef.BodyType.DynamicBody, true);
		this.become("unactive");
	}

	@Override
	public void become(String state) {
		super.become(state);

		Log.info("become " + state);
	}

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity instanceof Player && !this.isDead()) {
			((Player) entity).addBuff(new BurningBuff().setDuration(10));
		} else if (entity instanceof Mob && !this.isDead()) {
			((Mob) entity).addBuff(new BurningBuff().setDuration(10));
		} else if (entity instanceof Plant) {
			((Plant) entity).startBurning();
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (Dungeon.level != null) {
			Dungeon.level.addLightInRadius(this.x + 16, this.y + 16, 0, 0, 0, 3f * this.a, LIGHT_SIZE, true);
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

			Tween.to(new Tween.Task(0.5f, 0.3f) {
				@Override
				public float getValue() {
					return a;
				}

				@Override
				public void setValue(float value) {
					a = value;
				}
			});

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
	protected boolean canHaveBuff(Buff buff) {
		return !(buff instanceof BurningBuff);
	}

	@Override
	protected void onHurt() {
		super.onHurt();
		this.checkForRage();
	}

	public void onRageStart() {
		Log.error(this.hp + " hp and " + this.rageLevel +" rl");

		this.modifySpeed(4);

		this.damage += 5;

		UiLog.instance.print("[orange]Burning Knight is now raging!");
		Log.info("BK entered rage state");
	}

	public void onRageEnd() {
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
		Graphics.batch.setColor(1, 1, 1, this.a);

		if (this.dead) {
			this.animation = killed;
		} else if (this.invt > 0) {
			this.animation = hurt;
		} else {
			this.animation = idle;
		}

		this.animation.render(this.x, this.y, this.flipped);

		/*Graphics.batch.end();
		Graphics.shape.setColor(1, 0, 1, 1);
		Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);

		for (int i = 0; i < 4; i++) {
			double a = i * Math.PI / 2;

			Graphics.shape.line(this.x + this.w / 2, this.y + this.h / 2,
				(float) (this.x + this.w / 2 + Math.cos(a) * 64), (float) (this.y + this.h / 2 + Math.sin(a) * 64));
			a -= Math.toRadians(5);
			Graphics.shape.line(this.x + this.w / 2, this.y + this.h / 2,
				(float) (this.x + this.w / 2 + Math.cos(a) * 64), (float) (this.y + this.h / 2 + Math.sin(a) * 64));
			a += Math.toRadians(10);
			Graphics.shape.line(this.x + this.w / 2, this.y + this.h / 2,
				(float) (this.x + this.w / 2 + Math.cos(a) * 64), (float) (this.y + this.h / 2 + Math.sin(a) * 64));
		}

		Graphics.shape.setColor(0, 1, 1, 1);

		for (int i = 0; i < 4; i++) {
			double a = i * Math.PI / 2 + Math.PI / 4;

			Graphics.shape.line(this.x + this.w / 2, this.y + this.h / 2,
				(float) (this.x + this.w / 2 + Math.cos(a) * 64), (float) (this.y + this.h / 2 + Math.sin(a) * 64));
			a -= Math.toRadians(5);
			Graphics.shape.line(this.x + this.w / 2, this.y + this.h / 2,
				(float) (this.x + this.w / 2 + Math.cos(a) * 64), (float) (this.y + this.h / 2 + Math.sin(a) * 64));
			a += Math.toRadians(10);
			Graphics.shape.line(this.x + this.w / 2, this.y + this.h / 2,
				(float) (this.x + this.w / 2 + Math.cos(a) * 64), (float) (this.y + this.h / 2 + Math.sin(a) * 64));
		}

		Graphics.shape.end();
		Graphics.shape.setColor(1, 1, 1, 1);
		Graphics.batch.begin();*/
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

	public class WaitState extends BKState {
		@Override
		public void update(float dt) {
			super.update(dt);
			self.checkForTarget();
		}
	}

	public class IdleState extends BKState {
		public float delay;

		@Override
		public void onEnter() {
			super.onEnter();
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
						Log.info("Too many");
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

			this.delay = Random.newFloat(5f, 7f);
		}

		@Override
		public void update(float dt) {
			float d = self.getDistanceTo(self.lastSeen.x + 8, self.lastSeen.y + 8);

			if (this.flyTo(self.lastSeen, self.speed * 1.2f, 64f)) {
				self.become("preattack");
				return;
			} else if ((self.lastSeen == null || (self.target != null && d > (LIGHT_SIZE) * 16) &&
				(Dungeon.depth > 0)) || (self.target != null && self.target.invisible)) {
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

			this.delay = Random.newFloat(1f, 3f);
		}

		@Override
		public void update(float dt) {
			float d = self.getDistanceTo(self.lastSeen.x + 8, self.lastSeen.y + 8);

			if (this.flyTo(self.lastSeen, self.speed * 3f, 32f)) {
				self.become("preattack");
				return;
			} else if ((self.lastSeen == null || d > (LIGHT_SIZE) * 16)) {

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
		@Override
		public void onEnter() {
			super.onEnter();

		}

		@Override
		public void update(float dt) {
			if (this.t >= 2f / Dungeon.depth) {
				self.become("attack");
				return;
			}

			super.update(dt);
		}
	}

	public enum AttackType {
		AREA,
		MISSILE,
		DIAGONAL,
		VERTICAL,
		NULL
	}

	public AttackType nextAttack = AttackType.NULL;

	public class AttackState extends BKState {
		public boolean attacked;

		@Override
		public void update(float dt) {
			this.findCurrentRoom();

			if (self.nextAttack == AttackType.NULL && !this.attacked) {
				float d = self.getDistanceTo(self.target.x + self.target.w / 2, self.target.y + self.target.h / 2);

				if (d <= 24f) {
					self.nextAttack = AttackType.AREA;
				} else {
					float a = (float) Math.toDegrees(self.getAngleTo(self.target.x + self.target.w / 2, self.target.y + self.target.h / 2));

					if (a < 0) {
						a += 360;
					}

					float a2 = (a % 90);

					if (a2 <= 5 || a2 >= 85) {
						self.nextAttack = AttackType.VERTICAL;
					} else {
						float a3 = ((a - 45) % 90);

						if (a3 <= 5 || a3 >= 85) {
							self.nextAttack = AttackType.DIAGONAL;
						} else {
							self.nextAttack = AttackType.MISSILE;
						}
					}
				}
			}

			if (self.nextAttack != AttackType.NULL && !this.attacked) {
				Fireball ball;

				switch (self.nextAttack) {
					case MISSILE:
						ball = new Fireball();

						ball.target = self.target;
						ball.x = self.x + (self.w - 16) / 2;
						ball.y = self.y + (self.h - 10) / 2;
						ball.bad = !self.stupid;

						Dungeon.area.add(ball);
						break;

					case AREA:
						for (int i = 0; i < Random.newInt(10, 20); i++) {
							ball = new Fireball();

							float d = Random.newFloat(16f, 64f);
							float a = Random.newFloat((float) (Math.PI * 2));

							ball.x = (float) (self.target.x + 8 + Math.cos(a) * d);
							ball.y = (float) (self.target.y + 8 + Math.sin(a) * d);
							ball.noMove = true;
							ball.bad = !self.stupid;

							Dungeon.area.add(ball);
						}

						break;
					case DIAGONAL:
						for (int i = 0; i < 4; i++) {
							ball = new Fireball();

							float a = (float) ((i * Math.PI / 2) + Math.PI / 4);
							ball.vel = new Vector2((float) Math.cos(a) * 12f, (float) Math.sin(a) * 12f);

							ball.x = self.x + (self.w - 16) / 2;
							ball.y = self.y + (self.h - 10) / 2;

							ball.bad = !self.stupid;
							Dungeon.area.add(ball);
						}
						break;
					case VERTICAL:
						for (int i = 0; i < 4; i++) {
							ball = new Fireball();

							float a = (float) (i * Math.PI / 2);

							ball.vel = new Vector2((float) Math.cos(a) * 12f, (float) Math.sin(a) * 12f);

							ball.x = self.x + (self.w - 16) / 2;
							ball.y = self.y + (self.h - 10) / 2;
							ball.bad = !self.stupid;

							a = (float) Math.toRadians(Math.round(Math.toDegrees(self.getAngleTo(self.target.x + self.target.w / 2, self.target.y + self.target.h / 2)) / 90) * 90);

							self.vel.x += Math.cos(a) * 60f;
							self.vel.y += Math.sin(a) * 60f;

							Dungeon.area.add(ball);
						}
						break;
				}

				this.attacked = true;
				self.nextAttack = AttackType.NULL;
			} if (!this.attacked && this.t >= 1f) {
				self.nextAttack = AttackType.MISSILE;
			} else if (this.t >= 2f) {
				self.become("chase");
			}

			super.update(dt);
		}
	}

	public class FadeInState extends BKState {
		@Override
		public void onEnter() {
			self.a = 0;

			Tween.to(new Tween.Task(1, 0.3f) {
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
					self.become(self.dialog == null ? "idle" : "dialog");
					self.attackTp = false;
				}
			});
		}
	}

	public boolean attackTp = false;

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
					self.findStartPoint();
					self.become("fadeIn");
				}
			});
		}
	}

	public static Dialog dialogs = Dialog.make("burning-knight");
	public static DialogData onLampTake = dialogs.get("on_lamp_take");
	public DialogData dialog;

	public class DialogState extends BKState {
		@Override
		public void onEnter() {
			super.onEnter();

			Dialog.active = self.dialog;
			Dialog.active.start();
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (Dialog.active == null) {
				self.become("chase");
			}
		}
	}

	public class UnactiveState extends BKState {
		@Override
		public void onEnter() {
			super.onEnter();

			self.a = 0;
			self.unhittable = true;
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (Player.instance.currentRoom != null && !(Player.instance.currentRoom instanceof EntranceRoom)) {
				Log.info("BK is out");

				float a = Random.newFloat((float) (Math.PI * 2));

				self.unhittable = false;
				self.tp(Player.instance.x + Player.instance.w / 2 + (float) Math.cos(a) * 64f,
					Player.instance.y + Player.instance.h / 2 + (float) Math.sin(a) * 64f);
				self.become("fadeIn");

				Lamp.play();
			}
		}
	}

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "idle":
				return new IdleState();
			case "roam":
				return new RoamState();
			case "alerted":
				return new AlertedState();
			case "chase": case "fleeing":
				return new ChaseState();
			case "dash":
				return new DashState();
			case "preattack":
				return new PreattackState();
			case "attack":
				return new AttackState();
			case "fadeIn":
				return new FadeInState();
			case "fadeOut":
				return new FadeOutState();
			case "dialog":
				return new DialogState();
			case "wait":
				return new WaitState();
			case "unactive":
				return new UnactiveState();
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
		for (Creature player : (this.stupid ? Mob.all : Player.all)) {
			if (player.invisible || player == this) {
				continue;
			}

			if (player instanceof Player && this.state.equals("wait") && ((Player) player).currentRoom != this.room) {
				continue;
			}

			float dx = player.x - this.x - 8;
			float dy = player.y - this.y - 8;
			float d = (float) Math.sqrt(dx * dx + dy * dy);

			if (d < (LIGHT_SIZE - 3) * 16) {
				this.target = player;
				this.become("alerted");
				this.noticeSignT = 2f;
				this.hideSignT = 0f;

				return;
			}
		}
	}
}