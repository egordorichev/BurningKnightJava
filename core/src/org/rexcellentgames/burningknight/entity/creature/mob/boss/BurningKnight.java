package org.rexcellentgames.burningknight.entity.creature.mob.boss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Settings;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.assets.Locale;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.Buff;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Lamp;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.FireballProjectile;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.ladder.EntranceRoom;
import org.rexcellentgames.burningknight.entity.level.save.GameSave;
import org.rexcellentgames.burningknight.entity.level.save.PlayerSave;
import org.rexcellentgames.burningknight.game.state.InGameState;
import org.rexcellentgames.burningknight.ui.UiBanner;
import org.rexcellentgames.burningknight.util.*;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.util.ArrayList;

public class BurningKnight extends Boss {
	public static BurningKnight instance;
	public static float LIGHT_SIZE = 12f;
	private static Animation animations = Animation.make("actor-burning-knight");
	private Room last;
	private AnimationData idle;
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData animation;
	private long sid;
	private static Sound sfx;

	{
		texture = "ui-bkbar-skull";
		hpMax = 430;
		damage = 10;
		w = 42;
		h = 38;
		ignoreRooms = true;
		depth = 6;
		alwaysActive = true;
		speed = 2;
		maxSpeed = 100;
		flying = true;

		idle = animations.get("idle");
		hurt = animations.get("hurt");
		killed = animations.get("dead");
		unhittable = false;
	}

	@Override
	protected void onHurt(float a, Creature from) {
		super.onHurt(a, from);

		Vector3 vec = Camera.game.project(new Vector3(this.x + this.w / 2, this.y + this.h / 2, 0));
		vec = Camera.ui.unproject(vec);
		vec.y = Display.GAME_HEIGHT - vec.y;

		Dungeon.glitchTime = 0.3f;
		Dungeon.shockTime = 0;
		Dungeon.shockPos.x = (vec.x) / Display.GAME_WIDTH;
		Dungeon.shockPos.y = (vec.y) / Display.GAME_HEIGHT;
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
		} while (room instanceof EntranceRoom);

		this.tp(center.x * 16 - 16, center.y * 16 - 16);

