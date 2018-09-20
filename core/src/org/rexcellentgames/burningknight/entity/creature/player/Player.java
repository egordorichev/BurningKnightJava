package org.rexcellentgames.burningknight.entity.creature.player;

import com.badlogic.gdx.Gdx;
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
import org.rexcellentgames.burningknight.entity.creature.mob.boss.BurningKnight;
import org.rexcellentgames.burningknight.entity.creature.npc.Trader;
import org.rexcellentgames.burningknight.entity.creature.player.fx.ItemPickedFx;
import org.rexcellentgames.burningknight.entity.creature.player.fx.ItemPickupFx;
import org.rexcellentgames.burningknight.entity.fx.BloodDropFx;
import org.rexcellentgames.burningknight.entity.fx.BloodSplatFx;
import org.rexcellentgames.burningknight.entity.fx.GrassBreakFx;
import org.rexcellentgames.burningknight.entity.fx.SteamFx;
import org.rexcellentgames.burningknight.entity.item.Gold;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.accessory.Accessory;
import org.rexcellentgames.burningknight.entity.item.accessory.equippable.*;
import org.rexcellentgames.burningknight.entity.item.accessory.hat.VikingHat;
import org.rexcellentgames.burningknight.entity.item.consumable.potion.HealingPotion;
import org.rexcellentgames.burningknight.entity.item.entity.BombEntity;
import org.rexcellentgames.burningknight.entity.item.permanent.ExtraHeart;
import org.rexcellentgames.burningknight.entity.item.permanent.MoreGold;
import org.rexcellentgames.burningknight.entity.item.permanent.StartWithHealthPotion;
import org.rexcellentgames.burningknight.entity.item.permanent.StartingArmor;
import org.rexcellentgames.burningknight.entity.item.plant.seed.GrassSeed;
import org.rexcellentgames.burningknight.entity.item.weapon.axe.Axe;
import org.rexcellentgames.burningknight.entity.item.weapon.bow.Bow;
import org.rexcellentgames.burningknight.entity.item.weapon.dagger.Dagger;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.Revolver;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.MagicMissileWand;
import org.rexcellentgames.burningknight.entity.item.weapon.magic.book.FastBook;
import org.rexcellentgames.burningknight.entity.item.weapon.spear.Spear;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.Butcher;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.MorningStar;
import org.rexcellentgames.burningknight.entity.item.weapon.sword.Sword;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.entities.ClassSelector;
import org.rexcellentgames.burningknight.entity.level.entities.Coin;
import org.rexcellentgames.burningknight.entity.level.entities.Entrance;
import org.rexcellentgames.burningknight.entity.level.entities.Exit;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.shop.ShopRoom;
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
	public static Type toSet = Type.WARRIOR;
	public static float mobSpawnModifier = 1f;
	public static ArrayList<Player> all = new ArrayList<>();
	public static Player instance;
	public static Entity ladder;
	private static HashMap<String, Animation> skins = new HashMap<>();
	public static ShaderProgram shader;
	public static boolean showStats;

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
	public float heat;
	public boolean hasRedLine;
	public float defenseModifier = 1f;
	public UiInventory ui;
	public boolean moreManaRegenWhenLow;
	public ArrayList<UiBuff> uiBuffs = new ArrayList<>();
	public float poisonChance;
	public float burnChance;
	public float freezeChance;
	public float reflectDamageChance;
	public float thornDamageChance;
	public float regen = 1;
	public float goldModifier = 1f;
	public float vampire;
	public int lavaResist;
	public int fireResist;
	public int poisonResist;
	public int stunResist;
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
	private AnimationData roll;
	private AnimationData killed;
	private AnimationData animation;
	public float accuracy;
	public static boolean seeMore;

	{
		hpMax = 8;
		manaMax = 6;
		level = 1;
		mul = 0.7f;
		speed = 30;
		alwaysActive = true;

		setSkin("body");
	}

	public boolean seePath;

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

		if (GlobalSave.isTrue(MoreGold.ID)) {
			this.goldModifier += 1.3f;
		}
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
	private static AnimationData headRoll = headAnimations.get("roll");

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
		roll = animations.get("roll");
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

		if (GlobalSave.isTrue(StartWithHealthPotion.ID)) {
			this.give(new HealingPotion());
		}

		if (GlobalSave.isTrue(StartingArmor.ID)) {
			this.give(new VikingHat());
		}

		if (GlobalSave.isTrue(ExtraHeart.ID)) {
			this.hpMax += 2;
			this.hp += 2;
		}
	}

	private void generateWarrior() {
		this.manaMax = 4;
		this.mana = 4;

		switch (Random.newInt(5)) {
			case 0:
			default:
				this.give(new Sword());
				break;
			case 1:
				this.give(new Butcher());
				break;
			case 2:
				this.give(new MorningStar());
				break;
			case 3:
				this.give(new Dagger());
				break;
			case 4:
				this.give(new Spear());
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
		this.manaMax = 4;
		this.mana = 4;

		switch (Random.newInt(3)) {
			case 0: default: this.give(new Bow()); break;
			case 1: this.give(new Revolver()); break;
			case 2: this.give(new Axe()); break;
		}
	}

	public void give(Item item) {
		if (item instanceof Accessory) {
			this.inventory.setSlot(6, item);
			item.setOwner(this);
			((Accessory) item).onEquip(false);
		} else {
			this.inventory.add(new ItemHolder(item));
		}
	}

	public void setUi(UiInventory ui) {
		this.ui = ui;
	}

	public void modifyManaMax(int a) {
		this.manaMax += a;
		this.modifyMana(0);
	}

	@Override
	public void renderBuffs() {
		super.renderBuffs();

		if (this.seePath && Exit.instance != null) {
			float dx = Exit.instance.x + 8 - x - w / 2;
			float dy = Exit.instance.y + 8 - y - h / 2;
			float a = (float) Math.atan2(dy, dx);

			Graphics.batch.end();
			Graphics.shape.setProjectionMatrix(Camera.game.combined);
			Graphics.shape.begin(ShapeRenderer.ShapeType.Filled);
			Graphics.shape.setColor(1, 1, 1, 1);

			float an = (float) Math.toRadians(20);

			Graphics.shape.rectLine((float) (x + w / 2 + Math.cos(a - an) * 18), (float) (y + h / 2 + Math.sin(a - an) * 18), (float) (x + w / 2 + Math.cos(a) * 22), (float) (y + h / 2 + Math.sin(a) * 22), 2);
			Graphics.shape.rectLine((float) (x + w / 2 + Math.cos(a + an) * 18), (float) (y + h / 2 + Math.sin(a + an) * 18), (float) (x + w / 2 + Math.cos(a) * 22), (float) (y + h / 2 + Math.sin(a) * 22), 2);

			Graphics.shape.end();
			Graphics.batch.begin();
		}

		int count = 0;
		Mob last = null;

		for (Mob mob : Mob.all) {
			if (mob.room == this.room) {
				if (count == 1) {
					return;
				}

				last = mob;
				count ++;
			}
		}

		if (last != null) {
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

			Graphics.startShape();
			Graphics.shape.setProjectionMatrix(Camera.game.combined);
			Graphics.shape.setColor(1, 0.2f, 0.2f, 1);

			a = (float) Math.atan2(y - last.y - last.w / 2, x - last.x - last.h / 2);
			float m = 10;
			float am = 0.5f;

			Graphics.shape.rectLine(x, y, x + (float) Math.cos(a - am) * m, y + (float) Math.sin(a - am) * m, 2);
			Graphics.shape.rectLine(x, y, x + (float) Math.cos(a + am) * m, y + (float) Math.sin(a + am) * m, 2);

			Graphics.endShape();
		}
	}

	public boolean isRolling() {
		return this.rolling;
	}

	@Override
	public void render() {
		Graphics.batch.setColor(1, 1, 1, this.a);

		if (this.rolling) {
			this.animation = roll;
		} else if (this.invt > 0) {
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

		if (!this.rolling && this.ui != null && before) {
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
			this.y + this.z, false, false, region.getRegionWidth() / 2,
			0, 0, this.sx * (this.flipped ? -1 : 1), this.sy);

		if (this.hat != null) {
			Graphics.render(this.hat, this.x + 3 + hat.getRegionWidth() / 2, this.y + this.z + offsets[id] + region.getRegionHeight() / 2 - 2,
				0, region.getRegionWidth() / 2, 0, false, false, this.sx * (this.flipped ? -1 : 1), this.sy);
		} else {
			AnimationData anim = headIdle;

			if (this.rolling) {
				anim = headRoll;
			} else if (this.invt > 0) {
				anim = headHurt;
			} else if (this.state.equals("run")) {
				anim = headRun;
			}

			anim.setFrame(this.animation.getFrame());
			region = anim.getCurrent().frame;

			anim.render(this.x - region.getRegionWidth() / 2 + 8,
				this.y + this.z, false, false, region.getRegionWidth() / 2,
				0, 0, this.sx * (this.flipped ? -1 : 1), this.sy);
		}

		if (shade || this.fa > 0) {
			Graphics.batch.end();
			Graphics.batch.setShader(null);
			Graphics.batch.begin();
		}

		if (!this.rolling && this.ui != null && !before) {
			this.ui.renderOnPlayer(this, of);
		}

		Graphics.batch.setColor(1, 1, 1, 1);
	}

	private static int offsets[] = new int[] {
		0, 0, 0, -1, -1, -1, 0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 0,
		0, 0, 0 // for roll
	};

	@Override
	public void setHpMax(int hpMax) {
		super.setHpMax(hpMax);

		if (this.hpMax >= 16) {
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

			if (item.getItem() instanceof Coin) {
				this.playSfx("coin");

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
						});

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
			} else if (!item.getItem().shop && (item.getItem().hasAutoPickup() || item.getAuto())) {
				if (this.tryToPickup(item) && !item.getAuto()) {
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
			} else if (!item.getFalling()) {
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
			if (this.frostLevel > 0) {
				((Mob) entity).addBuff(new FreezeBuff());
			}

			if (this.burnLevel > 0) {
				((Mob) entity).addBuff(new BurningBuff());
			}

			if (Random.chance(this.thornDamageChance)) {
				((Mob) entity).modifyHp(-this.inventory.findItem(ThornRing.class).getLevel() * 2, this);
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
		Graphics.shadow(this.x + this.hx, this.y, this.hw, this.hh, this.z);
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

		switch (this.type) {
			case WARRIOR: case WIZARD: this.accuracy -= 10; break;
		}
	}

	public boolean leaveSmall;

	private void doTp(boolean fromInit) {
		if (this.teleport) {
			this.tp((float) Math.floor(this.lastGround.x) / 16 * 16, (float) Math.floor(this.lastGround.y) / 16 * 16 - 8);
			return;
		}

		if (Dungeon.depth == -1) {
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
		vec.y = Display.GAME_HEIGHT - vec.y;

		Dungeon.darkX = vec.x;
		Dungeon.darkY = vec.y;
	}

	@Override
	public void tp(float x, float y) {
		super.tp(x, y);
		Camera.follow(this, !this.teleport);
		orbitalRing.x = this.x + this.w / 2;
		orbitalRing.y = this.y + this.h / 2;
	}

	public Vector2 orbitalRing = new Vector2();

	private float last;
	private float lastBlood;
	private boolean teleport;

	@Override
	public boolean isUnhittable() {
		return super.isUnhittable() || this.rolling;
	}

	private float zvel;
	private boolean onGround;
	private Vector2 lastGround = new Vector2();

	@Override
	public void update(float dt) {
		super.update(dt);

		if (!this.rolling) {
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
		this.zvel = Math.max(-120, this.zvel - dt * 40);

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
			this.lastMana += dt * (this.velocity.len2() > 9.9f ?
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

				float mx = Input.instance.getAxis("moveX");
				float my = Input.instance.getAxis("moveY");

				if (mx != 0 || my != 0) {
					this.acceleration.x += mx * this.speed;
					this.acceleration.y -= my * this.speed; // Inverted!
				}
			}

			if (!this.rolling) {
				if (Input.instance.wasPressed("roll")) {
					this.rolling = true;
					this.mul = 1;
					this.zvel = 20;

					if (this.acceleration.x == 0 && this.acceleration.y == 0) {
						double a = (this.getAngleTo(Input.instance.worldMouse.x, Input.instance.worldMouse.y));

						this.acceleration.x = (float) Math.cos(a) * this.speed * 3;
						this.acceleration.y = (float) Math.sin(a) * this.speed * 3;
					}
				}
			}
		} else if (Dialog.active != null) {
			if (Input.instance.wasPressed("interact")) {
				Dialog.active.skip();
			}
		}

		float v = this.acceleration.len2();
		this.lastRun += dt;

		if (v > 20) {
			this.become("run");

			/*if (this.lastRun >= 0.08f) {
				this.lastRun = 0;
				this.area.add(new RunFx(this.x, this.y - 8));
			}*/
		} else {
			this.become("idle");
		}

		super.common();

		if (this.animation != null && !this.freezed) {
			if (this.animation.update(dt)) {
				if (this.animation == this.roll) {
					this.animation = this.idle;
					this.rolling = false;
					this.mul = 0.7f;
					this.removeBuff(BurningBuff.class);
				}
			}
		}

		float dx = this.x + this.w / 2 - Input.instance.worldMouse.x;
		this.flipped = dx >= 0;

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

	public int frostLevel;
	private boolean rolling;

	public int getManaMax() {
		return this.manaMax;
	}

	public int flight;

	@Override
	public boolean isFlying() {
		return flight > 0 || this.rolling;
	}

	@Override
	protected void onTouch(short t, int x, int y, int info) {
		if (t == Terrain.FLOOR_A || t == Terrain.FLOOR_B || t == Terrain.FLOOR_C || t == Terrain.FLOOR_D) {
			this.onGround = true;
			this.lastGround.x = this.x;
			this.lastGround.y = this.y;
		}

		if (t == Terrain.WATER && !this.isFlying()) {
			if (this.hasBuff(BurningBuff.class)) {
				int num = GlobalSave.getInt("num_fire_out") + 1;
				GlobalSave.put("num_fire_out", num);

				if (num >= 50) {
					Achievements.unlock(Achievements.UNLOCK_WATER_BOLT);
				}

				this.removeBuff(BurningBuff.class);

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

			this.watery = 5f;
		} else {
			if (!this.isFlying() && BitHelper.isBitSet(info, 0) && !this.hasBuff(BurningBuff.class)) {
				this.addBuff(new BurningBuff());
			}

			if (t == Terrain.LAVA && !this.isFlying() && this.lavaResist == 0) {
				this.modifyHp(-1, null, true);

				if (this.isDead()) {
					Achievements.unlock(Achievements.UNLOCK_WINGS);
				}
			} else if (t == Terrain.COBWEB && this.cutCobweb) {
				Dungeon.level.liquidData[Level.toIndex(x, y)] = 0;
				Dungeon.level.updateTile(x, y);
			} else if (!this.isFlying() && (t == Terrain.HIGH_GRASS || t == Terrain.HIGH_DRY_GRASS)) {
				Dungeon.level.set(x, y, t == Terrain.HIGH_GRASS ? Terrain.GRASS : Terrain.DRY_GRASS);

				if (Random.chance(10)) {
					ItemHolder holder = new ItemHolder(new GrassSeed());

					holder.x = x * 16 + (16 - holder.w) / 2;
					holder.y = y * 16 + (16 - holder.h) / 2;

					Dungeon.area.add(holder.add());
				}

				for (int i = 0; i < 10; i++) {
					GrassBreakFx fx = new GrassBreakFx();

					fx.x = x * 16 + Random.newFloat(16);
					fx.y = y * 16 + Random.newFloat(16) - 8;

					Dungeon.area.add(fx);
				}
			} else if (!this.isFlying() && t == Terrain.VENOM) {
				this.addBuff(new PoisonBuff());
			}
		}
	}

	private boolean hadEnemies;
	public int numCollectedHearts;
	public int burnLevel;

	@Override
	protected void onRoomChange() {
		super.onRoomChange();

		if (numCollectedHearts >= 6) {
			Achievements.unlock(Achievements.UNLOCK_MEATBOY);
		}

		if (hadEnemies && !gotHit) {
			Achievements.unlock(Achievements.UNLOCK_HALO);
		}

		this.resetHit();

		if (this.room != null) {
			if (this.room instanceof ShopRoom) {
				Audio.play("Shopkeeper");

				if (BurningKnight.instance != null) {
					BurningKnight.instance.become("await");
				}
			}

			hadEnemies = false;

			for (int i = Mob.all.size() - 1; i >= 0; i--) {
				Mob mob = Mob.all.get(i);

				if (mob.getRoom() == this.room) {
					hadEnemies = true;
					mob.target = this;
					mob.become("alerted");
				}
			}

			for (Trader trader : Trader.all) {
				if (trader.room == this.room) {
					trader.become("hi");
				}
			}
		}

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

		if (this.healOnEnter && room.numEnemies > 0 && Random.chance(50)) {
			this.modifyHp(this.inventory.findItem(DewVial.class).getLevel(), null);
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

	public int leaveVenom;

	@Override
	public float rollDamage() {
		float v;

		if (luckDamage) {
			if (Random.chance(((LuckyCube) this.inventory.findItem(LuckyCube.class)).getChance())) {
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

		return v * getDamageModifier();
	}

	public float getDamageModifier() {
		return  ((pauseMore && this.velocity.len() < 1f) ? 1.5f : 1) * damageModifier * this.getStat("damage") * (this.touches[Terrain.WATER] ? 0.5f : 1f);
	}

	public boolean cutCobweb;

	@Override
	public float rollDefense() {
		float v;

		if (luckDefense) {
			if (Random.chance(((FortuneArmor) this.inventory.findItem(FortuneArmor.class)).getChance())) {
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
		ManaShield shield = (ManaShield) this.ui.getEquipped(ManaShield.class);

		if (shield != null && this.mana >= shield.getCost() && Random.chance(shield.getChance()) && this.mana >= 2) {
			this.modifyMana((int) -shield.getCost());
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

			for (int i = 0; i < 10; i++) {
				PoofFx fx = new PoofFx();

				fx.x = this.x + this.w / 2;
				fx.y = this.y + this.h / 2;

				Dungeon.area.add(fx);
			}

			for (Mob mob : Mob.all) {
				if (mob.room == this.room) {
					mob.addBuff(new FreezeBuff().setDuration(10));
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
	protected void onHurt(int a, Creature from) {
		super.onHurt(a, from);

		this.gotHit = true;

		Camera.shake(4f);
		Audio.playSfx("voice_gobbo_" + Random.newInt(1, 4), 1f, Random.newFloat(0.9f, 1.9f));

		if (from != null && Random.chance(this.reflectDamageChance)) {
			from.modifyHp(4, this, true);
		}

		BlackHeart heart = (BlackHeart) this.ui.getEquipped(BlackHeart.class);

		if (heart != null && this.room != null) {
			for (int i = Mob.all.size() - 1; i >= 0; i--) {
				Mob mob = Mob.all.get(i);

				if (mob.getRoom() == this.room) {
					mob.modifyHp((int) heart.getDamage(), this, true);
				}
			}
		}

		ClockHeart clock = (ClockHeart) this.ui.getEquipped(ClockHeart.class);

		if (clock != null) {
			Dungeon.slowDown(0.5f - (clock.getLevel() - 1f) * 0.05f, 1f * clock.getLevel());
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
		if ((this.rolling || fireResist > 0) && buff instanceof BurningBuff) {
			return false;
		} else if (poisonResist > 0 && buff instanceof PoisonBuff) {
			return false;
		} else if ((this.rolling || stunResist > 0) && buff instanceof FreezeBuff) {
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
		this.mana = (int) MathUtils.clamp(0, this.manaMax, (float) (this.mana + Math.ceil(a * manaModifier)));
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

		public int id;

		Type(int id) {
			this.id = (int) id;
		}
	}
}