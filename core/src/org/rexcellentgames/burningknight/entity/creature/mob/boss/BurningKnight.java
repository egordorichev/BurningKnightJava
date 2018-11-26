package org.rexcellentgames.burningknight.entity.creature.mob.boss;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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
import org.rexcellentgames.burningknight.entity.fx.*;
import org.rexcellentgames.burningknight.entity.item.*;
import org.rexcellentgames.burningknight.entity.item.key.BurningKey;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.BulletProjectile;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.FireballProjectile;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Chest;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.boss.BossRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.entrance.EntranceRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.shop.ShopRoom;
import org.rexcellentgames.burningknight.entity.level.save.GameSave;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.entity.level.save.PlayerSave;
import org.rexcellentgames.burningknight.entity.pool.MobPool;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.ui.UiBanner;
import org.rexcellentgames.burningknight.util.*;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.io.IOException;
import java.util.ArrayList;

public class BurningKnight extends Boss {
	public static BurningKnight instance;
	public static float LIGHT_SIZE = 12f;
	private static Animation animations = Animation.make("actor-burning-knight-new");
	private Room last;
	private AnimationData idle;
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData defeated;
	private AnimationData lookUp;
	private AnimationData lookDown;
	private AnimationData animation;
	private long sid;
	private static Sound sfx;
	private BKSword sword;
	public boolean pickedKey;
	private PointLight light;


	{
		texture = "ui-bkbar-skull";
		hpMax = 50;
		damage = 10;
		w = 23;
		h = 30;
		depth = 16;
		alwaysActive = true;
		speed = 2;
		maxSpeed = 100;
		setFlying(true);

		idle = animations.get("idle");
		anim = idle;

		hurt = animations.get("hurt");
		killed = animations.get("dead");
		defeated = animations.get("defeat");
		lookUp = animations.get("look_up");
		lookDown = animations.get("look_down");

		lookUp.setAutoPause(true);
		lookDown.setAutoPause(true);
		unhittable = false;
	}

	@Override
	protected void onHurt(int am, Entity from) {
		super.onHurt(am, from);
		this.playSfx("BK_hurt_" + Random.newInt(1, 6));
	}

	private float dtx;
	private float dty;

	@Override
	public void die() {
		this.dead = false;
		this.deathDepth = Dungeon.depth;
		this.done = false;
		this.hp = 1;
		this.rage = true;
		this.unhittable = true;
		this.ignoreRooms = true;
		this.pickedKey = false;

		dtx = x;
		dty = y;

		Player.instance.setUnhittable(true);
		Camera.follow(this, false);

		if (dest) {
			return;
		}

		Camera.shake(10);
		this.invt = 3f;
		this.dest = true;
		Audio.stop();

		Tween.to(new Tween.Task(1, 1f) {
			@Override
			public float getValue() {
				return Dungeon.white;
			}

			@Override
			public void setValue(float value) {
				Dungeon.white = value;
			}

			@Override
			public void onEnd() {
				Tween.to(new Tween.Task(0, 0.1f) {
					@Override
					public float getValue() {
						return Dungeon.white;
					}

					@Override
					public void setValue(float value) {
						Dungeon.white = value;
					}

					@Override
					public void onEnd() {
						Vector3 vec = Camera.game.project(new Vector3(dtx + w / 2, dty + h / 2, 0));
						vec = Camera.ui.unproject(vec);
						vec.y = Display.UI_HEIGHT - vec.y;

						Dungeon.shockTime = 0;
						Dungeon.shockPos.x = (vec.x) / Display.UI_WIDTH;
						Dungeon.shockPos.y = (vec.y) / Display.UI_HEIGHT;

						for (int i = 0; i < 30; i++) {
							CurseFx fx = new CurseFx();

							fx.x = dtx + w / 2 + Random.newFloat(-w, w);
							fx.y = dty + h / 2 + Random.newFloat(-h, h);

							Dungeon.area.add(fx);
						}

						playSfx("explosion");
					}
				});
			}
		}).delay(2f);
	}

	public boolean dest;