		if (!this.state.equals("unactive")) {
			this.become("idle");
		}
	}

	@Override
	public void init() {
		sfx = Audio.getSound("bk");
		this.sid = sfx.loop(Audio.playSfx("bk", 0f));

		sfx.setVolume(this.sid, 0);

		instance = this;
		super.init();

		this.t = 0;
		this.body = this.createSimpleBody(7, 10, 21, 18, BodyDef.BodyType.DynamicBody, true);
		this.become("unactive");
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

		this.lastFrame += dt;

		if (this.lastFrame > 0.2f) {
			this.lastFrame = 0;
			if (frames.size() > 5) {
				frames.remove(0);
			}

			Frame p = new Frame();

			p.x = this.x;
			p.y = this.y;
			p.frame = this.animation == null ? 0 : this.animation.getFrame();
			p.flipped = this.flipped;

			frames.add(p);
		}

		if (this.freezed) {
			return;
		}

		if (Dungeon.level != null) {
			Dungeon.level.addLightInRadius(this.x + 16, this.y + 16, 0, 0, 0, 3f * this.a, LIGHT_SIZE, true);
		}

		if (this.onScreen) {
			float dx = this.x + 8 - Player.instance.x;
			float dy = this.y + 8 - Player.instance.y;
			float d = (float) Math.sqrt(dx * dx + dy * dy);

			sfx.setVolume(sid, this.state.equals("unactive") ? 0 : Settings.sfx * MathUtils.clamp(0, 1, (100 - d) / 100f));
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

		if (this.invt > 0) {
			this.common();
			return;
		}

		if (this.animation != null) {
			this.animation.update(dt * speedMod);
		}

		super.common();
	}

	@Override
	protected boolean canHaveBuff(Buff buff) {
		return !(buff instanceof BurningBuff);
	}

	public static ShaderProgram shader;

	static {
		String vertexShader;
		String fragmentShader;
		vertexShader = Gdx.files.internal("shaders/bk.vert").readString();
		fragmentShader = Gdx.files.internal("shaders/bk.frag").readString();
		shader = new ShaderProgram(vertexShader, fragmentShader);
		if (!shader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());
	}

	@Override
	public void render() {
		if (this.state.equals("unactive")) {
			return;
		}

		if (this.invt > 0) {
			this.animation = hurt;
		} else {
			this.animation = idle;
		}

		Graphics.batch.end();
		Mob.shader.begin();
		Mob.shader.setUniformf("u_color", new Vector3(1, 0.3f, 0.3f));
		Mob.shader.setUniformf("u_a", this.a / 2);
		Mob.shader.end();
		Graphics.batch.setShader(Mob.shader);
		Graphics.batch.begin();
		TextureRegion region = this.animation.getCurrent().frame;

		float dt = Gdx.graphics.getDeltaTime();

		for (Frame point : this.frames) {
			float s = point.s;

			if (!Dungeon.game.getState().isPaused() && !InGameState.map) {
				point.s -= dt * 0.8f;
			}

			TextureRegion r = this.idle.getFrames().get(Math.min(1, point.frame)).frame;
			Graphics.render(r, point.x + this.w / 2, point.y + this.h / 2, 0, r.getRegionWidth() / 2, r.getRegionHeight() / 2, false, false, point.flipped ? -s : s, s);
		}

		// todo: fix

		Graphics.batch.end();
		shader.begin();

		Texture texture = region.getTexture();

		shader.setUniformf("time", Dungeon.time);
		shader.setUniformf("a", this.a);
		shader.setUniformf("pos", new Vector2(((float) region.getRegionX()) / texture.getWidth(), ((float) region.getRegionY()) / texture.getHeight()));
		shader.setUniformf("size", new Vector2(((float) region.getRegionWidth()) / texture.getWidth(), ((float) region.getRegionHeight()) / texture.getHeight()));
		shader.end();
		Graphics.batch.setShader(shader);
		Graphics.batch.begin();

		TextureRegion reg = this.animation.getCurrent().frame;

		Graphics.render(reg, this.x + this.w / 2, this.y + this.h / 2, 0, reg.getRegionWidth() / 2, reg.getRegionHeight() / 2, false, false, this.flipped ? -1 : 1, 1);

		Graphics.batch.end();
		Graphics.batch.setShader(null);
		Graphics.batch.begin();
	}

	private float lastFrame;

	private ArrayList<Frame> frames = new ArrayList<>();

	private static class Frame extends Point {
		public boolean flipped;
		public float s = 1f;
		public int frame;
	}

	@Override
	public void renderShadow() {
		// Graphics.shadow(this.x + this.w / 4f, this.y, this.w / 2f, this.h / 2f, 5f);
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
			this.delay = Random.newFloat(1f, 3f);
		}

		@Override
		public void update(float dt) {
			if (this.t >= this.delay) {
				self.become("roam");
				return;
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
			if (self.lastSeen == null) {
				self.become("roam");
				return;
			}

			float d = self.getDistanceTo(self.lastSeen.x + 8, self.lastSeen.y + 8);

			if (this.flyTo(self.lastSeen, self.speed * 1.2f, 64f)) {
				self.become("preattack");
				return;
			} else if ((self.lastSeen == null || (self.target != null && d > (LIGHT_SIZE) * 16)) || (self.target != null && self.target.invisible)) {
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
			if (this.t >= 1f) {
				self.become("attack");
				return;
			}

			super.update(dt);
		}
	}

	public class AttackState extends BKState {
		public boolean attacked;

		@Override
		public void update(float dt) {
			if (self.target == null) {
				self.become("idle");
				return;
			}

			if (!this.attacked) {
					FireballProjectile ball = new FireballProjectile();

					ball.owner = self;
					ball.bad = true;
					ball.target = self.target;
					ball.x = self.x + (self.w) / 2;
					ball.y = self.y + (self.h) / 2;

					Dungeon.area.add(ball);

				this.attacked = true;
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
					self.become(self.dialog == null ? "chase" : "dialog");
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
	public static DialogData onLampTake = dialogs.get("you_seem_new");
	public static DialogData itsYouAgain = dialogs.get("its_you_again");
	public static DialogData justDie = dialogs.get("just_die");
	public static DialogData noPoint = dialogs.get("it_is_pointless");
	public DialogData dialog;
	private float volume;

	private Sound voice;
	private long vid;

	public class DialogState extends BKState {
		@Override
		public void onEnter() {
			super.onEnter();

			voice = Audio.getSound("bk_voice");
			vid = Audio.playSfx("bk_voice", Settings.sfx, 1f);
			voice.setVolume(vid, 0);
			voice.pause(vid);

			Dialog.active = self.dialog;
			Dialog.active.start();

			Camera.follow(self, false);

			Dialog.active.onEnd(new Runnable() {
				@Override
				public void run() {
					Camera.follow(Player.instance, false);
				}
			});

			Dialog.active.onStop(new Runnable() {
				@Override
				public void run() {
					Tween.to(new Tween.Task(0, 0.3f) {
						@Override
						public float getValue() {
							return volume;
						}

						@Override
						public void setValue(float value) {
							volume = value;
							voice.setVolume(vid, value);
						}

						@Override
						public void onEnd() {
							super.onEnd();
							voice.pause(vid);
						}
					});
				}
			});

			Dialog.active.onStart(new Runnable() {
				@Override
				public void run() {
					voice.resume(vid);
					Tween.to(new Tween.Task(Settings.sfx, 0.3f) {
						@Override
						public float getValue() {
							return volume;
						}

						@Override
						public void setValue(float value) {
							volume = value;
							voice.setVolume(vid, value);
						}

						@Override
						public void onEnd() {
							super.onEnd();
						}
					});
				}
			});
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (Dialog.active == null) {
				self.become("chase");
			}
		}

		@Override
		public void onExit() {
			super.onExit();
			talked = true;

			UiBanner banner = new UiBanner();
			banner.text = Locale.get("burning_knight");
			Dungeon.area.add(banner);
		}
	}

	@Override
	protected void die(boolean force) {
		super.die(force);

		instance = null;
		this.done = true;
		GameSave.defeatedBK = true;

		deathEffect(killed);
		PlayerSave.remove(this);
	}

	public class UnactiveState extends BKState {
		@Override
		public void onEnter() {
			super.onEnter();

			self.a = 0;
			self.setUnhittable(true);
			Mob.every.remove(self);
		}

		@Override
		public void onExit() {
			super.onExit();
			self.setUnhittable(false);
			Mob.every.add(self);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (Player.instance.room != null && !(Player.instance.room instanceof EntranceRoom)) {
				Log.info("BK is out");

				float a = Random.newFloat((float) (Math.PI * 2));

				self.setUnhittable(false);
				self.tp(Player.instance.x + Player.instance.w / 2 + (float) Math.cos(a) * 64f,
					Player.instance.y + Player.instance.h / 2 + (float) Math.sin(a) * 64f);
				self.become("fadeIn");
				self.a = 0;

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

	private void checkForTarget() {
		for (Creature player : (this.stupid ? Mob.all : Player.all)) {
			if (player.invisible || player == this) {
				continue;
			}

			// todo
			if (player instanceof Player && this.state.equals("wait") && ((Player) player).room != this.room) {
				continue;
			}

			float dx = player.x - this.x - 8;
			float dy = player.y - this.y - 8;
			float d = (float) Math.sqrt(dx * dx + dy * dy);

			if (d < (LIGHT_SIZE + 3) * 16) {
				this.target = player;
				this.become("alerted");
				this.noticeSignT = 2f;
				this.hideSignT = 0f;

				return;
			}
		}
	}
}