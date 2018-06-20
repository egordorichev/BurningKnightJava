package org.rexcellentgames.burningknight.entity.creature.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.*;
import org.rexcellentgames.burningknight.entity.creature.fx.BloodFx;
import org.rexcellentgames.burningknight.entity.creature.fx.TextFx;
import org.rexcellentgames.burningknight.entity.creature.inventory.Inventory;
import org.rexcellentgames.burningknight.entity.creature.inventory.UiBuff;
import org.rexcellentgames.burningknight.entity.creature.inventory.UiInventory;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.fx.ItemPickedFx;
import org.rexcellentgames.burningknight.entity.creature.player.fx.ItemPickupFx;
import org.rexcellentgames.burningknight.entity.creature.player.fx.RunFx;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.consumable.potion.HealingPotion;
import org.rexcellentgames.burningknight.entity.item.entity.BombEntity;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.BowA;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.GunA;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.SwordA;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.butcher.ButcherA;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.morning.MorningStarA;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.util.*;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Player extends Creature {
	public Type type;
	public static Type toSet = Type.WARRIOR;

	public Type getType() {
		return this.type;
	}

	public enum Type {
		WARRIOR(0),
		WIZARD(1),
		RANGER(2);

		public byte id;

		Type(int id) {
			this.id = (byte) id;
		}
	}

	public static ArrayList<Player> all = new ArrayList<>();
	public int inventorySize = 12;

	public boolean fireBombs;
	public boolean iceBombs;
	public boolean poisonBombs;
	private static final float LIGHT_SIZE = 5f;
	public static String NAME;
	public static Player instance;
	public static boolean REGISTERED = false;
	private static HashMap<String, Animation> skins = new HashMap<>();
	public float lightModifier;
	public int connectionId;
	public float heat;
	public boolean hasRedLine;
	protected float mana;
	protected float manaMax;
	protected int level;
	protected int forThisLevel;
	private ItemPickupFx pickupFx;
	private Inventory inventory;
	public UiInventory ui;
	private float hunger;
	private String name;
	private float watery;
	private AnimationData idle;
	private AnimationData run;
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData animation;
	public Room currentRoom;
	public float dashT;
	public ArrayList<UiBuff> uiBuffs = new ArrayList<>();
	public float poisonChance;
	public float burnChance;
	public float freezeChance;
	public float reflectDamageChance;
	public float thornDamageChance;
	public float regen;
	public float goldModifier = 1f;
	public float vampire;
	public boolean lavaResist;
	public boolean fireResist;
	public boolean poisonResist;
	public boolean stunResist;
	public boolean seeSecrets;

	@Override
	protected boolean canHaveBuff(Buff buff) {
		if (fireResist && buff instanceof BurningBuff) {
			return false;
		} else if (poisonResist && buff instanceof PoisonBuff) {
			return false;
		} else if (stunResist && buff instanceof FreezeBuff) {
			return false;
		}

		return super.canHaveBuff(buff);
	}

	@Override
	protected void onHurt(float a, Creature from) {
		super.onHurt(a, from);

		Camera.shake(4f);
		Audio.playSfx("voice_gobbo_" + Random.newInt(1, 4), 1f, Random.newFloat(0.9f, 1.9f));

		if (from != null && Random.chance(this.reflectDamageChance)) {
			from.modifyHp((int) Math.ceil(a / 2), this, true);
		}
	}

	@Override
	public void onHit(Creature who) {
		super.onHit(who);

		if (who == null) {
			return;
		}

		if (Random.chance(this.poisonChance)) {
			who.addBuff(new PoisonBuff().setDuration(5));
		}

		if (Random.chance(this.burnChance)) {
			who.addBuff(new BurningBuff().setDuration(5));
		}

		if (Random.chance(this.freezeChance)) {
			who.addBuff(new FreezeBuff().setDuration(2));
		}

		if (Random.chance(vampire)) {
			this.modifyHp(1, null);
		}
	}

	@Override
	public void addBuff(Buff buff) {
		if (this.canHaveBuff(buff)) {
			Buff b = this.buffs.get(buff.getClass());

			if (b != null) {
				b.setDuration(Math.max(b.getDuration(), buff.getDuration()));
			} else {
				this.buffs.put(buff.getClass(), buff);

				buff.setOwner(this);
				buff.onStart();

				UiBuff bf = new UiBuff();

				bf.buff = buff;
				bf.owner = this;

				for (UiBuff bu : this.uiBuffs) {
					if (bu.buff.getClass() == buff.getClass()) {
						bu.buff = buff;
						return;
					}
				}

				this.uiBuffs.add(bf);
			}
		}
	}

	@Override
	public void onBuffRemove(Buff buff) {
		super.onBuffRemove(buff);

		for (UiBuff b : this.uiBuffs) {
			if (b.buff.getClass().equals(buff.getClass())) {
				b.remove();
				return;
			}
		}
	}

	{
		hpMax = 16;
		manaMax = 16;
		level = 1;
		hunger = 10;
		mul = 0.85f;
		speed = 25;
		alwaysActive = true;
		invmax = 1f;
		// unhittable = true; // todo: remove

		setSkin("");
	}

	public boolean lowHealthDefense;

	public void setSkin(String add) {
		Animation animations;

		if (skins.containsKey(add)) {
			animations = skins.get(add);
		} else {
			animations = Animation.make("actor-gobbo", add);
			skins.put(add, animations);
		}

		idle = animations.get("idle");
		run = animations.get("run");
		hurt = animations.get("hurt");
		killed = animations.get("dead");
		animation = this.idle;
	}

	public Player() {
		this("player");
	}

	public Player(String name) {
		this.name = name;

		all.add(this);

		if (!name.equals("ghost")) {
			instance = this;
		}

		run.setListener(new AnimationData.Listener() {
			@Override
			public void onFrame(int frame) {
				if (frame == 2 || frame == 6) {
					playStepSfx();
				}
			}
		});
	}

	protected void playStepSfx() {
		if (this.watery > 4.5f) {
			Audio.playSfx("step_gobbo_water_" + Random.newInt(1, 6), 1f, Random.newFloat(0.9f, 1.9f));
		} else {
			Audio.playSfx("step_gobbo_" + Random.newInt(1, 6), 1f, Random.newFloat(0.9f, 1.9f));
		}
	}

	public float getLightSize() {
		return LIGHT_SIZE + this.lightModifier;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void generate() {
		if (Dungeon.type != Dungeon.Type.INTRO) {
			switch (this.type) {
				case WARRIOR:
					generateWarrior();
					break;
				case WIZARD:
					generateMage();
					break;
				case RANGER:
					generateRanger();
					break;
			}
		}
	}
	private void generateRanger() {
		switch (Random.newInt(2)) {
			case 0: default: this.give(new BowA()); break;
			case 1: this.give(new GunA()); break;
		}
	}

	private void generateWarrior() {
		switch (Random.newInt(3)) {
			case 0:
			default:
				this.give(new SwordA());
				break;
			case 1:
				this.give(new ButcherA());
				break;
			case 2:
				this.give(new MorningStarA());
				break;
		}

		this.give(new HealingPotion());
	}

	private void generateMage() {
		// todo
	}

	private void give(Item item) {
		item.generate();
		this.inventory.add(new ItemHolder().setItem(item));
	}

	public float getHunger() {
		return this.hunger;
	}

	public void setHunger(float hunger) {
		this.hunger = MathUtils.clamp(0, 360, hunger);

		if (this.hunger == 360) {
			if (!this.hasBuff(StarvingBuff.class)) {
				this.addBuff(new StarvingBuff());
			}
		} else {
			this.removeBuff(StarvingBuff.class);
		}

		if (this.hunger >= 260 && this.hunger != 360) {
			if (!this.hasBuff(HungryBuff.class)) {
				this.addBuff(new HungryBuff());
			}
		} else {
			this.removeBuff(HungryBuff.class);
		}
	}

	public void setUi(UiInventory ui) {
		this.ui = ui;
	}

	public boolean healOnEnter;

	@Override
	public void destroy() {
		super.destroy();

		if (this.ui != null && !this.ui.done) {
			this.ui.destroy();
			this.ui = null;
		}
		Player.all.remove(this);
	}

	public static Entity ladder;

	@Override
	public void init() {
		super.init();

		this.type = toSet;

		if (instance == null) {
			instance = this;
		}

		this.mana = this.manaMax;
		this.inventory = new Inventory(this, inventorySize);
		this.body = this.createSimpleBody(3, 1, 10, 10, BodyDef.BodyType.DynamicBody, false);

		Camera.follow(this, true);

		if (ladder != null) {
			this.tp(ladder.x, ladder.y - 2);
		}
	}

	public void modifyMana(float a) {
		this.mana = MathUtils.clamp(0, this.manaMax, this.mana + a);
	}

	private float lastRun;
	private float lastDashT;
	private float dashTimeout;
	private float lastRegen;

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.toDeath) {
			this.t += dt;
			this.animation.update(dt);

			if (this.t >= 1f) {
				super.die(false);

				this.dead = true;
				this.done = true;
				SaveManager.delete();

				Camera.shake(10);
				this.remove();

				deathEffect(killed);
				BloodFx.add(this, 20);
			}

			return;
		}


		if (this.regen > 0 && this.hp != this.hpMax) {
			this.lastRegen += dt;

			if (this.lastRegen > (20f - this.regen)) {
				this.modifyHp(1, null);
				this.lastRegen = 0;
			}
		}

		this.dashT = Math.max(0, this.dashT - dt);
		this.dashTimeout = Math.max(0, this.dashTimeout - dt);

		if (Dungeon.level != null) {
			Dungeon.level.addLightInRadius(this.x + 8, this.y + 8, 0, 0, 0, 2f, this.getLightSize(), false);
			Room room = Dungeon.level.findRoomFor(this.x, this.y);

			if (room != null) {
				if (this.currentRoom != room) {
					if (this.seeSecrets) {
						for (Room r : room.connected.keySet()) {
							if (r.hidden) {
								for (int x = r.left; x <= r.right; x++) {
									for (int y = r.top; y <= r.bottom; y++) {
										if (Dungeon.level.get(x, y) == Terrain.CRACK) {
											r.hidden = false;
											BombEntity.make(r);
											Dungeon.level.set(x, y, Terrain.FLOOR_A);

											Dungeon.level.loadPassable();
											Dungeon.level.addPhysics();
										}
									}
								}
							}
						}
					}


					if (this.healOnEnter && room.numEnemies > 0 && Random.chance(50)) {
						this.modifyHp(2, null);
					}
				}

				this.currentRoom = room;

				for (int x = this.currentRoom.left; x <= this.currentRoom.right; x++) {
					for (int y = this.currentRoom.top + 1; y <= this.currentRoom.bottom; y++) {
						if ((x == this.currentRoom.left || x == this.currentRoom.right || y == this.currentRoom.top || y == this.currentRoom.bottom
							) && (Dungeon.level.checkFor(x, y, Terrain.PASSABLE) || Dungeon.level.checkFor(x, y, Terrain.HOLE))) {
							Dungeon.level.addLightInRadius(x * 16, y * 16, 0, 0, 0, 2f, 2f, false);
						}

						Dungeon.level.addLight(x * 16, y * 16, 0, 0, 0, 2f, 2f);
					}
				}
			}
		}

		float l = this.mana;

		this.modifyMana(dt * 10);

		if (l < this.mana && Float.compare(this.mana, this.manaMax) == 0) {
			Dungeon.area.add(new TextFx("Full Mana", this).setColor(Dungeon.BLUE));
		}

		this.watery = Math.max(0, this.watery - dt);

		if (this.dead) {
			super.common();
			return;
		}

		this.heat = Math.max(0, this.heat - dt / 3);

		// this.setHunger(this.hunger + dt);

		if (Dialog.active == null && !this.freezed) {
			if (Input.instance.isDown("mouse2")) {
				float dx = Input.instance.worldMouse.x - this.x - 8;
				float dy = Input.instance.worldMouse.y - this.y - 8;
				float a = (float) Math.atan2(dy, dx);

				this.vel.x += (float) (this.speed * Math.cos(a));
				this.vel.y += (float) (this.speed * Math.sin(a));
			} else {
				if (Input.instance.isDown("left")) {
					this.vel.x -= this.speed;
				}

				if (Input.instance.isDown("right")) {
					this.vel.x += this.speed;
				}

				if (Input.instance.isDown("up")) {
					this.vel.y += this.speed;
				}

				if (Input.instance.isDown("down")) {
					this.vel.y -= this.speed;
				}

				float mx = Input.instance.getAxis("moveX");
				float my = Input.instance.getAxis("moveY");

				if (mx != 0 || my != 0) {
					this.vel.x += mx * this.speed;
					this.vel.y -= my * this.speed; // Inverted!
				}

				/*if (Input.instance.wasPressed("dash") && this.dashTimeout == 0 && this.dashT == 0f) {
					float dx = Input.instance.worldMouse.x - this.x - 8;
					float dy = Input.instance.worldMouse.y - this.y - 8;
					float a = (float) Math.atan2(dy, dx);

					for (int i = 0; i < 20; i++) {
						Part part = new Part();

						part.x = this.x + Random.newFloat(this.w);
						part.y = this.y - Random.newFloat(this.h);

						Dungeon.area.add(part);
					}

					this.vel.x += (float) (this.speed * 200 * Math.cos(a));
					this.vel.y += (float) (this.speed * 200 * Math.sin(a));

					this.dashT = 0.3f;

					Tween.to(new Tween.Task(1.5f, 0.05f) {
						@Override
						public float getValue() {
							return sx;
						}

						@Override
						public void setValue(float value) {
							sx = value;
						}

						@Override
						public void onEnd() {
							super.onEnd();

							Tween.to(new Tween.Task(1f, 0.05f) {
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
					});
				}*/
			}
		} else if (Dialog.active != null) {
			if (Input.instance.wasPressed("action")) {
				Dialog.active.skip();
			}
		}

		float v = this.vel.len2();
		this.lastRun += dt;

		if (v > 20) {
			this.become("run");

			if (this.lastRun >= 0.08f) {
				this.lastRun = 0;
				this.area.add(new RunFx(this.x, this.y - 8));
			}
		} else {
			this.become("idle");

			this.vel.x = 0;
			this.vel.y = 0;
			this.dashT = 0;
		}

		super.common();

		if (this.animation != null && !this.freezed) {
			this.animation.update(dt);
		}

		float dx = this.x + this.w / 2 - Input.instance.worldMouse.x;
		this.flipped = dx >= 0;

		if (this.lastDashT != 0 && this.dashT == 0) {
			this.vel.mul(0.5f);
			this.dashTimeout = 3f;

			Tween.to(new Tween.Task(0.8f, 0.05f) {
				@Override
				public float getValue() {
					return sx;
				}

				@Override
				public void setValue(float value) {
					sx = value;
				}

				@Override
				public void onEnd() {
					super.onEnd();

					Tween.to(new Tween.Task(1f, 0.05f) {
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
			});
		}

		this.lastDashT = this.dashT;
	}

	@Override
	protected void onTouch(short t, int x, int y) {
		if (t == Terrain.WATER && !this.flying) {
			this.removeBuff(BurningBuff.class);
			this.watery = 5f;
		} else if (t == Terrain.LAVA && !this.flying && !this.lavaResist) {
			this.modifyHp(-1, null,true);
		}
	}

	public boolean toDeath;

	@Override
	protected void die(boolean force) {
		if (this.toDeath) {
			return;
		}

		GlobalSave.put("deaths", GlobalSave.getInt("deaths") + 1);

		Vector3 vec = Camera.game.project(new Vector3(this.x + this.w / 2, this.y + this.h / 2, 0));
		vec = Camera.ui.unproject(vec);
		vec.y = Display.GAME_HEIGHT - vec.y;

		Dungeon.shockTime = 0;
		Dungeon.shockPos.x = (vec.x) / Display.GAME_WIDTH;
		Dungeon.shockPos.y = (vec.y) / Display.GAME_HEIGHT;

		this.toDeath = true;
		this.t = 0;
		Dungeon.slowDown(0.5f, 1f);
	}

	public static ShaderProgram shader;

	static {
		shader = new ShaderProgram(Gdx.files.internal("shaders/rainbow.vert").readString(),  Gdx.files.internal("shaders/rainbow.frag").readString());
		if (!shader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());
	}

	public boolean drawInvt;
	public boolean luckDamage;
	public boolean luckDefense;
	public boolean pauseMore;
	public boolean lowHealthDamage;

	@Override
	public float rollDamage() {
		float v;

		if (luckDamage) {
			if (Random.chance(60)) {
				v = 2;
			} else {
				v = 0.5f;
			}
		} else {
			v = super.rollDamage();
		}

		if (lowHealthDamage && this.hp < this.hpMax / 4) {
			v *= 2;
		}

		return (pauseMore && this.vel.len() < 1f) ? v * 1.5f : v;
	}

	@Override
	public float rollDefense() {
		float v;

		if (luckDefense) {
			if (Random.chance(60)) {
				v = 2;
			} else {
				v = 0.5f;
			}
		} else {
			v = super.rollDefense();
		}

		if (lowHealthDefense && this.hp < this.hpMax / 4) {
			v *= 2;
		}

		return v;
	}

	@Override
	public void render() {
		Graphics.batch.setColor(1, 1, 1, this.a);

		if (this.falling) {
			this.renderFalling(this.animation);
			return;
		}

		if (this.invt > 0) {
			this.animation = hurt;
		} else if (this.state.equals("run")) {
			this.animation = run;
		} else {
			this.animation = idle;
		}

		if (this.invtt == 0) {
			this.drawInvt = false;
		}

		if (this.ui != null) {
			this.ui.renderBeforePlayer(this);
		}

		boolean shade = this.drawInvt && this.invtt > 0;
		TextureRegion region = this.animation.getCurrent().frame;

		if (shade) {
			Texture texture = region.getTexture();

			Graphics.batch.end();
			shader.begin();
			shader.setUniformf("time", Dungeon.time);
			shader.setUniformf("pos", new Vector2((float) region.getRegionX() / texture.getWidth(), (float) region.getRegionY() / texture.getHeight()));
			shader.setUniformf("size", new Vector2((float) region.getRegionWidth() / texture.getWidth(), (float) region.getRegionHeight() / texture.getHeight()));
			shader.setUniformf("a", this.a);
			shader.end();
			Graphics.batch.setShader(shader);
			Graphics.batch.begin();
		} else if (this.fa > 0) {
			Graphics.batch.end();
			Mob.frozen.begin();
			Mob.frozen.setUniformf("time", Dungeon.time);
			Mob.frozen.setUniformf("f", this.fa);
			Mob.frozen.setUniformf("a", this.a);
			Mob.frozen.end();
			Graphics.batch.setShader(Mob.frozen);
			Graphics.batch.begin();
		}

		if (this.freezed) {
			this.fa += (1 - this.fa) * Gdx.graphics.getDeltaTime() * 3f;
		} else {
			this.fa += (0 - this.fa) * Gdx.graphics.getDeltaTime() * 3f;
		}

		this.animation.render(this.x - region.getRegionWidth() / 2 + 8,
			this.y - region.getRegionHeight() / 2 + 8, false, false, region.getRegionWidth() / 2,
			(int) Math.ceil(((float) region.getRegionHeight()) / 2), 0, this.sx * (this.flipped ? -1 : 1), this.sy);

		if (shade || this.fa > 0) {
			Graphics.batch.end();
			Graphics.batch.setShader(null);
			Graphics.batch.begin();
		}

		if (this.ui != null) {
			this.ui.renderOnPlayer(this);
		}

		Graphics.batch.setColor(1, 1, 1, 1);
		this.renderBuffs();
	}

	private float fa;

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x + this.hx, this.y + this.hy, this.hw, this.hh);
	}

	private float sx = 1f;
	private float sy = 1f;

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.inventorySize = reader.readInt16();
		this.inventory.resize(inventorySize);
		this.inventory.load(reader);

		this.mana = reader.readInt32();
		this.manaMax = reader.readFloat();
		this.level = reader.readInt32();

		float last = this.speed;
		this.speed = reader.readFloat();

		this.maxSpeed += (this.speed - last) * 7f;

		this.setHunger(reader.readInt16());

		if (ladder != null) {
			this.tp(ladder.x, ladder.y - 2);
		}
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeInt16((short) this.inventorySize);
		this.inventory.save(writer);

		writer.writeFloat(this.mana);
		writer.writeFloat(this.manaMax);
		writer.writeInt32(this.level);
		writer.writeFloat(this.speed);

		writer.writeInt16((short) this.hunger);
	}

	private ArrayList<ItemHolder> holders = new ArrayList<>();

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof ItemHolder) {
			ItemHolder item = (ItemHolder) entity;

			if (item.getItem().hasAutoPickup() || item.auto) {
				if (this.tryToPickup(item) && !item.auto) {
					this.area.add(new ItemPickedFx(item));
					item.remove();
				}
			} else if (!item.falling) {
				this.holders.add(item);

				if (this.pickupFx == null) {
					this.pickupFx = new ItemPickupFx(item, this);
					this.area.add(this.pickupFx);
				}
			}
		} else if (entity instanceof Mob) {
			if (Random.chance(this.thornDamageChance)) {
				((Mob) entity).modifyHp(-4, this);
			}
		}
	}

	@Override
	public void onCollisionEnd(Entity entity) {
		if (entity instanceof ItemHolder) {
			if (this.pickupFx != null) {
				this.pickupFx.remove();
				this.pickupFx = null;
			}

			this.holders.remove(entity);

			if (this.holders.size() > 0) {
				this.pickupFx = new ItemPickupFx(this.holders.get(0), this);
				this.area.add(this.pickupFx);
			}
		}
	}

	public boolean tryToPickup(ItemHolder item) {
		if (!item.done) {

			if (this.inventory.add(item)) {
				if (item.getItem().hasAutoPickup()) {
					this.area.add(new ItemPickedFx(item));
				}

				return true;
			}
		}

		return false;
	}

	public Inventory getInventory() {
		return this.inventory;
	}

	public float getMana() {
		return this.mana;
	}

	public float getManaMax() {
		return this.manaMax;
	}

	public int getLevel() {
		return this.level;
	}
}