	public void findStartPoint() {
		if (this.attackTp) {
			float a = Random.newFloat(0, (float) (Math.PI * 2));
			float d = isActiveState() ? 64 : 96;
			this.tp((float) Math.cos(a) * d + Player.instance.x - Player.instance.w / 2 + this.w / 2,
				(float) Math.sin(a) * d + Player.instance.y - Player.instance.h / 2 + this.h / 2);

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
		talked = true;
		saw = true;

		sfx = Audio.getSound("bk");
		this.sid = sfx.loop(Audio.playSfx("bk", 0f));

		sfx.setVolume(this.sid, 0);

		instance = this;
		super.init();

		this.t = 0;
		this.body = this.createSimpleBody(0, 3, 23, 20, BodyDef.BodyType.DynamicBody, false);
		this.become("unactive");

		sword = new BKSword();
		sword.setOwner(this);
		sword.modifyDamage(-10);

		this.tp(0, 0);

		light = World.newLight(256, new Color(1, 0.5f, 0, 1f), 128, x, y);
	}

	public void restore() {
		this.pickedKey = false;
		this.hpMax = Dungeon.depth * 20 + 30;
		this.hp = this.hpMax;
		this.rage = false;

		this.body.getFixtureList().get(0).setSensor(false);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		if (Dungeon.depth != -3) {
			this.tp(0, 0);
		}

		this.rage = reader.readBoolean();
		this.pickedKey = reader.readBoolean();
		boolean def = reader.readBoolean();
		deathDepth = reader.readInt16();

		if (deathDepth != Dungeon.depth) {
			restore();
		}

		if (def && !this.pickedKey) {
			this.rage = true;
			this.become("defeated");
		} if (this.rage && this.pickedKey) {
			this.ignoreRooms = true;
			this.attackTp = true;
			this.findStartPoint();
			this.rage = true;
			this.dead = false;
			this.deathDepth = Dungeon.depth;
			this.done = false;
			this.unhittable = true;
			this.ignoreRooms = true;
			this.become("appear");
		}
	}

	@Override
	public void become(String state) {
		if (this.state.equals("defeated") && !state.equals("appear")) {
			return;
		}

		super.become(state);
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeBoolean(this.rage);
		writer.writeBoolean(this.pickedKey);
		writer.writeBoolean(this.state.equals("defeated"));
		writer.writeInt16((short) deathDepth);
	}

	private int deathDepth;
	private float lastExpl;

	@Override
	public void update(float dt) {
		if (this.dest) {
			this.invt -= dt;
			lastExpl += dt;

			if (lastExpl >= 0.2f) {
				lastExpl = 0;
				Dungeon.area.add(new Explosion(this.x + this.w / 2 + Random.newFloat(this.w * 2) - this.w, this.y + this.h / 2 + Random.newFloat(this.h * 2) - this.h));
				this.playSfx("explosion");
			}

			FadeFx ft = new FadeFx();
			ft.x = x + w / 2;
			ft.y = y + h / 2;
			float fr = 120;
			float an = Random.newFloat((float) (Math.PI * 2));
			ft.vel = new Point((float) Math.cos(an) * fr, (float) Math.sin(an) * fr);
			Dungeon.area.add(ft);

			Camera.shake(10);

			if (this.invt <= 0) {
				ArrayList<Item> items = new ArrayList<>();
				items.add(new BurningKey());
				items.add(Chest.generate(ItemRegistry.Quality.IRON_PLUS, Random.chance(50)));

				for (Item item : items) {
					ItemHolder holder = new ItemHolder(item);

					holder.x = this.x;
					holder.y = this.y;
					holder.getItem().generate();

					this.area.add(holder);

					Camera.follow(holder, false);
					LevelSave.add(holder);
				}

				Tween.to(new Tween.Task(0, 2f) {
					@Override
					public void onEnd() {
						Camera.follow(Player.instance, false);
					}
				});

				this.invt = 0;

				this.become("defeated");
				this.dest = false;

				Player.instance.setUnhittable(false);

				Camera.shake(30);
				Audio.highPriority("Reckless");
			}

			return;
		}

		this.activityTimer += dt;
		this.time += dt;

		if (this.acceleration.len() > 9.9f) {
			this.flipped = this.acceleration.x < 0;
		} else {
			if (this.velocity.x < 0) {
				this.flipped = true;
			} else if (this.velocity.x > 0) {
				this.flipped = false;
			}
		}

		if (!this.state.equals("defeated")) {
			super.update(dt);
		}

		this.light.setPosition(x + this.w / 2, y + this.h / 2);

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

		this.sword.update(dt * speedMod);

		if (this.onScreen) {
			float dx = this.x + 8 - Player.instance.x;
			float dy = this.y + 8 - Player.instance.y;
			float d = (float) Math.sqrt(dx * dx + dy * dy);

			sfx.setVolume(sid, this.state.equals("unactive") ? 0 : Settings.sfx * MathUtils.clamp(0, 1, (100 - d) / 100f));
		} else {
			sfx.setVolume(sid, 0);
		}

		this.t += dt * speedMod;

		if (this.target == null) {
			this.assignTarget();
		} else {
			this.flipped = this.target.x + this.target.w / 2 < this.x + this.w / 2;
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
		vertexShader = Gdx.files.internal("shaders/default.vert").readString();
		fragmentShader = Gdx.files.internal("shaders/bk.frag").readString();
		shader = new ShaderProgram(vertexShader, fragmentShader);
		if (!shader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());
	}

	private AnimationData anim;

	@Override
	public void render() {
		if (this.state.equals("unactive") || this.state.equals("defeated")) {
			return;
		}

		if (this.invt > 0) {
			this.animation = hurt;
		} else {
			this.animation = anim;
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

			if (!Dungeon.game.getState().isPaused()) {
				point.s = Math.max(0, point.s - dt * 0.8f);
			}

			TextureRegion r = this.idle.getFrames().get(Math.min(1, point.frame)).frame;
			Graphics.render(r, point.x + this.w / 2, point.y + this.h / 2, 0, r.getRegionWidth() / 2, r.getRegionHeight() / 2, false, false, point.flipped ? -s : s, s);
		}

		Graphics.batch.end();
		shader.begin();

		Texture texture = region.getTexture();

		shader.setUniformf("time", this.time);
		shader.setUniformf("a", this.a);
		shader.setUniformf("white", this.invt > 0.2f ? 1f : 0f);
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

		Graphics.batch.setColor(1, 1, 1, 1);
		// this.sword.render(this.x, this.y, this.w, this.h, this.flipped);

		super.renderStats();
		// Graphics.print(this.state, Graphics.small, x, y);
	}

	private float time;
	private float lastFrame;

	private ArrayList<Frame> frames = new ArrayList<>();

	private static class Frame extends Point {
		public boolean flipped;
		public float s = 1f;
		public int frame;
	}

	private float activityTimer;

	public boolean isActiveState() {
		return this.activityTimer % 50 <= 30;
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x + this.w / 4f, this.y, this.w / 2f, this.h / 2f, 5f, this.a);
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
				if (this.flyTo(this.roomToVisit, self.speed * 5, 32f)) {
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

			if (!self.rage) {
				self.become("preattack");
			} else if (this.flyTo(self.lastSeen, 20, 128f)) {
				self.become("rangedAttack");
				return;
			}

			super.update(dt);
		}
	}

	@Override
	protected ArrayList<Item> getDrops() {
		ArrayList<Item> items = super.getDrops();

		items.add(new BKSword());

		return items;
	}

	private static final float ATTACK_DISTANCE = 32;
	private static final float RANGED_ATTACK_DISTANCE = 132;
	private static final float TP_DISTANCE = 140;

	@Override
	public void destroy() {
		super.destroy();

		sfx.stop(this.sid);
		sword.destroy();
		World.removeLight(light);
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
			if (self.t >= this.delay) {
				self.become("chase");
				return;
			}

			super.update(dt);
		}
	}

