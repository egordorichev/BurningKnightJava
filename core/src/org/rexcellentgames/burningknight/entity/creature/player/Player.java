package org.rexcellentgames.burningknight.entity.creature.player;

import box2dLight.PointLight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.rexcellentgames.burningknight.Display;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.Settings;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Camera;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.*;
import org.rexcellentgames.burningknight.entity.creature.fx.BloodFx;
import org.rexcellentgames.burningknight.entity.creature.fx.HpFx;
import org.rexcellentgames.burningknight.entity.creature.inventory.Inventory;
import org.rexcellentgames.burningknight.entity.creature.inventory.UiInventory;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.mob.boss.Boss;
import org.rexcellentgames.burningknight.entity.creature.mob.boss.BurningKnight;
import org.rexcellentgames.burningknight.entity.creature.mob.tech.Bot;
import org.rexcellentgames.burningknight.entity.creature.player.fx.ItemPickedFx;
import org.rexcellentgames.burningknight.entity.creature.player.fx.ItemPickupFx;
import org.rexcellentgames.burningknight.entity.fx.BloodDropFx;
import org.rexcellentgames.burningknight.entity.fx.BloodSplatFx;
import org.rexcellentgames.burningknight.entity.fx.GrassBreakFx;
import org.rexcellentgames.burningknight.entity.fx.SteamFx;
import org.rexcellentgames.burningknight.entity.item.Bomb;
import org.rexcellentgames.burningknight.entity.item.Gold;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.accessory.Accessory;
import org.rexcellentgames.burningknight.entity.item.accessory.hat.Hat;
import org.rexcellentgames.burningknight.entity.item.active.ActiveItem;
import org.rexcellentgames.burningknight.entity.item.active.InfiniteBomb;
import org.rexcellentgames.burningknight.entity.item.autouse.Autouse;
import org.rexcellentgames.burningknight.entity.item.consumable.Consumable;
import org.rexcellentgames.burningknight.entity.item.entity.BombEntity;
import org.rexcellentgames.burningknight.entity.item.key.BurningKey;
import org.rexcellentgames.burningknight.entity.item.key.Key;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Gun;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.Sword;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.entities.Coin;
import org.rexcellentgames.burningknight.entity.level.entities.Entrance;
import org.rexcellentgames.burningknight.entity.level.entities.Exit;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.boss.BossRoom;
import org.rexcellentgames.burningknight.entity.level.rooms.shop.ShopRoom;
import org.rexcellentgames.burningknight.entity.level.save.GlobalSave;
import org.rexcellentgames.burningknight.entity.level.save.SaveManager;
import org.rexcellentgames.burningknight.game.Achievements;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.game.state.InGameState;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.ui.UiMap;
import org.rexcellentgames.burningknight.util.*;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Player extends Creature {
	public static Type toSet = Type.NONE;
	public static Item startingItem;
	public static float mobSpawnModifier = 1f;
	public static ArrayList<Player> all = new ArrayList<>();
	public static Player instance;
	public static Entity ladder;
	public static ShaderProgram shader;
	public static boolean showStats;
	public static String hatId;
	public static String skin;
	public static TextureRegion balloon = Graphics.getTexture("item-red_balloon");
	public static boolean sucked = false;
	public static boolean dullDamage;
	private static HashMap<String, Animation> skins = new HashMap<>();
	private static Animation headAnimations = Animation.make("actor-gobbo", "-gobbo");
	private static AnimationData headIdle = headAnimations.get("idle");
	private static AnimationData headRun = headAnimations.get("run");
	private static AnimationData headHurt = headAnimations.get("hurt");
	private static AnimationData headRoll = headAnimations.get("roll");
	private static TextureRegion wing = Graphics.getTexture("item-half_wing");
	private static TextureRegion playerTexture = Graphics.getTexture("props-gobbo_full");
	private static int offsets[] = new int[]{
		0, 0, 0, -1, -1, -1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0 // for roll
	};

	static {
		shader = new ShaderProgram(Gdx.files.internal("shaders/default.vert").readString(), Gdx.files.internal("shaders/rainbow.frag").readString());
		if (!shader.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + shader.getLog());
	}

	public Type type;
	public float heat;
	public boolean hasRedLine;
	public UiInventory ui;
	public float goldModifier = 1f;
	public int lavaResist;
	public int fireResist;
	public int sales;
	public int poisonResist;
	public int stunResist;
	public boolean seeSecrets;
	public boolean toDeath;
	public boolean drawInvt;
	public ItemPickupFx pickupFx;
	public float accuracy;
	public boolean seePath;
	public float stopT;
	public boolean rotating;
	public float al;
	public boolean hasBkKey;
	public boolean leaveSmall;
	public Vector2 orbitalRing = new Vector2();
	public int step;
	public float tt;
	public int frostLevel;
	public int flight;
	public int numCollectedHearts;
	public int burnLevel;
	public int leaveVenom;
	protected float mana;
	protected int manaMax;
	protected int level;
	private Inventory inventory;
	private String name;
	private float fa;
	private float sx = 1f;
	private float sy = 1f;
	private ArrayList<ItemHolder> holders = new ArrayList<>();
	private AnimationData idle;
	private AnimationData run;
	private AnimationData hurt;
	private AnimationData roll;
	private AnimationData killed;
	private AnimationData animation;
	private PointLight light;
	private int money;
	private int bombs;
	private int keys;
	private TextureRegion hat;
	private int numIronHearts;
	private int numGoldenHearts;
	private boolean wasFreezed;
	private boolean wasPoisoned;
	private boolean gotHit;
	private float last;
	private float lastBlood;
	private boolean teleport;
	private float zvel;
	private boolean onGround;
	private Vector2 lastGround = new Vector2();
	private boolean moved;
	private boolean rolled;
	private float lastFx = 0;

	@Override
	public void renderBuffs() {
		super.renderBuffs();
		Graphics.batch.setProjectionMatrix(Camera.game.combined);
		Graphics.batch.setProjectionMatrix(Camera.game.combined);
		Item item = this.inventory.getSlot(this.inventory.active);

		if (item instanceof Gun) {
			((Gun) item).renderReload();
		}

		if (BurningKnight.instance != null && (BurningKnight.instance.rage) && Exit.instance != null) {
			float dx = Exit.instance.x + 8 - x - w / 2;
			float dy = Exit.instance.y + 8 - y - h / 2;
			float a = (float) Math.atan2(dy, dx);

			Graphics.batch.end();
			Graphics.shape.setProjectionMatrix(Camera.game.combined);
			Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);

			float an = (float) Math.toRadians(10);
			float d = (float) (28 + Math.cos(Dungeon.time * 6) * 2.5f);
			float d2 = d + 8;

			Graphics.shape.setColor(0, 0, 0, 1);
			Graphics.shape.rectLine((float) (x + w / 2 + Math.cos(a - an) * d), (float) (y + h / 2 + Math.sin(a - an) * d), (float) (x + w / 2 + Math.cos(a) * d2), (float) (y + h / 2 + Math.sin(a) * d2), 4f);
			Graphics.shape.rectLine((float) (x + w / 2 + Math.cos(a + an) * d), (float) (y + h / 2 + Math.sin(a + an) * d), (float) (x + w / 2 + Math.cos(a) * d2), (float) (y + h / 2 + Math.sin(a) * d2), 4f);
			float v = (float) (Math.sin(Dungeon.time * 12) * 0.5f + 0.5f);
			Graphics.shape.setColor(1, v, v, 1);
			Graphics.shape.rectLine((float) (x + w / 2 + Math.cos(a - an) * d), (float) (y + h / 2 + Math.sin(a - an) * d), (float) (x + w / 2 + Math.cos(a) * d2), (float) (y + h / 2 + Math.sin(a) * d2), 2);
			Graphics.shape.rectLine((float) (x + w / 2 + Math.cos(a + an) * d), (float) (y + h / 2 + Math.sin(a + an) * d), (float) (x + w / 2 + Math.cos(a) * d2), (float) (y + h / 2 + Math.sin(a) * d2), 2);

			Graphics.shape.end();
			Graphics.batch.begin();
		}

		if (Dungeon.depth < 0) {
			return;
		}

		int count = 0;
		Mob last = null;

		for (Mob mob : Mob.all) {
			if (mob.room == this.room) {
				last = mob;
				count++;
			}
		}

		/*if (this.isFlying() && inventory.findEquipped(RedBalloon.class)) {
			float dx = Math.abs(this.acceleration.x) > 0.5f ? (this.acceleration.x > 0 ? 1 : -1) * 32 : 0;
			float dy = Math.abs(this.acceleration.y) > 0.5f ? (this.acceleration.y > 0 ? 1 : -1) * 24 : 0;

			float dt = Gdx.graphics.getDeltaTime();
			bx += (dx - bx) * dt * 3;
			by += (dy - by) * dt * 3;

			float of = (float) (Math.cos(Dungeon.time * 4) * 2.5f);
			Graphics.startAlphaShape();
			Graphics.shape.setColor(1, 1, 1, 0.5f);
			Graphics.shape.rectLine(this.x + 8, this.y + 10, this.x + 8 + bx, this.y + 20 + of + 14 + by, 1f);
			Graphics.shape.setColor(1, 1, 1, 1);
			Graphics.endAlphaShape();
			float a = -bx * 1.2f;
			Graphics.render(balloon, this.x + (16 - balloon.getRegionWidth()) / 2 + bx + balloon.getRegionWidth() / 2, this.y + of + 32 + by, a, balloon.getRegionWidth() / 2, 0, false, false);
		}*/

		if (last != null && count == 1 && !Ui.hideUi) {
			float dx = last.x + last.w / 2 - this.x - this.w / 2;
			float dy = last.y + last.h / 2 - this.y - this.h / 2;
			float d = (float) Math.sqrt(dx * dx + dy * dy);
			float a = this.getAngleTo(last.x + last.w / 2, last.y + last.h / 2);

			if (d < 48) {
				return;
			}

			d -= 32;

			float cx = Camera.game.position.x;
			float cy = Camera.game.position.y;

			float x = MathUtils.clamp(cx - Display.GAME_WIDTH / 2 + 16, cx + Display.GAME_WIDTH / 2 - 16, (float) Math.cos(a) * d + this.x + this.w / 2);
			float y = MathUtils.clamp(cy - Display.GAME_HEIGHT / 2 + 16, cy + Display.GAME_HEIGHT / 2 - 16, (float) Math.sin(a) * d + this.y + this.h / 2);

			Graphics.startAlphaShape();
			Graphics.shape.setProjectionMatrix(Camera.game.combined);
			Graphics.shape.setColor(1, 0.1f, 0.1f, 0.8f);

			a = (float) Math.atan2(y - last.y - last.w / 2, x - last.x - last.h / 2);
			float m = 10;
			float am = 0.5f;

			Graphics.shape.rectLine(x, y, x + (float) Math.cos(a - am) * m, y + (float) Math.sin(a - am) * m, 2);
			Graphics.shape.rectLine(x, y, x + (float) Math.cos(a + am) * m, y + (float) Math.sin(a + am) * m, 2);

			Graphics.endAlphaShape();
		}
	}
	private boolean rolling;
	private boolean hadEnemies;

	{
		defense = 1;
	}

	{
		hpMax = 6;
		manaMax = 8;
		level = 1;
		mul = 0.7f;
		speed = 25;
		alwaysActive = true;
		invTime = 1f;

		setSkin("body");
	}

	public Player() {
		this("player");
	}

	public Player(String name) {
		if (Player.instance != null) {
			Player.instance.done = true;
			Player.instance.destroy();
		}

		all.add(this);
		instance = this;

		Ui.ui.dead = false;
	}

	public static float getStaticMage() {
		return instance == null ? (toSet == Type.WIZARD ? 1f : 0.1f) : instance.getMage();
	}

	public float getMage() {
		return this.type == Type.WIZARD ? 1f : 0.1f;
	}

	public static float getStaticWarrior() {
		return instance == null ? (toSet == Type.WARRIOR ? 1f : 0.1f) : instance.getWarrior();
	}

	public float getWarrior() {
		return this.type == Type.WARRIOR ? 1f : 0.1f;
	}	@Override
	public void setHpMax(int hpMax) {
		super.setHpMax(hpMax);

		if (this.hpMax >= 16) {
			Achievements.unlock(Achievements.GET_8_HEART_CONTAINERS);
		}
	}

	public static float getStaticRanger() {
		return instance == null ? (toSet == Type.RANGER ? 1f : 0.1f) : instance.getRanger();
	}

	public float getRanger() {
		return this.type == Type.RANGER ? 1f : 0.1f;
	}

	public int getKeys() {
		return keys;
	}

	public void setKeys(int money) {
		this.keys = Math.min(99, money);
	}	public void resetHit() {
		gotHit = false;
	}

	public int getBombs() {
		return bombs;
	}

	public void setBombs(int money) {
		this.bombs = Math.min(99, money);
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;

		if (money >= 100) {
			Achievements.unlock(Achievements.UNLOCK_MONEY_PRINTER);
		}

		if (money >= 300) {
			Achievements.unlock(Achievements.COLLECT_300_GOLD);
		}
	}

	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public void renderShadow() {
		float z = this.z;
		boolean flying = false;

		/*if (this.isFlying() && this.inventory.findEquipped(Wings.class)) {
			z -= (float) (Math.cos(Dungeon.time * 9) * 1.5f);
			flying = true;
		}*/

		Graphics.shadow(this.x + this.hx, this.y - (flying ? 3 : 0), this.hw, this.hh, z);
	}

	public void setSkin(String add) {
		Animation animations;
		skin = add;

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
		roll = animations.get("roll");
		killed = animations.get("dead");
		animation = this.idle;
	}	@Override
	public void destroy() {
		super.destroy();
		World.removeLight(light);

		if (UiMap.instance != null) {
			UiMap.instance.remove();
		}

		hasBkKey = false;
		Player.all.remove(this);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}	@Override
	public void init() {
		super.init();
		invt = 0.5f;
		al = 0;
		rotating = false;

		Tween.to(new Tween.Task(0, 0.1f) {
			@Override
			public void onEnd() {
				super.onEnd();
				Camera.follow(Player.instance, true);
			}
		});

		/*if (Dungeon.depth == -2) {
			Achievements.unlock(Achievements.TUTORIAL_DONE);
		}*/

		t = 0;

		if (toSet != Type.NONE) {
			this.type = toSet;
			toSet = Type.NONE;
		} else if (this.type == null) {
			this.type = Type.WARRIOR;
		}

		if (instance == null) {
			instance = this;
		}

		this.mana = this.manaMax;
		this.inventory = new Inventory(this);
		this.body = this.createSimpleBody(3, 0, 10, 11, BodyDef.BodyType.DynamicBody, false);

		doTp(true);

		switch (this.type) {
			case WARRIOR:
			case WIZARD:
				this.accuracy -= 5;
				break;
		}

		light = World.newLight(256, new Color(1, 1, 1, 1f), 180, x, y);
		light.setPosition(this.x + 8, this.y + 8);
		light.attachToBody(this.body, 8, 8, 0);
		light.setIgnoreAttachedBody(true);

		if (Dungeon.depth == -3) {
			this.inventory.clear();

			this.hpMax = 10;
			this.hp = 10;
			this.give(new Sword());

			Player.instance.tp(Spawn.instance.x, Spawn.instance.y);
		}

		Camera.follow(this, true);
	}

	public void generate() {
		this.inventory.clear();

		bombs = 1;
		keys = 0;
		money = 0;

		if (Dungeon.depth == -3) {
			this.hpMax = 12;
			this.hp = 12;
			this.give(new Sword());
		} else {
			if (startingItem != null) {
				this.give(startingItem);
				startingItem = null;
			} else {
				this.give(new Sword());
			}

			if (this.type != Type.WIZARD) {
				this.manaMax -= 2;
				this.mana -= 2;
			}

			if (this.type == Type.RANGER) {
				this.hpMax = 4;
				this.hp = 4;
			}
		}

		if (hatId != null) {
			this.give(ItemPickupFx.setSkin(hatId));
		} else {
			String id = GlobalSave.getString("last_hat", null);
			this.setHat(id);
		}

		if (Random.getSeed().equals("HP")) {
			this.hpMax = 12;
			this.hp = 12;
		} else if (Random.getSeed().equals("DIE")) {
			this.hpMax = 2;
			this.hp = 2;
		} else if (Random.getSeed().equals("BOMB")) {
			this.bombs = 99;
		} else if (Random.getSeed().equals("KEY")) {
			this.keys = 99;
		} else if (Random.getSeed().equals("GOLD")) {
			this.money = 999;
		}
	}

	public void give(Item item) {
		if (item instanceof Hat) {
			this.inventory.setSlot(3, item);
			item.setOwner(this);
			((Accessory) item).onEquip(false);
		} else {
			this.inventory.add(new ItemHolder(item));
		}
	}	private void doTp(boolean fromInit) {
		if (this.teleport) {
			this.tp(this.lastGround.x, this.lastGround.y);
			return;
		}

		if (Dungeon.depth == -3) {

		} else if (Dungeon.depth == -1) {
			Room room = Dungeon.level.getRooms().get(0);
			this.tp((room.left + room.getWidth() / 2) * 16 - 8, room.top * 16 + 32);
		} else if (ladder != null && (Dungeon.loadType != Entrance.LoadType.LOADING
			|| (!fromInit && (Dungeon.level.findRoomFor(this.x + this.w / 2, this.y) == null)))) {
			this.tp(ladder.x, ladder.y - 4);
		} else if (ladder == null) {
			Log.error("Null lader!");
		}

		Vector3 vec = Camera.game.project(new Vector3(Player.instance.x + Player.instance.w / 2, Player.instance.y + Player.instance.h / 2, 0));
		vec = Camera.ui.unproject(vec);
		vec.y = Display.GAME_HEIGHT - vec.y / Display.UI_SCALE;

		Dungeon.darkX = vec.x / Display.UI_SCALE;
		Dungeon.darkY = vec.y;
	}

	public void setHat(String name) {
		if (name == null || name.isEmpty()) {
			hat = null;
			return;
		}

		GlobalSave.put("last_hat", name);
		hatId = name;
		this.hat = Graphics.getTexture("hat-" + name + "-idle-00");
	}	@Override
	public void tp(float x, float y) {
		super.tp(x, y);
		Camera.follow(this, true);
		orbitalRing.x = this.x + this.w / 2;
		orbitalRing.y = this.y + this.h / 2;
	}

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

	public void setUi(UiInventory ui) {
		this.ui = ui;
	}	@Override
	public boolean isUnhittable() {
		return super.isUnhittable() || this.rolling;
	}

	public void modifyManaMax(int a) {
		this.manaMax += a;
		this.modifyMana(0);
	}

	public void modifyMana(int a) {
		this.mana = (int) MathUtils.clamp(0, this.manaMax, this.mana + a);
	}

	@Override
	public void render() {
		// fixme: player head blink white when hurt too (hat / body head)
		Graphics.batch.setColor(1, 1, 1, this.a);

		float offset = 0;

		/*if (this.isFlying() && this.inventory.findEquipped(Wings.class)) {
			float a = (float) Math.cos(Dungeon.time * 9) * 40f - 10f;
			offset = (float) -(Math.cos(Dungeon.time * 9) * 1.5f);
			Graphics.render(wing, this.x + 12, this.y + 4 + offset, a, 0, 2, false, false);
			Graphics.render(wing, this.x + 4, this.y + 4 + offset, -a, 0, 2, false, false, -1, 1);
		}*/

		if (this.rotating) {
			this.al += Gdx.graphics.getDeltaTime() * 960;
			Graphics.render(playerTexture, this.x + 6.5f, this.y + 2.5f, this.al, 6.5f, 2.5f, false, false);
			Graphics.batch.setColor(1, 1, 1, 1);
		} else {
			if (this.rolling) {
				this.animation = roll;
			} else if (this.invt > 0) {
				this.animation = hurt;
				hurt.setFrame(0);
			} else if (!this.isFlying() && this.state.equals("run")) {
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
			} else if (!this.isFlying() && this.state.equals("run")) {
				id += 8;
			}

			if (this.ui != null && !isRolling()) {
				this.ui.renderBeforePlayer(this, of);
			}

			boolean shade = (this.drawInvt && this.invtt > 0) || (invt > 0 && invt % 0.2f > 0.1f);
			TextureRegion region = this.animation.getCurrent().frame;

			if (shade) {
				Texture texture = region.getTexture();

				Graphics.batch.end();
				shader.begin();
				shader.setUniformf("time", Dungeon.time);
				shader.setUniformf("pos", new Vector2((float) region.getRegionX() / texture.getWidth(), (float) region.getRegionY() / texture.getHeight()));
				shader.setUniformf("size", new Vector2((float) region.getRegionWidth() / texture.getWidth(), (float) region.getRegionHeight() / texture.getHeight()));
				shader.setUniformf("a", this.a);
				shader.setUniformf("white", invt > 0 ? 1 : 0);
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

			float angle = 0;

			this.animation.render(this.x - region.getRegionWidth() / 2 + 8,
				this.y + this.z + offset, false, false, region.getRegionWidth() / 2,
				0, angle, this.sx * (this.flipped ? -1 : 1), this.sy);

			if (this.hat != null && !this.isRolling()) {
				Graphics.render(this.hat, this.x + w / 2 - (this.flipped ? -1 : 1) * 7, this.y + 1 + this.z + offsets[id] + region.getRegionHeight() / 2 - 2 + offset,
					angle, region.getRegionWidth() / 2, 0, false, false, this.sx * (this.flipped ? -1 : 1), this.sy);
			} else {
				AnimationData anim = headIdle;

				if (this.rolling) {
					anim = headRoll;
				} else if (this.invt > 0) {
					anim = headHurt;
				} else if (this.state.equals("run") && !this.isFlying()) {
					anim = headRun;
				}

				anim.setFrame(this.animation.getFrame());
				region = anim.getCurrent().frame;

				anim.render(this.x - region.getRegionWidth() / 2 + 8,
					this.y + this.z + offset, false, false, region.getRegionWidth() / 2,
					0, angle, this.sx * (this.flipped ? -1 : 1), this.sy);
			}

			Graphics.batch.setColor(1, 1, 1, 1);

			if (shade || this.fa > 0) {
				Graphics.batch.end();
				Graphics.batch.setShader(null);
				Graphics.batch.begin();
			}

			if (!this.rolling && this.ui != null && Dungeon.depth != -2) {
				this.ui.renderOnPlayer(this, of + offset);
			}
		}

		Graphics.batch.setColor(1, 1, 1, 1);
	}

	public boolean isRolling() {
		return this.rolling;
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof ItemHolder) {
			ItemHolder item = (ItemHolder) entity;

			if (item.getItem() instanceof Coin) {
				item.remove();
				item.done = true;

				Tween.to(new Tween.Task(20, 0.2f, Tween.Type.BACK_OUT) {
					@Override
					public float getValue() {
						return Ui.y;
					}

					@Override
					public void setValue(float value) {
						Ui.y = value;
					}

					@Override
					public void onEnd() {
						Tween.to(new Tween.Task(0, 0.1f) {
							@Override
							public void onEnd() {
								GlobalSave.put("num_coins", GlobalSave.getInt("num_coins") + 1);
							}
						}).delay(0.5f);

						Tween.to(new Tween.Task(0, 0.2f) {
							@Override
							public float getValue() {
								return Ui.y;
							}

							@Override
							public void setValue(float value) {
								Ui.y = value;
							}

							@Override
							public void onStart() {
								if (Ui.y < 20) {
									deleteSelf();
								}
							}
						}).delay(3.1f);
					}
				});

				for (int i = 0; i < 10; i++) {
					PoofFx fx = new PoofFx();

					fx.x = item.x + item.w / 2;
					fx.y = item.y + item.h / 2;

					Dungeon.area.add(fx);
				}
			} else if (!item.getItem().shop && (item.getItem().hasAutoPickup() || item.auto)) {
				if (this.tryToPickup(item) && !item.auto) {
					if (!(item.getItem() instanceof Gold)) {
						this.area.add(new ItemPickedFx(item));
					}

					item.done = true;
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
			if (this.frostLevel > 0) {
				((Mob) entity).addBuff(new FrozenBuff());
			}

			if (this.burnLevel > 0) {
				((Mob) entity).addBuff(new BurningBuff());
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

			if (this.holders.size() > 0 && !Ui.hideUi) {
				this.pickupFx = new ItemPickupFx(this.holders.get(0), this);
				this.area.add(this.pickupFx);
			}
		}
	}

	public boolean tryToPickup(ItemHolder item) {
		if (!item.done) {
			if (item.getItem() instanceof Bomb && !(item.getItem() instanceof InfiniteBomb)) {
				setBombs(bombs + item.getItem().getCount());
				item.getItem().onPickup();
				item.remove();
				item.done = true;
				this.playSfx("pickup_item");

				for (int j = 0; j < 3; j++) {
					PoofFx fx = new PoofFx();

					fx.x = item.x + item.w / 2;
					fx.y = item.y + item.h / 2;

					Dungeon.area.add(fx);
				}

				return true;
			} else if (item.getItem() instanceof Gold) {
				setMoney(money + item.getItem().getCount());
				item.getItem().onPickup();
				item.remove();
				item.done = true;
				this.playSfx("pickup_item");

				for (int j = 0; j < 3; j++) {
					PoofFx fx = new PoofFx();

					fx.x = item.x + item.w / 2;
					fx.y = item.y + item.h / 2;

					Dungeon.area.add(fx);
				}

				return true;
			} else if (item.getItem() instanceof Key && !(item.getItem() instanceof BurningKey)) {
				setKeys(keys + item.getItem().getCount());
				item.getItem().onPickup();
				item.remove();
				item.done = true;
				this.playSfx("pickup_item");

				for (int j = 0; j < 3; j++) {
					PoofFx fx = new PoofFx();

					fx.x = item.x + item.w / 2;
					fx.y = item.y + item.h / 2;

					Dungeon.area.add(fx);
				}

				return true;
			} else if (item.getItem() instanceof WeaponBase) {
				if (inventory.isEmpty(0)) {
					inventory.setSlot(0, item.getItem());

					item.getItem().setOwner(this);
					item.getItem().onPickup();
					item.remove();
					item.done = true;

					this.playSfx("pickup_item");

					for (int j = 0; j < 3; j++) {
						PoofFx fx = new PoofFx();

						fx.x = item.x + item.w / 2;
						fx.y = item.y + item.h / 2;

						Dungeon.area.add(fx);
					}

					return true;
				} else if (inventory.isEmpty(1)) {
					inventory.setSlot(1, item.getItem());
					item.getItem().setOwner(this);
					item.getItem().onPickup();

					item.remove();
					item.done = true;
					this.playSfx("pickup_item");

					for (int j = 0; j < 3; j++) {
						PoofFx fx = new PoofFx();

						fx.x = item.x + item.w / 2;
						fx.y = item.y + item.h / 2;

						Dungeon.area.add(fx);
					}

					return true;
				} else {
					Item it = item.getItem();
					item.setItem(this.inventory.getSlot(this.inventory.active));
					this.inventory.setSlot(this.inventory.active, it);
					it.setOwner(this);
					it.onPickup();
					this.playSfx("pickup_item");
					return false;
				}
			} else if (item.getItem() instanceof ActiveItem || (item.getItem() instanceof Consumable && !(item.getItem() instanceof Autouse))) {
				if (inventory.getSlot(2) == null) {
					inventory.setSlot(2, item.getItem());

					item.getItem().setOwner(this);
					item.getItem().onPickup();
					item.remove();
					item.done = true;
					this.playSfx("pickup_item");

					for (int j = 0; j < 3; j++) {
						PoofFx fx = new PoofFx();

						fx.x = item.x + item.w / 2;
						fx.y = item.y + item.h / 2;

						Dungeon.area.add(fx);
					}

					return true;
				} else {
					Item it = item.getItem();
					item.setItem(this.inventory.getSlot(2));
					this.inventory.setSlot(2, it);
					it.setOwner(this);
					it.onPickup();
					this.playSfx("pickup_item");
					return false;
				}
			} else {
				Item it = item.getItem();
				it.setOwner(this);
				it.onPickup();
				this.playSfx("pickup_item");
				inventory.add(item);
				return true;
			}
		}

		return false;
	}

	public boolean didGetHit() {
		return gotHit;
	}	@Override
	protected void common() {
		super.common();
	}

	public int getManaMax() {
		return this.manaMax;
	}

	public Inventory getInventory() {
		return this.inventory;
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		light.setActive(true);
		light.attachToBody(body, 8, 8, 0);
		light.setPosition(x + 8, y + 8);
		light.setDistance(180);

		if (this.hasBuff(Buffs.BURNING)) {
			this.light.setColor(1, 0.5f, 0f, 1);
		} else {
			this.light.setColor(1, 1, 0.8f, 1);
		}

		if (!this.rolling) {
			if (this.isFlying() || this.touches[Terrain.WALL] || this.touches[Terrain.FLOOR_A] || this.touches[Terrain.FLOOR_B] || this.touches[Terrain.FLOOR_C] || this.touches[Terrain.FLOOR_D] || this.touches[Terrain.DISCO]) {
				this.onGround = true;
				this.lastGround.x = this.x;
				this.lastGround.y = this.y;
			}

			if (!this.onGround) {
				this.teleport = true;

				for (int i = 0; i < 5; i++) {
					PoofFx fx = new PoofFx();

					fx.x = this.x + this.w / 2;
					fx.y = this.y + this.h / 2;

					Dungeon.area.add(fx);
				}

				this.doTp(false);

				for (int i = 0; i < 5; i++) {
					PoofFx fx = new PoofFx();

					fx.x = this.x + this.w / 2;
					fx.y = this.y + this.h / 2;

					Dungeon.area.add(fx);
				}

				this.teleport = false;
				this.modifyHp(-1, null, true);
			}

			this.onGround = false;
		}

		this.z = Math.max(0, this.zvel * dt + this.z);
		this.zvel = this.zvel - dt * 220;

		orbitalRing.lerp(new Vector2(this.x + this.w / 2, this.y + this.h / 2), 4 * dt);

		if (this.toDeath) {
			this.t += dt;
			this.animation.update(dt * (this.flipped != this.acceleration.x < 0 && this.animation == run ? -1 : 1));

			if (this.t >= 1f) {
				Ui.ui.dead = true;
				super.die(false);

				this.dead = true;
				this.done = true;

				Camera.shake(10);
				this.remove();

				deathEffect(killed);
				BloodFx.add(this, 20);

				ArrayList<Item> items = new ArrayList<>();

				for (int i = 0; i < 3; i++) {
					if (inventory.getSlot(i) != null) {
						items.add(inventory.getSlot(i));
					}
				}

				for (int i = 0; i < inventory.getSpace(); i++) {
					items.add(inventory.getSpace(i));
				}

				for (Item item : items) {
					ItemHolder holder = new ItemHolder();
					holder.x = this.x + Random.newFloat(16);
					holder.y = this.y + Random.newFloat(16);
					holder.randomVelocity();
					holder.setItem(item);
					Dungeon.area.add(holder);
				}

				SaveManager.delete();
				inventory.clear();
			}

			return;
		}

		if (this.mana != this.manaMax) {
			boolean dark = Player.instance.isDead();

			if (!dark) {
				dark = Boss.all.size() > 0 && Player.instance.room instanceof BossRoom && !BurningKnight.instance.rage;

				if (!dark) {
					for (Mob mob : Mob.all) {
						if (mob.room == Player.instance.room) {
							dark = true;
							break;
						}
					}
				}
			}
		}

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

			if (this.last >= 1f && Settings.blood) {
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

		if (!sucked && Dialog.active == null && !this.freezed && !UiMap.large) {
			if (!this.rolling) {
				if (Input.instance.isDown("left")) {
					this.acceleration.x -= this.speed;
				}

				if (Input.instance.isDown("right")) {
					this.acceleration.x += this.speed;
				}

				if (Input.instance.isDown("up")) {
					this.acceleration.y += this.speed;
				}

				if (Input.instance.isDown("down")) {
					this.acceleration.y -= this.speed;
				}

				Vector2 move = Input.instance.getAxis("move");

				if (move.len2() > 0.2f) {
					this.acceleration.x += move.x * this.speed;
					this.acceleration.y -= move.y * this.speed; // Inverted!
				}
			}

			if (!this.rolling) {
				if (Input.instance.wasPressed("roll")) {
					rolled = true;

					float f = 80;
					ignoreAcceleration = true;

					if (acceleration.len() > 1f) {
						double a = (Math.atan2(acceleration.y, acceleration.x));

						acceleration.x = (float) Math.cos(a) * speed * f;
						acceleration.y = (float) Math.sin(a) * speed * f;
					} else {
						double a = (getAngleTo(Input.instance.worldMouse.x, Input.instance.worldMouse.y));

						acceleration.x = (float) Math.cos(a) * speed * f;
						acceleration.y = (float) Math.sin(a) * speed * f;
					}

					for (int i = 0; i < 3; i++) {
						PoofFx fx = new PoofFx();

						fx.x = this.x + this.w / 2;
						fx.y = this.y + this.h / 2;
						fx.t = 0.5f;

						Dungeon.area.add(fx);
					}
					playSfx("dash_short");

					final Player self = this;

					Tween.to(new Tween.Task(0, 0.2f) {
						@Override
						public void onEnd() {
							removeBuff(Buffs.BURNING);

							ignoreAcceleration = false;
							self.velocity.x = 0;
							self.velocity.y = 0;

							Tween.to(new Tween.Task(0, 0) {
								@Override
								public void onStart() {
									rolling = false;
								}

								@Override
								public void onEnd() {
									super.onEnd();
									animation = idle;
								}
							}).delay(0.05f);
						}
					}).delay(0.05f);

					this.rolling = true;
					this.velocity.x = 0;
					this.velocity.y = 0;
				}
			}
		} else if (Dialog.active != null) {
			if (Input.instance.wasPressed("interact")) {
				Dialog.active.skip();
			}
		}

		float v = this.acceleration.len2();

		if (knockback.len() + v > 4f) {
			this.stopT = 0;
		} else {
			stopT += dt;
		}

		if (v > 20) {
			this.become("run");
		} else {
			this.become("idle");
		}

		super.common();

		if (this.animation != null && !this.freezed) {
			if (this.animation.update(dt)) {

			}
		}

		if (this.isRolling()) {
			lastFx += dt;

			if (lastFx >= 0.05f) {
				PoofFx fx = new PoofFx();

				fx.x = this.x + this.w / 2;
				fx.y = this.y + this.h / 2;
				fx.t = 0.5f;

				Dungeon.area.add(fx);
				lastFx = 0;
			}
		}

		if (!this.freezed) {
			float dx = this.x + this.w / 2 - Input.instance.worldMouse.x;
			this.flipped = dx >= 0;
		}

		int i = Level.toIndex(Math.round((this.x) / 16), Math.round((this.y + this.h / 2) / 16));

		if (this.burnLevel > 0) {
			Dungeon.level.setOnFire(i, true);
		}

		if (this.frostLevel > 0) {
			Dungeon.level.freeze(i);

			if (this.frostLevel >= 4) {
				if (Dungeon.level.liquidData[i] == Terrain.LAVA) {
					Dungeon.level.set(i, Terrain.ICE);
					Dungeon.level.updateTile(Level.toX(i), Level.toY(i));
				}
			}
		}
	}

	public int getMana() {
		return (int) this.mana;
	}

	public int getLevel() {
		return this.level;
	}

	public enum Type {
		WARRIOR,
		WIZARD,
		RANGER,
		NONE
	}

	public static Type getTypeFromId(int id) {
		switch (id) {
			case 0: return Type.WARRIOR;
			case 1: return Type.WIZARD;
			case 2: return Type.RANGER;
			default: return Type.NONE;
		}
	}

	public static int getTypeId(Type type) {
		switch (type) {
			case WARRIOR: return 0;
			case WIZARD: return 1;
			case RANGER: return 2;
			default: return 3;
		}
	}

	@Override
	public boolean isFlying() {
		return flight > 0 || this.rolling;
	}

	@Override
	protected void onTouch(short t, int x, int y, int info) {
		if (t == Terrain.WATER && !this.isFlying()) {
			if (this.hasBuff(Buffs.BURNING)) {
				int num = GlobalSave.getInt("num_fire_out") + 1;
				GlobalSave.put("num_fire_out", num);

				if (num >= 50) {
					Achievements.unlock(Achievements.UNLOCK_WATER_BOLT);
				}

				this.removeBuff(Buffs.BURNING);

				for (int i = 0; i < 20; i++) {
					SteamFx fx = new SteamFx();

					fx.x = this.x + Random.newFloat(this.w);
					fx.y = this.y + Random.newFloat(this.h);

					Dungeon.area.add(fx);
				}
			}

			if (this.leaveVenom > 0) {
				Dungeon.level.venom(x, y);
			}
		} else {
			if (!this.isFlying() && BitHelper.isBitSet(info, 0) && !this.hasBuff(Buffs.BURNING)) {
				this.addBuff(new BurningBuff());
			}

			if (t == Terrain.LAVA && !this.isFlying() && this.lavaResist == 0) {
				this.modifyHp(-1, null, true);
				this.addBuff(new BurningBuff());

				if (this.isDead()) {
					Achievements.unlock(Achievements.UNLOCK_WINGS);
				}
			} else if (!this.isFlying() && (t == Terrain.HIGH_GRASS || t == Terrain.HIGH_DRY_GRASS)) {
				Dungeon.level.set(x, y, t == Terrain.HIGH_GRASS ? Terrain.GRASS : Terrain.DRY_GRASS);

				/*
				if (Random.chance(10)) {
					ItemHolder holder = new ItemHolder(new GrassSeed());

					holder.x = x * 16 + (16 - holder.w) / 2;
					holder.y = y * 16 + (16 - holder.h) / 2;

					Dungeon.area.add(holder.add());
				}*/

				for (int i = 0; i < 10; i++) {
					GrassBreakFx fx = new GrassBreakFx();

					fx.x = x * 16 + Random.newFloat(16);
					fx.y = y * 16 + Random.newFloat(16) - 8;

					Dungeon.area.add(fx);
				}
			} else if (!this.isFlying() && t == Terrain.VENOM) {
				this.addBuff(new PoisonedBuff());
			}
		}
	}





	@Override
	protected void onRoomChange() {
		super.onRoomChange();
		Bot.data.clear();

		InGameState.checkMusic();

		if (Dungeon.depth > -1) {
			if (numCollectedHearts >= 6) {
				Achievements.unlock(Achievements.UNLOCK_MEATBOY);
			}

			if (hadEnemies && !gotHit) {
				Achievements.unlock(Achievements.UNLOCK_HALO);
			}
		}

		this.resetHit();

		if (this.room != null) {
			if (this.room instanceof ShopRoom) {
				Audio.play("Shopkeeper");

				if (BurningKnight.instance != null && !BurningKnight.instance.getState().equals("unactive")) {
					BurningKnight.instance.become("await");
				}
			}

			hadEnemies = false;

			/*
			for (Trader trader : Trader.all) {
				if (trader.room == this.room) {
					trader.become("hi");
				}
			}*/
		}

		this.checkSecrets();

		if (room != null) {
			int count = 0;

			for (Mob mob : Mob.all) {
				if (mob.room == room) {
					count++;
				}
			}

			if (count > 0) {
				this.invt = Math.max(this.invt, 1f);
			}
		}
	}

	public void checkSecrets() {
		if (this.seeSecrets) {
			if (room != null) {
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
		}
	}

	@Override
	public HpFx modifyHp(int amount, Creature from) {
		if (amount > 0 && this.hp + amount > 1) {
			Tween.to(new Tween.Task(0, 0.4f) {
				@Override
				public float getValue() {
					return Dungeon.blood;
				}

				@Override
				public void setValue(float value) {
					Dungeon.blood = value;
				}
			});
		}

		return super.modifyHp(amount, from);
	}

	@Override
	protected boolean ignoreWater() {
		return slowLiquidResist > 0;
	}

	@Override
	protected void checkDeath() {
		if (this.hp == 0 && this.numIronHearts == 0 && this.numGoldenHearts == 0) {
			this.shouldDie = true;
		}
	}



	@Override
	public float rollDamage() {
		return getDamageModifier();
	}

	public float getDamageModifier() {
		return 1;
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

			for (int i = 0; i < 10; i++) {
				PoofFx fx = new PoofFx();

				fx.x = this.x + this.w / 2;
				fx.y = this.y + this.h / 2;

				Dungeon.area.add(fx);
			}

			for (Mob mob : Mob.all) {
				if (mob.room == this.room) {
					mob.addBuff(new FrozenBuff().setDuration(10));
				}
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
	public void onHurt(int a, Entity from) {
		super.onHurt(a, from);

		this.gotHit = true;

		Dungeon.flash(Color.WHITE, 0.05f);
		Camera.shake(4f);
		Audio.playSfx("voice_gobbo_" + Random.newInt(1, 4), 1f, Random.newFloat(0.9f, 1.9f));
	}

	@Override
	protected void die(boolean force) {
		if (this.toDeath) {
			return;
		}

		UiMap.instance.hide();
		Ui.ui.onDeath();

		this.done = false;
		int num = GlobalSave.getInt("deaths") + 1;
		GlobalSave.put("deaths", num);

		Vector3 vec = Camera.game.project(new Vector3(this.x + this.w / 2, this.y + this.h / 2, 0));
		vec = Camera.ui.unproject(vec);
		vec.y = Display.UI_HEIGHT - vec.y;

		Dungeon.shockTime = 0;
		Dungeon.shockPos.x = (vec.x) / Display.UI_WIDTH;
		Dungeon.shockPos.y = (vec.y) / Display.UI_HEIGHT;

		this.toDeath = true;
		this.t = 0;
		Dungeon.slowDown(0.5f, 1f);

		if (Dungeon.depth != -3) {
			Achievements.unlock(Achievements.DIE);
		}

		if (num >= 50) {
			Achievements.unlock(Achievements.UNLOCK_ISAAC_HEAD);
		}
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		this.inventory.save(writer);

		writer.writeInt32((int) this.mana);
		writer.writeInt32(this.manaMax);
		writer.writeInt32(this.level);
		writer.writeFloat(this.speed);

		writer.writeByte((byte) numIronHearts);
		writer.writeByte((byte) numGoldenHearts);
		writer.writeBoolean(this.gotHit);
		// writer.writeString(hatId);

		writer.writeByte((byte) this.bombs);
		writer.writeByte((byte) this.keys);
		writer.writeInt16((short) this.money);

		writer.writeByte((byte) getTypeId(this.type));
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.inventory.load(reader);

		reader.readInt32(); // mana
		this.manaMax = reader.readInt32();
		this.mana = manaMax;
		this.level = reader.readInt32();

		float last = this.speed;
		this.speed = reader.readFloat();

		this.numIronHearts = reader.readByte();
		this.numGoldenHearts = reader.readByte();
		this.gotHit = reader.readBoolean();

		this.maxSpeed += (this.speed - last) * 7f;
		this.setHat(null);

		doTp(false);

		hasBkKey = this.inventory.find(BurningKey.class);

		onRoomChange();

		this.bombs = reader.readByte();
		this.keys = reader.readByte();
		this.money = reader.readInt16();

		light.setPosition(this.x + 8, this.y + 8);
		light.attachToBody(this.body, 8, 8, 0);

		this.type = getTypeFromId(reader.readByte());
	}

	@Override
	protected boolean canHaveBuff(Buff buff) {
		if ((this.rolling || fireResist > 0) && buff instanceof BurningBuff) {
			return false;
		} else if (poisonResist > 0 && buff instanceof PoisonedBuff) {
			return false;
		} else if ((this.rolling || stunResist > 0) && buff instanceof FrozenBuff) {
			return false;
		}

		return super.canHaveBuff(buff);
	}
}