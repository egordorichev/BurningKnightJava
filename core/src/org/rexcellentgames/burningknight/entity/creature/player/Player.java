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
import org.rexcellentgames.burningknight.entity.creature.buff.Buff;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.creature.buff.FreezeBuff;
import org.rexcellentgames.burningknight.entity.creature.buff.PoisonBuff;
import org.rexcellentgames.burningknight.entity.creature.fx.BloodFx;
import org.rexcellentgames.burningknight.entity.creature.inventory.Inventory;
import org.rexcellentgames.burningknight.entity.creature.inventory.UiBuff;
import org.rexcellentgames.burningknight.entity.creature.inventory.UiInventory;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.fx.ItemPickedFx;
import org.rexcellentgames.burningknight.entity.creature.player.fx.ItemPickupFx;
import org.rexcellentgames.burningknight.entity.fx.BloodDropFx;
import org.rexcellentgames.burningknight.entity.fx.BloodSplatFx;
import org.rexcellentgames.burningknight.entity.item.Gold;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.accessory.equipable.BlackHeart;
import org.rexcellentgames.burningknight.entity.item.accessory.equipable.ClockHeart;
import org.rexcellentgames.burningknight.entity.item.accessory.equipable.ManaShield;
import org.rexcellentgames.burningknight.entity.item.consumable.potion.HealingPotion;
import org.rexcellentgames.burningknight.entity.item.entity.BombEntity;
import org.rexcellentgames.burningknight.entity.item.weapon.axe.AxeA;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.BowA;
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.DaggerA;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Revolver;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.MagicMissileWand;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.book.FastBook;
import org.rexcellentgames.burningknight.entity.item.weapon.spear.SpearA;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.SwordA;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.butcher.ButcherA;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.morning.MorningStarA;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.entities.ClassSelector;
import org.rexcellentgames.burningknight.entity.level.entities.Entrance;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.ui.UiMap;
import org.rexcellentgames.burningknight.util.*;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Player extends Creature {
	private static final float LIGHT_SIZE = 5f;
	public static Type toSet = Type.WARRIOR;
	public static float mobSpawnModifier = 1f;
	public static ArrayList<Player> all = new ArrayList<>();
	public static Player instance;
	public static Entity ladder;
	private static HashMap<String, Animation> skins = new HashMap<>();
	public static ShaderProgram shader;

	static {
		shader = new ShaderProgram(Gdx.files.internal("shaders/rainbow.vert").readString(),  Gdx.files.internal("shaders/rainbow.frag").readString());
		if (!shader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());
	}

	public Type type;
	public boolean flipRegenFormula;
	public boolean manaCoins;
	public int inventorySize = 12;
	public boolean fireBombs;
	public boolean iceBombs;
	public boolean poisonBombs;
	public float lightModifier;
	public float heat;
	public boolean hasRedLine;
	public float defenseModifier = 1f;
	public UiInventory ui;
	public boolean moreManaRegenWhenLow;
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
	public float manaRegenRate = 1f;
	public float damageModifier = 1f;
	public boolean manaRegenRoom = false;
	public boolean lifeRegenRegensMana;
	public boolean lowHealthDefense;
	public boolean manaBombs;
	public boolean healOnEnter;
	public boolean toDeath;
	public boolean drawInvt;
	public boolean luckDamage;
	public boolean luckDefense;
	public boolean pauseMore;
	public float manaModifier = 1;
	public boolean lowHealthDamage;
	protected float mana;
	protected int manaMax;
	protected int level;
	public ItemPickupFx pickupFx;
	private Inventory inventory;
	private String name;
	private float watery;
	private float lastRun;
	private float lastRegen;
	private float lastMana;
	private float fa;
	private float sx = 1f;
	private float sy = 1f;
	private ArrayList<ItemHolder> holders = new ArrayList<>();
	private AnimationData idle;
	private AnimationData run;
	private AnimationData hurt;
	private AnimationData killed;
	private AnimationData animation;
	public float accuracy;
	public static boolean seeMore;

	{
		hpMax = 8;
		manaMax = 6;
		level = 1;
		mul = 0.8f;
		speed = 20;
		alwaysActive = true;

		setSkin("body");
	}

	public float getMage() {
		return this.type == Type.WIZARD ? 1f : 0f;
	}

	public float getWarrior() {
		return this.type == Type.WARRIOR ? 1f : 0f;
	}

	public float getRanger() {
		return this.type == Type.RANGER ? 1f : 0f;
	}

	@Override
	public void initStats() {
		super.initStats();

		this.modifyStat("ammo_capacity", 1f);
		this.modifyStat("inv_time", 1f);
		this.modifyStat("reload_time", 1f);
		this.modifyStat("gun_use_time", 1f);
	}

	public Player() {
		this("player");
	}

	public Player(String name) {
		this.name = name;

		all.add(this);
		instance = this;

		run.setListener(new AnimationData.Listener() {
			@Override
			public void onFrame(int frame) {
				if (frame == 2 || frame == 6) {
					playStepSfx();
				}
			}
		});
		Ui.ui.dead = false;
	}

	protected void playStepSfx() {
		if (this.watery > 4.5f) {
			Audio.playSfx("step_gobbo_water_" + Random.newInt(1, 6), 1f, Random.newFloat(0.9f, 1.9f));
		} else {
			Audio.playSfx("step_gobbo_" + Random.newInt(1, 6), 1f, Random.newFloat(0.9f, 1.9f));
		}
	}

	public Type getType() {
		return this.type;
	}

	private TextureRegion hat;

	public void setHat(String name) {
		if (name.isEmpty()) {
			hat = null;
			return;
		}

		this.hat = Graphics.getTexture("hat-" + name + "-idle-00");
	}

	private static Animation headAnimations = Animation.make("actor-gobbo", "-gobbo");
	private static AnimationData headIdle = headAnimations.get("idle");
	private static AnimationData headRun = headAnimations.get("run");
	private static AnimationData headHurt = headAnimations.get("hurt");

	public void setSkin(String add) {
		Animation animations;

		if (!add.isEmpty()) {
			add = "-" + add;
		}

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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void generate() {
		this.inventory.clear();

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

	private void generateWarrior() {
		switch (Random.newInt(5)) {
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
			case 3:
				this.give(new DaggerA());
				break;
			case 4:
				this.give(new SpearA());
				break;
		}

		this.give(new HealingPotion());
	}

	private void generateMage() {
		this.hpMax = 6;
		this.hp = 6;
		this.numIronHearts = 2;

		switch (Random.newInt(2)) {
			case 0: default: this.give(new MagicMissileWand()); break;
			case 1: this.give(new FastBook()); break;
		}
	}

	private int numIronHearts;
	private int numGoldenHearts;

	public void addIronHearts(int a) {
		numIronHearts += a;
	}

	public void addGoldenHearts(int a) {
		numGoldenHearts += a;
	}

	public int getIronHearts() {
		return numIronHearts;
	}

	public int getGoldenHearts() {
		return numGoldenHearts;
	}

	private void generateRanger() {
		this.hpMax = 6;
		this.hp = 6;

		switch (Random.newInt(3)) {
			case 0: default: this.give(new BowA()); break;
			case 1: this.give(new Revolver()); break;
			case 2: this.give(new AxeA().setCount(10)); break;
		}
	}

	public void give(Item item) {
		item.generate();
		this.inventory.add(new ItemHolder().setItem(item));
	}

	public void setUi(UiInventory ui) {
		this.ui = ui;
	}

	public void modifyManaMax(int a) {
		this.manaMax += a;
		this.modifyMana(0);
	}

	@Override
	public void render() {
		Graphics.batch.setColor(1, 1, 1, this.a);

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

		int id = this.animation.getFrame();
		float of = offsets[id] - 2;

		if (this.invt > 0) {
			id += 16;
		} else if (this.state.equals("run")) {
			id += 8;
		}

		if (this.ui != null) {
			this.ui.renderBeforePlayer(this, of);
		}

		boolean before = false;

		/*Item item = this.inventory.getSlot(this.inventory.activeController);

		if (item instanceof WeaponBase) {
			Point aim = this.getAim();
			double a = this.getAngleTo(aim.x, aim.y);
			before = (a > 0 && a < Math.PI);
		}*/

		if (this.ui != null && before) {
			this.ui.renderOnPlayer(this, of);
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
			Mob.frozen.setUniformf("freezed", this.wasFreezed ? 1f : 0f);
			Mob.frozen.setUniformf("poisoned", this.wasPoisoned ? 1f : 0f);
			Mob.frozen.end();
			Graphics.batch.setShader(Mob.frozen);
			Graphics.batch.begin();
		}

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

		this.animation.render(this.x - region.getRegionWidth() / 2 + 8,
			this.y, false, false, region.getRegionWidth() / 2,
			0, 0, this.sx * (this.flipped ? -1 : 1), this.sy);

		if (this.hat != null) {
			Graphics.render(this.hat, this.x + 3 + hat.getRegionWidth() / 2, this.y + offsets[id] + region.getRegionHeight() / 2 - 2,
				0, region.getRegionWidth() / 2, 0, false, false, this.sx * (this.flipped ? -1 : 1), this.sy);
		} else {
			AnimationData anim = headIdle;

			if (this.invt > 0) {
				anim = headHurt;
			} else if (this.state.equals("run")) {
				anim = headRun;
			}

			anim.setFrame(this.animation.getFrame());
			region = anim.getCurrent().frame;

			anim.render(this.x - region.getRegionWidth() / 2 + 8,
				this.y, false, false, region.getRegionWidth() / 2,
				0, 0, this.sx * (this.flipped ? -1 : 1), this.sy);
		}

		if (shade || this.fa > 0) {
			Graphics.batch.end();
			Graphics.batch.setShader(null);
			Graphics.batch.begin();
		}

		if (this.ui != null && !before) {
			this.ui.renderOnPlayer(this, of);
		}

		Graphics.batch.setColor(1, 1, 1, 1);
		this.renderBuffs();

		// Graphics.print((int) Math.floor(this.x) + " " + (int) Math.floor(this.y), Graphics.small, this.x, this.y);
	}

	private static int offsets[] = new int[] {
		0, 0, 0, -1, -1, -1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0
	};

	@Override
	public void setHpMax(int hpMax) {
		super.setHpMax(hpMax);

		if (this.hpMax >= 8) {
			Achievements.unlock(Achievements.GET_8_HEART_CONTAINERS);
		}
	}

	private boolean wasFreezed;
	private boolean wasPoisoned;
	private boolean gotHit;

	public void resetHit() {
		gotHit = false;
	}

	public boolean didGetHit() {
		return gotHit;
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof ItemHolder) {
			ItemHolder item = (ItemHolder) entity;

			if (item.getItem().hasAutoPickup() || item.auto) {
				if (this.tryToPickup(item) && !item.auto) {
					if (!(item.getItem() instanceof Gold)) {
						this.area.add(new ItemPickedFx(item));
					} else {
						if (this.inventory.getGold() >= 100) {
							Achievements.unlock(Achievements.UNLOCK_MONEY_PRINTER);
						}

						if (this.inventory.getGold() >= 300) {
							Achievements.unlock(Achievements.COLLECT_300_GOLD);
						}
					}

					item.remove();
				}
			} else if (!item.falling) {
				if (item instanceof ClassSelector) {
					if (((ClassSelector) item).same(this.type)) {
						return;
					}
				}

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
					if (!(item.getItem() instanceof Gold)) {
						this.area.add(new ItemPickedFx(item));
					}
				}

				if (item.getItem() instanceof Gold && this.manaCoins) {
					this.modifyMana(2);
				}

				return true;
			}
		}

		return false;
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x + this.hx, this.y, this.hw, this.hh);
	}

	@Override
	public void destroy() {
		super.destroy();

		if (this.ui != null && !this.ui.done) {
			this.ui.remove();
		}

		if (UiMap.instance != null) {
			UiMap.instance.remove();
		}

		Player.all.remove(this);
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public void init() {
		super.init();

		this.type = toSet;

		if (instance == null) {
			instance = this;
		}

		this.mana = this.manaMax;
		this.inventory = new Inventory(this, inventorySize);
		this.body = this.createSimpleBody(3, 0, 10, 11, BodyDef.BodyType.DynamicBody, false);

		Camera.follow(this, true);

		doTp(true);
	}

	private void doTp(boolean fromInit) {
		if (Dungeon.depth == -1) {
			Room room = Dungeon.level.getRooms().get(0);
			this.tp((room.left + room.getWidth() / 2) * 16 - 8, room.top * 16 + 16);
		} else if (ladder != null && (Dungeon.loadType != Entrance.LoadType.LOADING
			 || (!fromInit && (Dungeon.level.findRoomFor(this.x + this.w / 2, this.y) == null)))) {
			this.tp(ladder.x, ladder.y - 2);
		} else if (ladder == null) {
			Log.error("Null lader!");
		}
	}

	@Override
	public void tp(float x, float y) {
		super.tp(x, y);
		Camera.follow(this, true);
		orbitalRing.x = this.x + this.w / 2;
		orbitalRing.y = this.y + this.h / 2;
	}

	public Vector2 orbitalRing = new Vector2();

	private float last;
	private float lastBlood;

	@Override
	public void update(float dt) {
		super.update(dt);

		orbitalRing.lerp(new Vector2(this.x + this.w / 2, this.y + this.h / 2), 4 * dt);

		if (this.toDeath) {
			this.t += dt;
			this.animation.update(dt);

			if (this.t >= 1f) {
				Ui.ui.dead = true;
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

		if (this.mana != this.manaMax) {
			this.lastMana += dt * (this.vel.len2() > 9.9f ?
				(this.flipRegenFormula ? 1f : 0.5f) :
				(this.flipRegenFormula ? 0.5f : 1f)) * this.manaRegenRate * (
					(moreManaRegenWhenLow && this.hp <= this.hpMax / 3) ? 2 : 1
				) * (this.type == Type.WIZARD ? 1 : 0.5f);

			if (this.lastMana > 1f) {
				this.lastMana = 0;
				this.mana += 1;
			}
		}

		if (this.regen > 0 && this.hp != this.hpMax) {
			this.lastRegen += dt;

			if (this.lastRegen > (20f - this.regen)) {
				if (this.lifeRegenRegensMana) {
					this.modifyMana(1);
				} else {
					this.modifyHp(1, null);
				}

				this.lastRegen = 0;
			}
		}

		this.watery = Math.max(0, this.watery - dt);

		if (this.dead) {
			super.common();
			return;
		}

		if (this.hp <= 2) {
			this.last += dt;
			this.lastBlood += dt;

			if (this.lastBlood > 0.1f) {
				this.lastBlood = 0;

				BloodDropFx fx = new BloodDropFx();

				fx.owner = this;

				Dungeon.area.add(fx);
			}

			if (this.last >= 1f) {
				this.last = 0;
				BloodSplatFx fxx = new BloodSplatFx();

				fxx.x = x + Random.newFloat(w) - 8;
				fxx.y = y + Random.newFloat(h) - 8;

				Dungeon.area.add(fxx);
			}
		}

		Item item = this.inventory.getSlot(this.inventory.active);

		if (item != null) {
			item.updateInHands(dt);
		}

		this.heat = Math.max(0, this.heat - dt / 3);

		if (Dialog.active == null && !this.freezed && !UiMap.large) {
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
		} else if (Dialog.active != null) {
			if (Input.instance.wasPressed("interact")) {
				Dialog.active.skip();
			}
		}

		float v = this.vel.len2();
		this.lastRun += dt;

		if (v > 20) {
			this.become("run");

			/*if (this.lastRun >= 0.08f) {
				this.lastRun = 0;
				this.area.add(new RunFx(this.x, this.y - 8));
			}*/
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
	}

	public int getManaMax() {
		return this.manaMax;
	}

	@Override
	protected void onTouch(short t, int x, int y, byte info) {
		if (t == Terrain.WATER && !this.flying) {
			if (this.hasBuff(BurningBuff.class)) {
				int num = GlobalSave.getInt("num_fire_out") + 1;
				GlobalSave.put("num_fire_out", num);

				if (num >= 30) {
					Achievements.unlock(Achievements.UNLOCK_WATER_BOLT);
				}
			}

			this.removeBuff(BurningBuff.class);
			this.watery = 5f;
		} else {
			if (BitHelper.isBitSet(info, 0) && !this.hasBuff(BurningBuff.class)) {
				this.addBuff(new BurningBuff());
			}

			if (t == Terrain.LAVA && !this.flying && !this.lavaResist) {
				this.modifyHp(-1, null, true);

				if (this.isDead()) {
					Achievements.unlock(Achievements.UNLOCK_WINGS);
				}
			}
		}
	}

	private boolean hadEnemies;
	public byte numCollectedHearts;

	@Override
	protected void onRoomChange() {
		super.onRoomChange();

		if (numCollectedHearts >= 6) {
			Achievements.unlock(Achievements.UNLOCK_MEETBOY);
		}

		if (hadEnemies && !gotHit) {
			Achievements.unlock(Achievements.UNLOCK_HALO);
		}

		this.resetHit();

		if (this.room == null) {

		} else {
			hadEnemies = false;

			for (int i = Mob.all.size() - 1; i >= 0; i--) {
				Mob mob = Mob.all.get(i);

				if (mob.getRoom() == this.room) {
					hadEnemies = true;
					break;
				}
			}
		}

		if (this.seeSecrets) {
			for (Room r : room.connected.keySet()) {
				if (r.hidden) {
					for (int y = r.top; y <= r.bottom; y++) {
						for (int x = r.left; x <= r.right; x++) {

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

		if (manaRegenRoom && room.numEnemies > 0 && Random.chance(50)) {
			this.modifyMana(this.getManaMax());
		}
	}

	@Override
	protected void checkDeath() {
		if (this.hp == 0 && this.numIronHearts == 0 && this.numGoldenHearts == 0) {
			this.shouldDie = true;
		}
	}

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

		return ((pauseMore && this.vel.len() < 1f) ? v * 1.5f : v) * damageModifier * this.getStat("damage");
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

		return v * defenseModifier;
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
	public boolean rollBlock() {
		if (Random.chance(50) && this.ui.hasEquiped(ManaShield.class) && this.mana >= 2) {
			this.modifyMana(-2);
			return true;
		}

		return false;
	}

	@Override
	protected void doHurt(int a) {
		if (this.numGoldenHearts > 0) {
			int d = Math.min(this.numGoldenHearts, -a);
			this.numGoldenHearts -= d;
			a += d;

			for (int i = 0; i < 10; i++) {
				PoofFx fx = new PoofFx();

				fx.x = this.x + this.w / 2;
				fx.y = this.y + this.h / 2;

				Dungeon.area.add(fx);
			}

			Log.info("Golden hearts tp");
			this.doTp(false);

			for (int i = 0; i < 10; i++) {
				PoofFx fx = new PoofFx();

				fx.x = this.x + this.w / 2;
				fx.y = this.y + this.h / 2;

				Dungeon.area.add(fx);
			}
		}

		if (this.numIronHearts > 0) {
			int d = Math.min(this.numIronHearts, -a);
			this.numIronHearts -= d;
			a += d;
		}

		if (a < 0) {
			this.hp = Math.max(0, this.hp + a);
		}
	}

	@Override
	protected void onHurt(int a, Creature from) {
		super.onHurt(a, from);

		this.gotHit = true;

		Camera.shake(4f);
		Audio.playSfx("voice_gobbo_" + Random.newInt(1, 4), 1f, Random.newFloat(0.9f, 1.9f));

		if (from != null && Random.chance(this.reflectDamageChance)) {
			from.modifyHp((int) Math.ceil(a / 2), this, true);
		}

		if (this.ui.hasEquiped(BlackHeart.class) && this.room != null) {
			for (int i = Mob.all.size() - 1; i >= 0; i--) {
				Mob mob = Mob.all.get(i);

				if (mob.getRoom() == this.room) {
					mob.modifyHp(-3, this, true);
				}
			}
		}

		if (this.ui.hasEquiped(ClockHeart.class)) {
			Dungeon.slowDown(0.5f, 1f);
		}
	}

	@Override
	protected void die(boolean force) {
		if (this.toDeath) {
			return;
		}

		ui.hide();
		UiMap.instance.hide();

		Ui.ui.onDeath();

		this.done = false;
		int num = GlobalSave.getInt("deaths") + 1;
		GlobalSave.put("deaths", num);

		Vector3 vec = Camera.game.project(new Vector3(this.x + this.w / 2, this.y + this.h / 2, 0));
		vec = Camera.ui.unproject(vec);
		vec.y = Display.GAME_HEIGHT - vec.y;

		Dungeon.shockTime = 0;
		Dungeon.shockPos.x = (vec.x) / Display.GAME_WIDTH;
		Dungeon.shockPos.y = (vec.y) / Display.GAME_HEIGHT;

		this.toDeath = true;
		this.t = 0;
		Dungeon.slowDown(0.5f, 1f);

		Achievements.unlock(Achievements.DIE);

		if (num >= 50) {
			Achievements.unlock(Achievements.UNLOCK_ISAAC_HEAD);
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

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeInt16((short) this.inventorySize);
		this.inventory.save(writer);

		writer.writeInt32((int) this.mana);
		writer.writeInt32(this.manaMax);
		writer.writeInt32(this.level);
		writer.writeFloat(this.speed);

		writer.writeByte((byte) numIronHearts);
		writer.writeByte((byte) numGoldenHearts);
		writer.writeBoolean(this.gotHit);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.inventorySize = reader.readInt16();
		this.inventory.resize(inventorySize);
		this.inventory.load(reader);

		this.mana = reader.readInt32();
		this.manaMax = reader.readInt32();
		this.level = reader.readInt32();

		float last = this.speed;
		this.speed = reader.readFloat();

		this.numIronHearts = reader.readByte();
		this.numGoldenHearts = reader.readByte();
		this.gotHit = reader.readBoolean();

		this.maxSpeed += (this.speed - last) * 7f;

		doTp(false);
	}

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

	public void modifyMana(int a) {
		this.mana = (int) MathUtils.clamp(0, this.manaMax, this.mana + a * manaModifier);
	}

	public Inventory getInventory() {
		return this.inventory;
	}

	public int getMana() {
		return (int) this.mana;
	}

	public int getLevel() {
		return this.level;
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
}