	private int lastAttack;
	private int pattern = -1;

	public class PreattackState extends BKState {
		@Override
		public void onEnter() {
			super.onEnter();

			if (pattern == -1) {
				pattern = Random.newInt(3);
			}
		}

		@Override
		public void update(float dt) {
			if (this.t >= 5f) {
				int i = lastAttack % 3;

				if (self.pattern == 0) {
					if (i == 0) {
						self.become("laserAttack");
					} else if (i == 1) {
						self.become("missileAttack");
					} else {
						self.become("laserAimAttack");
						pattern = Random.newInt(3);
					}
				} else if (self.pattern == 1) {
					if (i == 0) {
						self.become("autoAttack");
					} else if (i == 1) {
						self.become("missileAttack");
					} else {
						self.become("spawnAttack");
						pattern = Random.newInt(3);
					}
				} else if (self.pattern == 2) {
					if (i == 0) {
						self.become("autoAttack");
					} else if (i == 1) {
						self.become("spawnAttack");
					} else {
						self.become("laserAimAttack");
						pattern = Random.newInt(3);
					}
				}

				lastAttack++;

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
				sword.use();
				this.attacked = true;
			} else if (sword.getDelay() == 0) {
				if (Random.chance(30)) {
					float a = Random.newFloat(0, (float) (Math.PI * 2));
					float d = 128;
					Tween.to(new Tween.Task(0, 0.5f) {
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
							self.tp((float) Math.cos(a) * d + Player.instance.x - Player.instance.w / 2 + self.w / 2,
								(float) Math.sin(a) * d + Player.instance.y - Player.instance.h / 2 + self.h / 2);

							Tween.to(new Tween.Task(1, 0.5f) {
								@Override
								public float getValue() {
									return self.a;
								}

								@Override
								public void setValue(float value) {
									self.a = value;
								}
							});
						}
					});
				} else {
					self.become("chase");
				}
			}

