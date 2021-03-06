package org.rexcellentgames.burningknight.entity.creature.mob;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Settings;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.Buff;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.creature.buff.FrozenBuff;
import org.rexcellentgames.burningknight.entity.creature.buff.PoisonedBuff;
import org.rexcellentgames.burningknight.entity.creature.fx.HeartFx;
import org.rexcellentgames.burningknight.entity.creature.fx.HpFx;
import org.rexcellentgames.burningknight.entity.creature.mob.boss.Boss;
import org.rexcellentgames.burningknight.entity.creature.mob.boss.BurningKnight;
import org.rexcellentgames.burningknight.entity.creature.mob.common.Fly;
import org.rexcellentgames.burningknight.entity.creature.mob.ice.IceElemental;
import org.rexcellentgames.burningknight.entity.creature.mob.prefix.Prefix;
import org.rexcellentgames.burningknight.entity.creature.npc.Npc;
import org.rexcellentgames.burningknight.entity.creature.npc.Shopkeeper;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.fx.BloodDropFx;
import org.rexcellentgames.burningknight.entity.fx.BloodSplatFx;
import org.rexcellentgames.burningknight.entity.item.Gold;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.key.KeyC;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.entities.Door;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Chest;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Mimic;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.save.GameSave;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.entity.pool.PrefixPool;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.util.*;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;
import org.rexcellentgames.burningknight.util.geometry.Point;

import java.io.IOException;
import java.util.ArrayList;

public class Mob extends Creature {
	public static int maxId;
	private int id;

	public Point lastSeen;
	public static boolean challenge;
	public static float speedMod = 1f;
	public static float shotSpeedMod = 1f;
	public Creature target;
	public static ArrayList<Mob> all = new ArrayList<>();
	public static ArrayList<Mob> every = new ArrayList<>();
	public ArrayList<Player> colliding = new ArrayList<>();
	protected boolean drop;
	protected State ai;
	public boolean stupid = false;
	protected Prefix prefix;
	public boolean noLoot;

	private static TextureRegion hideSign = Graphics.getTexture("ui-hide");
	private static TextureRegion noticeSign = Graphics.getTexture("ui-notice");
	public float noticeSignT;
	public float hideSignT;
	public boolean nodebuffs;

	protected float sx = 1;
	protected float sy = 1;

	@Override
	protected boolean canHaveBuff(Buff buff) {
		if (nodebuffs && (buff instanceof FrozenBuff || buff instanceof BurningBuff || buff instanceof PoisonedBuff)) {
			return false;
		}

		return super.canHaveBuff(buff);
	}

	{
		alwaysActive = true;
	}

	@Override
	public void init() {
		super.init();

		id = maxId;
		maxId ++;

		if (!(this instanceof BurningKnight) && !(this instanceof Npc) && !(this instanceof Mimic) && !(this instanceof IceElemental)) {
			all.add(this);
		}

		every.add(this);

		if (Random.chance(50)) {
			this.become("roam");
		}
	}

	public Mob generate() {
		if (challenge) { //  || Random.chance(10)
			this.generatePrefix();
		}

		this.hp = (int) (this.hpMax * 0.75f);

		return this;
	}

	public void renderStats() {
		if (Player.showStats) {
			Graphics.print(this.hp + "/" + this.hpMax, Graphics.small, this.x, this.y + this.z);
		}
	}

	public void renderSigns() {
		TextureRegion region = // this.hideSignT > 0 ? hideSign :
			(this.noticeSignT > 0 ? noticeSign : null);

		if (region != null) {
			float t = Math.max(this.hideSignT, this.noticeSignT);

			if (t <= 0.2f) {
				Graphics.batch.setColor(1, 1, 1, t * 5);
			} else if (t >= 1.8f) {
				Graphics.batch.setColor(1, 1, 1, (t - 1.8f) * 5);
			}

			Graphics.render(region,
				this.x + (this.w - region.getRegionWidth()) / 2 + region.getRegionWidth() / 2,
				(float) (this.y + this.h + 2 + Math.cos(t * 5) * 5.5f + region.getRegionHeight() / 2),
				(float) (Math.cos(t * 4) * 30f), region.getRegionWidth() / 2, region.getRegionHeight() / 2, false, false);

			Graphics.batch.setColor(1, 1, 1, 1);
		}
	}

