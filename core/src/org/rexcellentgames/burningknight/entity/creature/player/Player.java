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
import org.rexcellentgames.burningknight.entity.creature.player.fx.RunFx;
import org.rexcellentgames.burningknight.entity.item.Gold;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.accessory.equipable.BlackHeart;
import org.rexcellentgames.burningknight.entity.item.accessory.equipable.ClockHeart;
import org.rexcellentgames.burningknight.entity.item.accessory.equipable.ManaShield;
import org.rexcellentgames.burningknight.entity.item.consumable.potion.HealingPotion;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.BowA;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Revolver;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.MagicMissileWand;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.book.FastBook;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.SwordA;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.butcher.ButcherA;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.morning.MorningStarA;
import org.rexcellentgames.burningknight.entity.level.Terrain;
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
	private static final float LIGHT_SIZE = 5f;
	public static Type toSet = Type.WARRIOR;
	public static float mobSpawnModifier = 1f;
	public static ArrayList<Player> all = new ArrayList<>();
	public static Player instance;
	public static Entity ladder;
	public static ShaderProgram shader;
	private static HashMap<String, Animation> skins = new HashMap<>();

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
	private ItemPickupFx pickupFx;
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

	{
		hpMax = 8;
		manaMax = 6;
		level = 1;
		mul = 0.85f;
		speed = 25;
		alwaysActive = true;

		setSkin("");
	}

	@Override
	public void initStats() {
		super.initStats();

		this.stats.put("inv_time", 1f);
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
		switch (Random.newInt(2)) {
			case 0: default: this.give(new MagicMissileWand()); break;
			case 1: this.give(new FastBook()); break;
		}
	}

	private void generateRanger() {
		switch (Random.newInt(2)) {
			case 0: default: this.give(new BowA()); break;
			case 1: this.give(new Revolver()); break;
		}
	}

	private void give(Item item) {
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

		if (this.ui != null) {
			this.ui.renderBeforePlayer(this);
		}

		boolean before = false;
		/*Item item = this.inventory.getSlot(this.inventory.active);

		if (item instanceof WeaponBase) {
			Point aim = this.getAim();
			double a = this.getAngleTo(aim.x, aim.y);
			before = (a > 0 && a < Math.PI);
		}*/

		if (this.ui != null && before) {
			this.ui.renderOnPlayer(this);
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

		if (this.ui != null && !before) {
			this.ui.renderOnPlayer(this);
		}

		Graphics.batch.setColor(1, 1, 1, 1);
		this.renderBuffs();
	}

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
		Graphics.shadow(this.x + this.hx, this.y + this.hy, this.hw, this.hh);
	}

	@Override
	public void destroy() {
		super.destroy();

		if (this.ui != null && !this.ui.done) {
			this.ui.destroy();
			this.ui = null;
		}
		Player.all.remove(this);
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
		this.body = this.createSimpleBody(3, 1, 10, 10, BodyDef.BodyType.DynamicBody, false);

		Camera.follow(this, true);

		if (ladder != null) {
			this.tp(ladder.x, ladder.y - 2);
		}
	}

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

		if (this.mana != this.manaMax) {
			this.lastMana += dt * (this.vel.len2() > 9.9f ?
				(this.flipRegenFormula ? 1f : 0.5f) :
				(this.flipRegenFormula ? 0.5f : 1f)) * this.manaRegenRate * (
					(moreManaRegenWhenLow && this.hp <= this.hpMax / 3) ? 2 : 1
				);

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

		if (Dungeon.level != null) {
			//Dungeon.level.addLightInRadius(this.x + 8, this.y + 8, 0, 0, 0, 2f, this.getLightSize(), false);
			//Room room = Dungeon.level.findRoomFor(this.x, this.y);

			if (room != null) {
				/*if (this.room != room) {
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

					if (manaRegenRoom && room.numEnemies > 0 && Random.chance(50)) {
						this.modifyMana(this.getManaMax());
					}
				}

				this.room = room;*/

				for (int x = this.room.left; x <= this.room.right; x++) {
					for (int y = this.room.top; y <= this.room.bottom; y++) {
						Dungeon.level.addLight(x * 16, y * 16, 0, 0, 0, 2f, 2f);
					}
				}
			}
		}

		/*if (l < this.mana && Float.compare(this.mana, this.manaMax) == 0) {
			Dungeon.area.add(new TextFx("Full Mana", this).setColor(Dungeon.BLUE));
		}*/

		this.watery = Math.max(0, this.watery - dt);

		if (this.dead) {
			super.common();
			return;
		}

		this.heat = Math.max(0, this.heat - dt / 3);

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
	}

	public float getLightSize() {
		return LIGHT_SIZE + this.lightModifier;
	}

	public int getManaMax() {
		return this.manaMax;
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

		return ((pauseMore && this.vel.len() < 1f) ? v * 1.5f : v) * damageModifier;
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
	protected void onHurt(float a, Creature from) {
		super.onHurt(a, from);

		Camera.shake(4f);
		Audio.playSfx("voice_gobbo_" + Random.newInt(1, 4), 1f, Random.newFloat(0.9f, 1.9f));

		if (from != null && Random.chance(this.reflectDamageChance)) {
			from.modifyHp((int) Math.ceil(a / 2), this, true);
		}

		if (this.ui.hasEquiped(BlackHeart.class) && this.room != null) {
			for (int i = Mob.all.size() - 1; i >= 0; i--) {
				Mob mob = Mob.all.get(i);

				if (mob.getRoom() == this.room) {
					mob.modifyHp(-1, this, true);
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

		this.maxSpeed += (this.speed - last) * 7f;
		
		if (ladder != null) {
			this.tp(ladder.x, ladder.y - 2);
		}
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