			super.update(dt);
		}
	}

	public class AppearState extends BKState {
		@Override
		public void onEnter() {
			self.attackTp = true;
			self.findStartPoint();
			self.setUnhittable(true);
			self.rage = true;
			self.body.getFixtureList().get(0).setSensor(true);
			self.knockback.x = 0;
			self.knockback.y = 0;
			self.velocity.x = 0;
			self.velocity.y = 0;

			self.hp = 1;
			self.ignoreRooms = true;
			Lamp.play();

			// fixme: this is broken
			/*Vector3 vec = Camera.game.project(new Vector3(x + w / 2, y + h / 2, 0));
			vec = Camera.ui.unproject(vec);
			vec.y = Display.UI_HEIGHT - vec.y;

			Dungeon.shockTime = 0;
			Dungeon.shockPos.x = (vec.x) / Display.UI_WIDTH;
			Dungeon.shockPos.y = (vec.y) / Display.UI_HEIGHT;*/

			for (int i = 0; i < 30; i++) {
				CurseFx fx = new CurseFx();

				fx.x = x + w / 2 + Random.newFloat(-w, w);
				fx.y = y + h / 2 + Random.newFloat(-h, h);

				Dungeon.area.add(fx);
			}

			Camera.follow(self, false);
			playSfx("explosion");
			self.a = 0;

			Tween.to(new Tween.Task(0.6f, 2f) {
				@Override
				public float getValue() {
					return self.a;
				}

				@Override
				public void setValue(float value) {
					self.a = value;
				}
			});
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			Camera.shake(6);

			if (this.t < 1.6f) {
				FadeFx ft = new FadeFx();
				float an = Random.newFloat((float) (Math.PI * 2));
				ft.x = (float) (x + w / 2 + Math.cos(an) * 48);
				ft.y = (float) (y + h / 2 + Math.sin(an) * 48);
				ft.to = true;
				float fr = 150;
				ft.vel = new Point((float) Math.cos(an - Math.PI) * fr, (float) Math.sin(an - Math.PI) * fr);
				Dungeon.area.add(ft);
			} else if (self.a == 0.6f) {
				Camera.follow(Player.instance, false);
				self.become("chase");
			}
		}
	}

	public class FadeInState extends BKState {
		@Override
		public void onEnter() {
			self.a = 0;
			Camera.follow(self, false);

			Tween.to(new Tween.Task(self.rage ? 0.6f : 1f, 2f) {
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
					self.become("chase");
					self.attackTp = false;
					Camera.follow(Player.instance, false);
				}
			});
		}

		@Override
		public void update(float dt) {
			super.update(dt);
			Camera.shake(6);
			if (t < 1.6f) {
				FadeFx ft = new FadeFx();
				float an = Random.newFloat((float) (Math.PI * 2));
				ft.x = (float) (x + w / 2 + Math.cos(an) * 48);
				ft.y = (float) (y + h / 2 + Math.sin(an) * 48);
				ft.to = true;
				float fr = 150;
				ft.vel = new Point((float) Math.cos(an - Math.PI) * fr, (float) Math.sin(an - Math.PI) * fr);
				Dungeon.area.add(ft);
			}
		}
	}

	public boolean attackTp = false;

	public class FadeOutState extends BKState {
		@Override
		public void onEnter() {
			super.onEnter();

			Tween.to(new Tween.Task(0, 2f) {
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

		@Override
		public void update(float dt) {
			super.update(dt);
			Camera.shake(6);

			if (t < 1.6f) {
				FadeFx ft = new FadeFx();
				ft.x = x + w / 2;
				ft.y = y + h / 2;
				float fr = 70;
				float an = Random.newFloat((float) (Math.PI * 2));
				ft.vel = new Point((float) Math.cos(an) * fr, (float) Math.sin(an) * fr);
				Dungeon.area.add(ft);
			}
		}
	}

	public static Dialog dialogs = Dialog.make("burning-knight");
	public static DialogData itsYouAgain = dialogs.get("its_you_again");
	public static DialogData justDie = dialogs.get("just_die");
	public static DialogData noPoint = dialogs.get("it_is_pointless");
	public DialogData dialog;

	public class DialogState extends BKState {
		@Override
		public void onEnter() {
			super.onEnter();

			Dialog.active = self.dialog;
			Dialog.active.start();

			Camera.follow(self, false);

			Dialog.active.onEnd(new Runnable() {
				@Override
				public void run() {
					Camera.follow(Player.instance, false);
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

			if (!Player.instance.isDead()) {
				UiBanner banner = new UiBanner();
				banner.text = Locale.get("burning_knight");
				Dungeon.ui.add(banner);
			}
		}
	}

	@Override
	protected void die(boolean force) {
		super.die(force);

		instance = null;
		this.done = true;
		GameSave.defeatedBK = true;
		Camera.shake(10);

		deathEffect(killed);
		PlayerSave.remove(this);

		Achievements.unlock(Achievements.KILL_BK);
	}

	public class UnactiveState extends BKState {
		@Override
		public void onEnter() {
			super.onEnter();

			self.a = 0;
			self.setUnhittable(true);
			self.tp(-100, -100);
			Mob.every.remove(self);
		}

		@Override
		public void onExit() {
			super.onExit();
			self.setUnhittable(false);
			Mob.every.add(self);

			if (!Player.instance.isDead()) {
				UiBanner banner = new UiBanner();
				banner.text = Locale.get("burning_knight");
				Dungeon.ui.add(banner);
			}
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (self.t >= 0.1f && ((Dungeon.depth > -1 && Player.instance.room instanceof BossRoom) || (Dungeon.depth == -3 && Player.instance.room == self.room))) {
				Log.info("BK is out");

				self.setUnhittable(false);
				self.tp((Player.instance.room.left + Player.instance.room.getWidth() / 2) * 16,
					(Player.instance.room.top + Player.instance.room.getHeight() / 2) * 16);
				self.become("fadeIn");
				self.a = 0;

				Camera.follow(self, false);
				Camera.shake(10);
				Lamp.play();

				if (Dungeon.depth == -3) {
					self.modifyHpMax(1000);
					self.modifyHp(1000, null);
				}
			}
		}
	}

	@Override
	protected State getAi(String state) {
		switch (state) {
			case "idle": return new IdleState();
			case "appear": return new AppearState();
			case "roam": return new RoamState();
			case "alerted": return new AlertedState();
			case "chase": case "fleeing": return new ChaseState();
			case "dash": return new DashState();
			case "preattack": return new PreattackState();
			case "attack": return new AttackState();
			case "fadeIn": return new FadeInState();
			case "fadeOut": return new FadeOutState();
			case "dialog": return new DialogState();
			case "wait": return new WaitState();
			case "unactive": return new UnactiveState();
			case "missileAttack": return new MissileAttackState();
			case "autoAttack": return new NewAttackState();
			case "laserAttack": return new LaserAttackState();
			case "laserAimAttack": return new LaserAimAttackState();
			case "await": return new AwaitState();
			case "defeated": return new DefeatedState();
			case "spawnAttack": return new SpawnAttack();
			case "rangedAttack": return new RangedAttackState();
		}

		return super.getAi(state);
	}

	public class SpawnAttack extends BKState {
		@Override
		public void onEnter() {
			super.onEnter();
			center = self.room.getCenter();
			center.x *= 16;
			center.y *= 16;
		}

		private Point center;
		private int num;
		private boolean reached;

		@Override
		public void update(float dt) {
			super.update(dt);

			if (!reached) {
				if (this.flyTo(center, 15f, 16f)) {
					reached = true;
					this.t = 0;
				}

				return;
			}

			float x = self.x + self.w / 2;
			float y = self.y + self.h / 2;

			if (num < 4 && this.t > num * 0.5f + 1f) {
				Mob mob = MobPool.instance.generate();

				if (mob == null) {
					MobPool.instance.initForFloor();
					mob = MobPool.instance.generate();
				}

				double a = Math.PI * 2 * (num * 0.25f);

				mob.x = (float) (Math.cos(a) * 96) + x;
				mob.y = (float) (Math.sin(a) * 96) + y;
				mob.noDrop = true;

				mob.poof();
				Dungeon.area.add(mob);

				num++;
				return;
			}

			if (num == 4) {
				for (Mob mob : Mob.all) {
					if (mob.room == self.room) {
						return;
					}
				}

				self.become("chase");
			}
		}
	}

	public class DefeatedState extends BKState {
		@Override
		public void onEnter() {
			self.dead = false;
			self.deathDepth = Dungeon.depth;
			self.done = false;
			self.hp = 1;
			self.rage = true;
			self.unhittable = true;
			self.ignoreRooms = true;
			self.pickedKey = false;
		}

		@Override
		public void onExit() {
			Lamp.play();

			Tween.to(new Tween.Task(0.6f, 0.3f) {
				@Override
				public float getValue() {
					return a;
				}

				@Override
				public void setValue(float value) {
					a = value;
				}
			});
		}
	}

	public class AwaitState extends BKState {
		@Override
		public void update(float dt) {
			super.update(dt);

			if (Player.instance == null || Player.instance.isDead() || !(Player.instance.room instanceof ShopRoom)) {
				self.become("idle");
			}
		}
	}

	public class MissileAttackState extends BKState {
		private int num;

		@Override
		public void onEnter() {
			super.onEnter();

			self.lookUp.setFrame(0);
			self.lookUp.setPaused(false);
			self.anim = self.lookUp;
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (num < 7) {
				if (this.t >= 2f) {
					num++;

					if (this.num == 7) {

					} else if (num == 6) {
						self.lookDown.setFrame(0);
						self.lookDown.setPaused(false);
						self.anim = self.lookDown;

						return;
					}

					this.t = 0;

					MissileProjectile missile = new MissileProjectile();
					missile.to = Player.instance;
					missile.bad = true;
					missile.owner = self;
					missile.x = self.x + self.w / 2 + (self.flipped ? -4 : 4);
					missile.y = self.y + self.h - 16;
					Dungeon.area.add(missile);

					MissileAppear appear = new MissileAppear();
					appear.missile = missile;
					Dungeon.area.add(appear);
				}
			} else if (self.lookDown.isPaused()) {
				self.anim = self.idle;
				self.become("preattack");
			}
		}
	}

	public class NewAttackState extends BKState {
		private int num;

		@Override
		public void update(float dt) {
			super.update(dt);

			if (t >= 3f) {
				if (num == 5) {
					self.become("preattack");
					return;
				}

				t = 0;
				num ++;

				BulletProjectile ball = new BulletProjectile() {
					@Override
					protected void onDeath() {
						super.onDeath();

						if (!brokeWeapon) {
							for (int i = 0; i < 8; i++) {
								BulletProjectile ball = new BulletProjectile();

								float a = (float) (i * Math.PI / 4);
								ball.velocity = new Point((float) Math.cos(a) / 2f, (float) Math.sin(a) / 2f).mul(40f * Mob.shotSpeedMod);

								ball.x = (this.x);
								ball.y = (this.y);
								ball.damage = 2;
								ball.bad = true;

								ball.letter = "bullet-nano";
								Dungeon.area.add(ball);
							}
						}
					}
				};

				ball.depth = 17;
				float a = self.getAngleTo(self.target.x + 8, self.target.y + 8);
				ball.velocity = new Point((float) Math.cos(a) / 2f, (float) Math.sin(a) / 2f).mul(40f * Mob.shotSpeedMod);
				ball.x = (float) (self.x + self.w / 2);
				ball.y = (float) (self.y + self.h - 8);
				ball.damage = 2;
				ball.bad = true;
				ball.auto = true;
				ball.delay = 1f;
				ball.alp = 0;

				Tween.to(new Tween.Task(1, 1) {
					@Override
					public float getValue() {
						return ball.alp;
					}

					@Override
					public void setValue(float value) {
						ball.alp = value;
					}
				});

				ball.letter = "bullet-skull";
				Dungeon.area.add(ball);
			}
		}
	}

	public class LaserAttackState extends BKState {
		private Laser laser;
		private float last;
		private int num;

		@Override
		public void update(float dt) {
			super.update(dt);

			float x = self.x + self.w / 2;
			float y = self.y + self.h + self.z - 12;

			if (laser != null) {
				laser.x = x;
				laser.y = y;
			}

			if (last <= 0) {
				last = 3f;
				t = 0;

				laser = new Laser();
				double an = self.getAngleTo(self.getAim().x, self.getAim().y);

				laser.fake = true;
				laser.x = x;
				laser.y = y;
				laser.a = (float) Math.toDegrees(an - Math.PI / 2);

				laser.huge = false;

				laser.w = 32f;
				laser.bad = true;
				laser.damage = 2;
				laser.depth = 17;
				laser.owner = self;

				Dungeon.area.add(laser);
			} else if (t >= 2.3f && !laser.removing) {
				laser.remove();
				num ++;
			} else if (laser.al == 1) {
				laser.huge = true;
				laser.fake = false;
			}

			if (t >= 3f && num == 3) {
				self.become("chase");
			}

			last -= dt;
		}

		@Override
		public void onExit() {
			super.onExit();

			if (!laser.dead) { // For example on death
				laser.remove();
			}
		}
	}

	public class LaserAimAttackState extends BKState {
		private Laser laser;

		@Override
		public void onEnter() {
			laser = new Laser();
			float x = self.x + self.w / 2;
			float y = self.y + self.h + self.z - 12;
			double an = self.getAngleTo(self.getAim().x, self.getAim().y);

			laser.fake = true;
			laser.x = x;
			laser.y = y;
			laser.a = (float) Math.toDegrees(an - Math.PI / 2) + (Random.chance(50) ? 10 : -10);

			laser.huge = false;

			laser.w = 32f;
			laser.bad = true;
			laser.damage = 2;
			laser.owner = self;

			Dungeon.area.add(laser);
		}

		@Override
		public void update(float dt) {
			super.update(dt);

			if (this.t > 2 || this.laser.al == 1) {
				laser.huge = true;
				laser.fake = false;

				float x = self.x + self.w / 2;
				float y = self.y + self.h + self.z - 12;
				double an = Math.toDegrees(self.getAngleTo(self.getAim().x, self.getAim().y) - Math.PI / 2);

				laser.x = x;
				laser.y = y;
				laser.depth = 17;

				float v = (float) (an - laser.a);
				float f = 64 * (Math.abs(v) > Math.PI ? 3 : 1);

				if (Gun.shortAngleDist((float) Math.toRadians(laser.a), (float) Math.toRadians(an)) > 0) {
					aVel += dt * f;
				} else {
					aVel -= dt * f;
				}

				laser.a += aVel * dt;
				aVel -= aVel * dt;
				laser.recalc();

				if (this.t >= 10) {
					if (!this.removed) {
						removed = true;
						laser.remove();
					} else if (this.laser.dead) {
						self.become("chase");
					}
				}
			}
		}

		@Override
		public void onExit() {
			super.onExit();

			if (!laser.dead) { // For example on death
				laser.remove();
			}
		}

		private float aVel;
		private boolean removed;
	}

	public class RangedAttackState extends BKState {
		private int count;
		private ArrayList<FireballProjectile> balls = new ArrayList<>();
		private boolean fast;
		private boolean auto;
		private boolean roll;
		private boolean swing;

		@Override
		public void onEnter() {
			super.onEnter();
			/*swing = Random.chance(10);

			if (swing) {
				left = Random.chance(50);
				return;
			}

			roll = Random.chance(10);

			if (roll) {
				return;
			}*/

			fast = Random.chance(50);
			count = Random.newInt(2, 8);
			// auto = Random.chance(30);
		}

		@Override
		public void onExit() {
			if (roll || swing) {
				return;
			}

			for (FireballProjectile ball : balls) {
				ball.done = true;
			}

			balls.clear();
		}

		private float speed = 1;
		private float tt;
		private boolean did;
		private int i;
		private boolean left;

		@Override
		public void update(float dt) {
			super.update(dt);

			if (swing) {
				if (i < 64 && self.t >= i * 0.1f + 1f) {
					float s = 30;
					int c = 32;

					if (i % 3 == 0) {
						self.playSfx("gun_machinegun");
					}

					BulletProjectile ball = new BulletProjectile();
					ball.x = self.x + self.w / 2;
					ball.y = self.y + self.h / 2;
					ball.bad = true;
					ball.owner = self;
					ball.letter = "bullet-nano";

					double a = ((float) i) / c * Math.PI * 2 + tt;

					if (!left) {
						a *= -1;
					}

					ball.velocity = new Point();
					ball.velocity.x = (float) (s * Math.cos(a) * Mob.shotSpeedMod);
					ball.velocity.y = (float) (s * Math.sin(a) * Mob.shotSpeedMod);

					Dungeon.area.add(ball);
					i++;
				}

				if (self.t >= 10f) {
					self.become("chase");
				}

				return;
			} else if (roll) {
				if (!did && self.t >= 2f) {
					float s = 30;
					int c = 32;

					self.playSfx("gun_machinegun");

					for (int i = 0; i < c; i++) {
						BulletProjectile ball = new BulletProjectile();
						ball.x = self.x + self.w / 2;
						ball.y = self.y + self.h / 2;
						ball.bad = true;
						ball.owner = self;
						ball.letter = "bullet-nano";

						double a = ((float) i) / c * Math.PI * 2 + tt;

						ball.velocity = new Point();
						ball.velocity.x = (float) (s * Math.cos(a) * Mob.shotSpeedMod);
						ball.velocity.y = (float) (s * Math.sin(a) * Mob.shotSpeedMod);

						Dungeon.area.add(ball);
					}

					did = true;
				}

				if (self.t >= 6f) {
					self.become("chase");
				}

				return;
			}

			speed = Math.min(speed + dt * 10, 10);
			tt += speed * dt;

			float r = 24;

			for (int i = 0; i < balls.size(); i++) {
				FireballProjectile ball = balls.get(i);
				double a = ((float) i) / count * Math.PI * 2 + tt;

				ball.setPos(
					self.x + self.w / 2 + (float) Math.cos(a) * r,
					self.y + self.h / 2 + (float) Math.sin(a) * r
				);
			}

			if (sec) {
				if (this.wait > 0) {
					this.wait -= dt;
					return;
				}

				if (fast) {
					boolean allDir = Random.chance(50);
					double a = (self.getAngleTo(self.target.x + self.target.w / 2, self.target.y + self.target.h / 2));
					float s = 200;

					for (int i = 0; i < balls.size(); i++) {
						FireballProjectile ball = balls.get(i);

						if (allDir) {
							a = ((float) i) / count * Math.PI * 2 + tt;
						}

						if (ball.done) {
							ball.destroy();
						} else {
							ball.tar = new Point();
							ball.tar.x = (float) (s * Math.cos(a));
							ball.tar.y = (float) (s * Math.sin(a));
							ball.target = auto ? self.target : null;
						}
					}

					this.balls.clear();
					self.become("chase");
				} else {
					if (this.balls.size() == 0) {
						self.become("chase");
						return;
					}

					float t = (self.getAngleTo(self.target.x + self.target.w / 2, self.target.y + self.target.h / 2));
					float an = (float) ((((float) (this.balls.size() - 1)) / count * Math.PI * 2 + Dungeon.time * 10) % (Math.PI * 2));

					if (Math.abs(t - an) % (Math.PI * 2) < Math.PI / 16) {
						FireballProjectile ball = this.balls.get(this.balls.size() - 1);
						this.balls.remove(this.balls.size() - 1);

						if (ball.done) {
							ball.destroy();
						} else {
							float a = (float) (t + Math.toRadians(Random.newFloat(-5, 5)));
							float s = 200;

							ball.tar = new Point();
							ball.tar.x = (float) (s * Math.cos(a));
							ball.tar.y = (float) (s * Math.sin(a));
							ball.target = auto ? self.target : null;
						}
					}
				}
			} else if (this.t >= (self.rage ? balls.size() * 0.25f + 0.25f : balls.size() * 0.5f + 1f)) {
				FireballProjectile ball = new FireballProjectile();

				ball.owner = self;

				Dungeon.area.add(ball);
				balls.add(ball);

				if (balls.size() == count) {
					this.t = 0;
					this.wait = self.rage ? 0.5f : 1.5f;
					this.sec = true;
				}
			}
		}

		private float wait;
		private boolean sec;
	}

	private void checkForTarget() {
		for (Creature player : (this.stupid ? Mob.all : Player.all)) {
			if (player.invisible || player == this) {
				continue;
			}

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