	public void generatePrefix() {
		if (this.prefix == null && Dungeon.depth != -3) {
			this.prefix = PrefixPool.instance.generate();
			this.prefix.apply(this);
			this.prefix.onGenerate(this);
		}
	}

	public static ShaderProgram shader;
	public static ShaderProgram frozen;

	static {
		shader = new ShaderProgram(Gdx.files.internal("shaders/default.vert").readString(), Gdx.files.internal("shaders/outline.frag").readString());
		if (!shader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());
		frozen = new ShaderProgram(Gdx.files.internal("shaders/default.vert").readString(), Gdx.files.internal("shaders/ice.frag").readString());
		if (!frozen.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + frozen.getLog());
	}

	public float getOx() {
		return w / 2;
	}

	public float getOy() {
		return 0;
	}

	public boolean flippedVert;

	public void renderWithOutline(AnimationData data) {
		TextureRegion region = data.getCurrent().frame;
		float w = region.getRegionWidth();

		if (this.prefix != null) {
			Color color = this.prefix.getColor();

			Graphics.batch.end();
			shader.begin();
			shader.setUniformf("u_a", 1f);
			shader.setUniformf("u_color", new Vector3(color.r, color.g, color.b));
			shader.end();
			Graphics.batch.setShader(shader);
			Graphics.batch.begin();

			for (int xx = -1; xx < 2; xx++) {
				for (int yy = -1; yy < 2; yy++) {
					if (Math.abs(xx) + Math.abs(yy) == 1) {
						Graphics.render(region, x + xx + w / 2, y + z + yy + getOy(), 0, getOx(), getOy(), false, false, sx * (flipped ? -1f : 1f), sy * (flippedVert ? -1 : 1));
					}
				}
			}

			Graphics.batch.end();
			Graphics.batch.setShader(null);
			Graphics.batch.begin();
		}

		if (this.fa > 0) {
			Graphics.batch.end();
			frozen.begin();
			frozen.setUniformf("time", Dungeon.time);
			frozen.setUniformf("f", this.fa);
			frozen.setUniformf("a", this.a);
			frozen.setUniformf("freezed", this.wasFreezed ? 1f : 0f);
			frozen.setUniformf("poisoned", this.wasPoisoned ? 1f : 0f);
			frozen.end();
			Graphics.batch.setShader(frozen);
			Graphics.batch.begin();
		}

		Graphics.batch.setColor(1, 1, 1, this.a);
		Graphics.render(region, Math.round(x + w / 2), Math.round(y + z) + getOy(), 0, getOx(), getOy(), false, false, sx * (flipped ? -1 : 1), sy * (flippedVert ? -1 : 1));

		if (this.freezed || this.poisoned) {
			this.fa += (1 - this.fa) * Gdx.graphics.getDeltaTime() * 3f;

			this.wasFreezed = this.freezed;
			this.wasPoisoned = this.poisoned;
		} else {
			this.fa += (0 - this.fa) * Gdx.graphics.getDeltaTime() * 3f;

			if (this.fa <= 0) {
				this.wasFreezed = false;
				this.wasPoisoned = false;
			}
		}

		if (this.fa > 0) {
			Graphics.batch.end();
			Graphics.batch.setShader(null);
			Graphics.batch.begin();
		}
	}

	private float fa;
	private boolean wasFreezed;
	private boolean wasPoisoned;

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		int id = reader.readInt16();

		if (id > 0) {
			this.prefix = PrefixPool.instance.getModifier(id - 1);
			this.prefix.apply(this);
		}

		noLoot = reader.readBoolean();
	}

	protected boolean ignoreRooms;

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		if (this.prefix == null) {
			writer.writeInt16((short) 0);
		} else {
			writer.writeInt16((short) (this.prefix.id + 1));
		}

		writer.writeBoolean(noLoot);
	}

	private float closestFraction = 1.0f;
	private Entity last;

	private RayCastCallback callback = new RayCastCallback() {
		@Override
		public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
			Object data = fixture.getBody().getUserData();

			if (!fixture.isSensor() && data instanceof Entity && !(data instanceof Level || data instanceof ItemHolder || (data instanceof Door && ((Door) data).isOpen()))) {
				if (fraction < closestFraction) {
					closestFraction = fraction;
					last = (Entity) data;
				}
			}

			return closestFraction;
		}
	};

	public boolean canSee(Creature player) {
		return canSee(player, 0);
	}

	public boolean canSee(Creature player, int ignore) {
		if (player == null) {
			return false;
		}

		if (!(this instanceof Shopkeeper || this instanceof BurningKnight)) {
			if (player.room != this.room) {
				return false;
			}
		}

		float x = this.x + this.w / 2;
		float y = this.y + this.h / 2;
		float x2 = player.x + player.w / 2;
		float y2 = player.y + player.h / 2;

		return Dungeon.level.canSee((int) Math.floor(x / 16), (int) Math.floor(y / 16), (int) Math.floor(x2 / 16), (int) Math.floor(y2 / 16), ignore) == 0;
	}

	public boolean canSeePoint(Point player) {
		if (player == null) {
			return false;
		}

		float x = this.x + this.w / 2;
		float y = this.y + this.h / 2;
		float x2 = player.x;
		float y2 = player.y;

		return Dungeon.level.canSee((int) Math.floor(x / 16), (int) Math.floor(y / 16), (int) Math.floor(x2 / 16), (int) Math.floor(y2 / 16), 0) == 0;
	}

	public Point getCloser(Point target) {
		int from = (int) (Math.floor((this.x + this.w / 2) / 16) + Math.floor((this.y + 12) / 16) * Level.getWidth());
		int to = (int) (Math.floor((target.x + this.w / 2) / 16) + Math.floor((target.y + 12) / 16) * Level.getWidth());

		if (!Dungeon.level.checkFor(to, Terrain.PASSABLE)) {
			to -= Level.getWidth();
		}

		if (!Dungeon.level.checkFor(from, Terrain.PASSABLE)) {
			from -= Level.getWidth();
		}

		int step = PathFinder.getStep(from, to, Dungeon.level.getPassable());

		if (step != -1) {
			Point p = new Point();

			p.x = step % Level.getWidth() * 16;
			p.y = (float) (Math.floor(step / Level.getWidth()) * 16);

			return p;
		}

		return null;
	}

	public Point getFar(Point target) {
		if (target == null) {
			return null;
		}

		int pos = (int) (Math.floor((target.x + this.w / 2) / 16) + Math.floor((target.y + 12) / 16) * Level.getWidth());
		int from = (int) (Math.floor((this.x + this.w / 2) / 16) + Math.floor((this.y + 12) / 16) * Level.getWidth());

		if (!Dungeon.level.checkFor(pos, Terrain.PASSABLE)) {
			pos -= Level.getWidth();
		}

		if (!Dungeon.level.checkFor(from, Terrain.PASSABLE)) {
			from -= Level.getWidth();
		}

		PathFinder.getStepBack(from, pos, Dungeon.level.getPassable(), pos);

		if (PathFinder.lastStep != -1) {
			Point p = new Point();

			p.x = PathFinder.lastStep % Level.getWidth() * 16;
			p.y = (float) (Math.floor(PathFinder.lastStep / Level.getWidth()) * 16);

			return p;
		}

		return null;
	}

	public float getWeight() {
		return 1;
	}

	private float lastBlood;
	private float lastSplat;

	public boolean isLow() {
		return this.hp != this.hpMax && this.hp <= Math.ceil(((float) this.hpMax) / 4) && !(this.state.equals("unactive"));
	}

	@Override
	public void update(float dt) {
		super.update(dt * speedMod);

		this.noticeSignT = Math.max(0, this.noticeSignT - dt);
		this.hideSignT = Math.max(0, this.hideSignT - dt);

		if (this.isLow()) {
			this.lastSplat += dt;
			this.lastBlood += dt;

			if (this.lastBlood > 0.1f) {
				this.lastBlood = 0;

				BloodDropFx fx = new BloodDropFx();

				fx.owner = this;

				Dungeon.area.add(fx);
			}

			if (this.lastSplat >= 1f && Settings.blood) {
				this.lastSplat = 0;
				BloodSplatFx fxx = new BloodSplatFx();

				fxx.x = x + Random.newFloat(w) - 8;
				fxx.y = y + Random.newFloat(h) - 8;

				Dungeon.area.add(fxx);
			}
		}

		if (this.drop) {
			if (Dungeon.depth != -3) {
				if (this.prefix != null) {
					this.prefix.onDeath(this);
				}

				this.drop = false;

				if (!noDrop && !noLoot) {
					ArrayList<Item> items = this.getDrops();

					for (Item item : items) {
						if (Chest.isVeganFine(item.getClass())) {
							ItemHolder holder = new ItemHolder(item);

							if (this instanceof Boss) {
								Point point = room.getCenter();

								holder.x = point.x * 16;
								holder.y = point.y * 16;
							} else {
								holder.x = this.x;
								holder.y = this.y;
							}

							holder.x += Random.newFloat(-4, 4);
							holder.y += Random.newFloat(-4, 4);

							holder.getItem().generate();

							this.area.add(holder);

							LevelSave.add(holder);
							holder.randomVelocity();
						}
					}
				}
			}
		}

		if (this.freezed) {
			this.velocity.x = 0;
			this.velocity.y = 0;
			this.acceleration.x = 0;
			this.acceleration.y = 0;
			this.knockback.x = 0;
			this.knockback.y = 0;

			if (this.body != null) {
				this.body.setTransform(this.x, this.y, 0);
			}

			return;
		}

		if (this.room != null && !this.ignoreRooms) {
			this.room.numEnemies += 1;
		}


		if (this.dead || this.dd) {
			return;
		}

		if (this.ai == null) {
			this.ai = this.getAiWithLow(this.state);

			if (this.ai != null) {
				this.ai.self = this;

				try {
					this.ai.onEnter();
				} catch (RuntimeException e) {
					e.printStackTrace();
					Log.error("AI error in " + this.getClass().getSimpleName());
					this.become("idle");
				}
			}
		}

		/*if (this.room != null && Player.instance.room == this.room) {
			for (Mob mob : Mob.all) {
				if (mob != this && mob.room == this.room) {
					float x = mob.x + mob.w / 2 + mob.velocity.x * dt * 10;
					float y = mob.y + mob.h / 2 + mob.velocity.y * dt * 10;
					float d = this.getDistanceTo(x, y);

					if (d < 10) {
						float a = d <= 1 ? Random.newFloat((float) (Math.PI * 2)) : this.getAngleTo(x, y);
						float f = 500 * dt;

						this.velocity.x -= Math.cos(a) * f;
						this.velocity.y -= Math.sin(a) * f;
					}
				}
			}
		}*/

		if (this.ai != null) {
			try {
				this.ai.update(dt * speedMod);
			} catch (RuntimeException e) {
				e.printStackTrace();
				Log.error("AI error in " + this.getClass().getSimpleName());
				this.become("idle");
			}

			if (this.ai != null) { // !?!?!
				this.ai.t += dt * speedMod;
			}
		}

		this.findTarget(false);

		if (this.target != null && (this.target.invisible || this.target.isDead())) {
			this.target = null;
			this.become("idle");
		}

		if (!this.friendly && !this.dd) {
			for (Player player : colliding) {
				player.modifyHp(-1, this, false);
			}
		}
	}

	public boolean friendly = false;

	@Override
	protected void common() {
		float dt = getDt();
		doVel();

		this.t += dt;
		this.timer += dt;
		this.invt = Math.max(0, this.invt - dt);
		this.invtt = Math.max(0, this.invtt - dt);

		if (!this.dead && !(this instanceof Boss || this instanceof Fly)) {
			if (this.velocity.x < 0) {
				this.flipped = true;
			} else if (this.velocity.x > 0) {
				this.flipped = false;
			}
		}

		if (this.velocity.len2() > 1) {
			this.lastIndex = Dungeon.longTime;
		}
		if (this.body != null) {
			if (ignoreVel) {
				this.x = this.body.getPosition().x;
				this.y = this.body.getPosition().y;
			} else {
				// this.velocity.clamp(0, this.maxSpeed);
				this.body.setTransform(this.x, this.y + this.z, 0);
				this.lz = this.z;
				this.body.setLinearVelocity(this.velocity.x * speedMod + this.knockback.x, this.velocity.y * speedMod + this.knockback.y);
			}
		}
	}

	protected boolean ignoreVel;

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Player) {
			Player player = (Player) entity;

			if (player.isDead()) {
				return;
			}

			this.target = player;

			if (!friendly && !this.state.equals("defeated") && !dead) {
				if (player.getInvt() == 0) {
					player.knockBackFrom(this, 2);
				}

				player.modifyHp(-1, this, false);
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
		ArrayList<Item> items = new ArrayList<>();

		if (Random.chance(30)) {
			Gold gold = new Gold();
			gold.generate();
			items.add(gold);
		}

		if (Random.chance(10)) {
			items.add(new KeyC());
		}

		return items;
	}

	@Override
	public HpFx modifyHp(int amount, Creature from) {
		if (this.dd) {
			return null;
		}

		return super.modifyHp(amount, from);
	}

	protected void deathEffects() {
		this.done = true;
		drop = true;

		if (this.room != null) {
			this.room.numEnemies -= 1;
		}

		all.remove(this);
		every.remove(this);
	}

	public boolean dd;

	@Override
	protected void die(boolean force) {
		if (dd) {
			return;
		}

		Camera.shake(3);

		// super.die(force);

		this.dd = true;
		this.done = false;
		this.velocity.x = 0;
		this.velocity.y = 0;

		Tween.to(new Tween.Task(0.7f, 0.2f) {
			@Override
			public float getValue() {
				return sy;
			}

			@Override
			public void setValue(float value) {
				sy = value;
			}

			@Override
			public void onEnd() {
				Tween.to(new Tween.Task(1.5f, 0.1f) {
					@Override
					public float getValue() {
						return sy;
					}

					@Override
					public void setValue(float value) {
						sy = value;
					}

					@Override
					public void onEnd() {
						dead = true;
						remove = true;
						deathEffects();
						Camera.shake(3);

						if (!force) {
							GameSave.killCount ++;

							if (GameSave.killCount >= 10) {
								Achievements.unlock(Achievements.UNLOCK_BLACK_HEART);
							}

							if (GameSave.killCount >= 100) {
								Achievements.unlock(Achievements.UNLOCK_BLOOD_CROWN);
							}
						}

						if (Player.instance != null && !Player.instance.isDead() && !force && Random.chance(20) && !noLoot) {
							HeartFx fx = new HeartFx();

							fx.x = x + w / 2 + Random.newFloat(-4, 4);
							fx.y = y + h / 2 + Random.newFloat(-4, 4);

							Dungeon.area.add(fx);
							LevelSave.add(fx);

							fx.randomVelocity();
						}
					}
				}).delay(0.2f);

				Tween.to(new Tween.Task(0.5f, 0.1f) {
					@Override
					public float getValue() {
						return sx;
					}

					@Override
					public void setValue(float value) {
						sx = value;
					}
				}).delay(0.2f);
			}
		});

		Tween.to(new Tween.Task(1.3f, 0.2f) {
			@Override
			public float getValue() {
				return sx;
			}

			@Override
			public void setValue(float value) {
				sx = value;
			}
		});
	}

	@Override
	protected float getDt() {
		return super.getDt() * speedMod;
	}

	protected float moveToPoint(float x, float y, float speed) {
		speed *= 0.8f;

		float dx = x - this.x - this.w / 2;
		float dy = y - this.y - this.h / 2;
		float d = (float) Math.sqrt(dx * dx + dy * dy);

		this.acceleration.x += dx / d * speed;
		this.acceleration.y += dy / d * speed;

		return d;
	}

	@Override
	public void become(String state) {
		if (!this.state.equals(state)) {
			if (this.ai != null) {
				try {
					this.ai.onExit();
				} catch (RuntimeException e) {
					e.printStackTrace();
					Log.error("Mob AI exdsception " + this.getClass().getSimpleName());
				}
			}

			this.ai = this.getAiWithLow(state);

			if (this.ai != null) {
				this.ai.self = this;

				try {
					this.ai.onEnter();
				} catch (RuntimeException e) {
					e.printStackTrace();
					Log.error("Mob AI exception " + this.getClass().getSimpleName());
				}
			} else {
				Log.error("'" + state + "' ai is not found for mob " + this.getClass().getSimpleName());
			}
		}

		super.become(state);
	}

	public boolean noDrop;

	public class GetOutState extends State {
		private float delay;

		@Override
		public void onEnter() {
			super.onEnter();
			delay = self.isLow() ? 100000000f : Random.newFloat(3f, 6f);
		}

		@Override
		public void update(float dt) {
			super.update(dt);
			this.checkForPlayer();

			if (self.lastSeen == null) {
				self.become("idle");
				return;
			}

			this.moveFrom(self.lastSeen, 25f, 5f);

			if (this.t >= delay) {
				self.become(getState());
			}
		}

		protected String getState() {
			return "idle";
		}
	}

	protected State getAiWithLow(String state) {
		/*if (this.isLow()) {
			return new GetOutState();
		}*/

		return getAi(state);
	}

	protected State getAi(String state) {
		switch (state) {
			case "getout": return new GetOutState();
		}

		return null;
	}

	public void findTarget(boolean force) {
		if (this.target == null && Dungeon.level != null) {
			for (Creature player : (this.stupid ? Mob.all : Player.all)) {
				if (player.invisible || player == this) {
					continue;
				}

				this.target = player;
			}
		}
	}

	@Override
	protected void onHurt(int a, Entity from) {
		super.onHurt(a, from);

		if (from instanceof Player) {
			this.target = (Creature) from;
		}
	}

	// HACKZ
	@Override
	public boolean rollBlock() {
		return false;// return super.rollBlock();
	}

	@Override
	public Point getAim() {
		if (this.target != null) {
			return new Point(Player.instance.x + 8, Player.instance.y + 8);
		} else {
			return new Point(this.x + this.w, this.y);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		all.remove(this);
		every.remove(this);
	}

	public Room lastRoom;
	public boolean toWater;

	public class State<T extends Mob> {
		public T self;
		public float t;
		public Point nextPathPoint;
		public Point targetPoint;

		public void update(float dt) {
			this.t += dt;
		}

		public void onEnter() {

		}

		public void onExit() {

		}

		public boolean flyTo(Point point, float s, float ds) {
			float dx = point.x - self.x - self.w / 2;
			float dy = point.y - self.y - self.h / 2;
			float d = (float) Math.sqrt(dx * dx + dy * dy);

			if (d < ds) {
				return true;
			}

			self.acceleration.x += dx / d * s;
			self.acceleration.y += dy / d * s;

			return false;
		}

		public boolean moveRightTo(Point point, float s, float d) {
			float ds = self.moveToPoint(point.x + 8, point.y + 8, s);
			float dd = self.getDistanceTo(point.x + 8, point.y + 8);

			if (ds < 4f || dd < d) {
				return dd <= d;
			}

			return false;
		}

		public boolean moveTo(Point point, float s, float d) {
			if (this.nextPathPoint == null) {
				this.nextPathPoint = self.getCloser(point);

				if (this.nextPathPoint == null) {
					return false;
				}
			}

			// tile offset
			float ds = self.moveToPoint(this.nextPathPoint.x + 8, this.nextPathPoint.y , s);
			float dd = self.getDistanceTo(point.x + 8, point.y + 8);

			if (ds < 4f || dd < d) {
				this.nextPathPoint = null;
				return dd <= d;
			}

			return false;
		}

		public boolean moveFrom(Point point, float s, float d) {
			if (this.targetPoint == null) {
				if (self.target == null) {
					self.target = Player.instance;

					if (!self.saw) {
						self.saw = true;
						self.noticeSignT = 2f;
						self.playSfx("enemy_alert");
					}
				}

				self.lastSeen = new Point(self.target.x, self.target.y);
				this.targetPoint = self.getFar(self.lastSeen);

				if (this.targetPoint == null) {
					self.lastSeen = new Point(self.target.x, self.target.y);
					return false;
				}
			}

			if (this.nextPathPoint == null) {
				this.nextPathPoint = self.getCloser(this.targetPoint);

				if (this.nextPathPoint == null) {
					this.targetPoint = null;
					return false;
				}
			}

			// tile offset
			float ds = self.moveToPoint(this.nextPathPoint.x + 8, this.nextPathPoint.y , s);
			float dd = self.getDistanceTo(this.targetPoint.x + 8, this.targetPoint.y + 8);

			if (ds < 4f || dd < d) {
				// self.lastSeen = new Point(self.target.x, self.target.y);
				this.nextPathPoint = null;
				return dd <= d;
			}

			return false;
		}

		public void checkForPlayer() {
			this.checkForPlayer(false);
		}

		public void checkForPlayer(boolean force) {
			if (self.target != null) {
				if (self.target.room != self.room) {
					self.saw = false;
					self.lastSeen = null; // So no one follows ya

					return;
				}

				self.lastSeen = new Point(self.target.x, self.target.y);
			}

			if (this.target == null && force) {
				self.findTarget(true);
			}

			if (self.target != null) {
				if (self.target.room == self.room && !self.saw && self.canSee(self.target)) {
					self.saw = true;
					self.playSfx("enemy_alert");
					self.noticeSignT = 2f;

					if (!self.state.equals("chase") && !self.state.equals("runaway")) {
						self.become("alerted");
					}
				}
			} else if (self.saw) {
				self.saw = false;
			}
		}

		public Room target;
		
		public void findNearbyPoint() {
			if (this.targetPoint != null) {
				return;
			}
			
			if (self.room != null) {
				if (true) {
					this.target = self.room;
				} else {
					for (int i = 0; i < 10; i++) {
						this.target = self.room.neighbours.get(Random.newInt(self.room.neighbours.size()));

						if (this.target != self.lastRoom && this.target != self.room) {
							self.lastRoom = self.room;
							break;
						}

						if (i == 9) {
							self.lastRoom = self.room;
						}
					}
				}

				boolean found = false;

				for (int i = 0; i < this.target.getWidth() * this.target.getHeight(); i++) {
					this.targetPoint = this.target.getRandomCell();

					if (Dungeon.level.isValid((int) this.targetPoint.x, (int) this.targetPoint.y) &&
						(self.toWater ? Dungeon.level.get((int) this.targetPoint.x, (int) this.targetPoint.y) == Terrain.WATER
							: Dungeon.level.checkFor(
						(int) this.targetPoint.x, (int) this.targetPoint.y, Terrain.PASSABLE))) {

						found = true;
						this.targetPoint.mul(16);
						break;
					}
				}

				if (!found) {
					this.target = null;
					this.targetPoint = null;
				} else {
					self.toWater = false;
				}
			}
		}
	}

	public boolean saw;

	@Override
	public int hashCode() {
		return id;
	}
}