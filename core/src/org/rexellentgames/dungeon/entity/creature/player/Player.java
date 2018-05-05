package org.rexellentgames.dungeon.entity.creature.player;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.UiLog;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Camera;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.buff.HungryBuff;
import org.rexellentgames.dungeon.entity.creature.buff.StarvingBuff;
import org.rexellentgames.dungeon.entity.creature.fx.TextFx;
import org.rexellentgames.dungeon.entity.creature.inventory.Inventory;
import org.rexellentgames.dungeon.entity.creature.inventory.UiInventory;
import org.rexellentgames.dungeon.entity.creature.player.fx.ItemPickedFx;
import org.rexellentgames.dungeon.entity.creature.player.fx.ItemPickupFx;
import org.rexellentgames.dungeon.entity.creature.player.fx.RunFx;
import org.rexellentgames.dungeon.entity.item.Gold;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.item.weapon.dagger.Dagger;
import org.rexellentgames.dungeon.entity.item.weapon.dagger.DaggerA;
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.Part;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.entity.level.entities.Entrance;
import org.rexellentgames.dungeon.entity.level.rooms.Room;
import org.rexellentgames.dungeon.entity.level.rooms.regular.RegularRoom;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.game.state.ComicsState;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.util.*;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;
import org.rexellentgames.dungeon.util.geometry.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Player extends Creature {
	public static ArrayList<Player> all = new ArrayList<Player>();
	public static int INVENTORY_SIZE = 12;

	private static final float LIGHT_SIZE = 2f;
	public static String NAME;
	public static Player instance;
	public static boolean REGISTERED = false;
	private static HashMap<String, Animation> skins = new HashMap<>();
	public float lightModifier;
	public int connectionId;
	public boolean main;
	public float heat;
	protected float mana;
	protected float manaMax;
	protected int experience;
	protected int experienceMax;
	protected int level;
	protected int forThisLevel;
	private ItemPickupFx pickupFx;
	private Inventory inventory;
	private UiInventory ui;
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

	{
		hpMax = 100;
		manaMax = 100;
		level = 1;
		hunger = 10;
		alwaysActive = true;
		invmax = 1f;
		// unhittable = true; // todo: remove

		setSkin("");
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

	public Player() {
		this("player");
	}

	public Player(String name) {
		this.name = name;

		all.add(this);

		if (!name.equals("ghost")) {
			if (instance == null || Network.NONE) {
				instance = this;
				main = true;
				local = true;
			}
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
			Graphics.playSfx("step_gobbo_water_" + Random.newInt(1, 6), 1f, Random.newFloat(0.9f, 1.9f));
		} else {
			Graphics.playSfx("step_gobbo_" + Random.newInt(1, 6), 1f, Random.newFloat(0.9f, 1.9f));
		}
	}

	public float getLightSize() {
		return LIGHT_SIZE + this.lightModifier;
	}

	public static int expNeeded(int level) {
		if (level == 1) {
			return 0;
		}

		level -= 1;

		return level * 2;
	}

	@Override
	public String getParam() {
		return this.name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void generate() {
		if (Dungeon.type != Dungeon.Type.INTRO) {
			this.inventory.add(new ItemHolder().setItem(new DaggerA()));
		}
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

	@Override
	public void destroy() {
		super.destroy();
		Player.all.remove(this);
	}

	@Override
	public void init() {
		super.init();

		if (instance == null) {
			instance = this;
		}

		this.experienceMax = expNeeded(this.level);
		this.forThisLevel = expNeeded(this.level);
		this.mana = this.manaMax;
		this.inventory = new Inventory(this, INVENTORY_SIZE);
		this.body = this.createSimpleBody(3, 1, 10, 10, BodyDef.BodyType.DynamicBody, false);
	}

	public void modifyMana(float a) {
		this.mana = MathUtils.clamp(0, this.manaMax, this.mana + a);
	}

	public void tryToFall() {
		if (Dungeon.loadType == Entrance.LoadType.FALL_DOWN) {
			while (true) {
				Room room = Dungeon.level.getRandomRoom(RegularRoom.class);
				Point cell = room.getRandomCell();

				if (Dungeon.level.checkFor((int) cell.x, (int) cell.y, Terrain.PASSABLE)) {
					this.tp(cell.x * 16, cell.y * 16);
					this.fallHurt = true;

					Camera.instance.follow(this);

					break;
				}
			}
		}
	}

	private boolean fallHurt;

	@Override
	protected void onHurt() {
		super.onHurt();

		Camera.instance.shake(4f);
		Graphics.playSfx("voice_gobbo_" + Random.newInt(1, 4), 1f, Random.newFloat(0.9f, 1.9f));

		if (this.hp == 0) {
			Dungeon.slowDown(0.5f, 2f);
		}
	}

	private float lastRun;
	private float lastDashT;
	private float dashTimeout;

	@Override
	public void update(float dt) {
		super.update(dt);

		this.dashT = Math.max(0, this.dashT - dt);
		this.dashTimeout = Math.max(0, this.dashTimeout - dt);

		if (Dungeon.game.getState() instanceof ComicsState) {
			float x = Math.max(Camera.instance.getCamera().position.x - 80, this.x);
			float y = MathUtils.clamp(Camera.instance.getCamera().position.y - 133,
				Camera.instance.getCamera().position.y, this.y);

			this.body.setTransform(x, y, 0);

			if (x > Camera.instance.getCamera().position.x + 170) {
				Dungeon.goToLevel(0);
				Dungeon.loadType = Entrance.LoadType.RUNNING;
				return;
			}
		}

		if (Dungeon.level != null) {
			Dungeon.level.addLightInRadius(this.x + 8, this.y + 8, 0, 0, 0, 2f, this.getLightSize(), false);
			Room room = Dungeon.level.findRoomFor(this.x, this.y);

			if (room != null) {
				this.currentRoom = room;
			}

			if (this.fallHurt) {
				this.fallHurt = false;
				this.falling = false;
				boolean h = this.unhittable;
				this.unhittable = false;
				this.modifyHp(-60, true);
				this.unhittable = h;
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

		if (Network.SERVER) {
			Input.set(this.getId());

			if (this.y >= 60 - 16) {
				Dungeon.goToLevel(0);
			}
		}

		if ((Network.SERVER || this.main) && (!(Dungeon.game.getState() instanceof ComicsState) || ComicsState.alpha[4] < 0.5f)
			&& Dialog.active == null){

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

				this.vel.x += mx * this.speed;
				this.vel.y -= my * this.speed; // Inverted!

				if (Input.instance.wasPressed("dash") && this.dashTimeout == 0 && this.dashT == 0f) {
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
				}
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

			if (this.lastRun >= 0.2f && !Network.SERVER) {
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

		if (this.animation != null) {
			this.animation.update(dt);
		}

		float dx = this.x + this.w / 2 - Input.instance.worldMouse.x - 8;
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
		super.onTouch(t, x, y);

		if (t == Terrain.WATER) {
			this.watery = 5f;
		}
	}

	private ArrayList<Point> last = new ArrayList<>();

	@Override
	public void render() {
		boolean h = (this.hp < this.hpMax / 4);

		Graphics.batch.setColor(1, h ? 0.2f : 1, h ? 0.2f : 1, this.a);

		if (this.falling) {
			this.renderFalling(this.animation);
			return;
		}

		if (this.dead) {
			this.animation = killed;
		} else if (this.invt > 0) {
			this.animation = hurt;
		} else if (this.state.equals("run")) {
			this.animation = run;
		} else {
			this.animation = idle;
		}

		if (this.ui != null) {
			this.ui.renderBeforePlayer(this);
		}

		for (int i = 0; i < this.last.size(); i++) {
			Point last = this.last.get(i);

			Graphics.batch.setColor(1, 1, 1, this.a / (this.last.size() - i + 1));
			Graphics.startShadows();
			this.animation.render(last.x, last.y, false, false, 8, 8, 0, this.sx * (this.flipped ? -1 : 1), this.sy);
			Graphics.endShadows();
			this.animation.render(last.x, last.y, false, false, 8, 8, 0, this.sx * (this.flipped ? -1 : 1), this.sy);
		}

		if (this.dashT > 0) {
			this.last.add(new Point(this.x, this.y));
		}

		if (this.last.size() > 5 || (this.last.size() > 0 && this.dashT <= 0)) {
			this.last.remove(0);
		}

		this.animation.render(this.x, this.y, false, false, 8, 8, 0, this.sx * (this.flipped ? -1 : 1), this.sy);
		Graphics.batch.setColor(1, 1, 1, this.a);

		if (this.ui != null) {
			this.ui.renderOnPlayer(this);
		}

		Graphics.batch.setColor(1, 1, 1, 1);
		this.renderBuffs();

		if (this.name != null && !Network.NONE) {
			Graphics.small.draw(Graphics.batch, this.name, this.x, this.y - 4);
		}
	}

	private float sx = 1f;
	private float sy = 1f;

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.inventory.load(reader);

		this.mana = reader.readInt32();
		this.manaMax = reader.readFloat();
		this.experience = reader.readInt32();
		this.experienceMax = reader.readInt32();
		this.level = reader.readInt32();
		this.forThisLevel = expNeeded(this.level);

		this.setHunger(reader.readInt16());
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		this.inventory.save(writer);

		writer.writeFloat(this.mana);
		writer.writeFloat(this.manaMax);
		writer.writeInt32(this.experience);
		writer.writeInt32(this.experienceMax);
		writer.writeInt32(this.level);

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
					Dungeon.level.removeSaveable(item);
				}
			} else if (!Network.SERVER && !item.falling) {
				this.holders.add(item);

				if (this.pickupFx == null) {
					this.pickupFx = new ItemPickupFx(item, this);
					this.area.add(this.pickupFx);
				}
			}
		}
	}

	@Override
	public void onCollisionEnd(Entity entity) {
		if (entity instanceof ItemHolder) {
			if (this.pickupFx != null) {
				this.pickupFx.done = true;
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

	public int getExperienceForLevel() {
		return this.experience;
	}

	public int getExperienceMaxForLevel() {
		return this.experienceMax;
	}

	public int getForThisLevel() {
		return this.forThisLevel;
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

	@Override
	protected void die() {
		super.die();

		if (UiLog.instance != null) {
			UiLog.instance.print("[red]You died!");
		}
	}

	public void addExperience(int am) {
		this.experience += am;

		while (this.experience >= this.experienceMax) {
			this.level += 1;
			this.experience -= this.experienceMax;
			this.forThisLevel = expNeeded(this.level);
			this.experienceMax = expNeeded(this.level + 1);
			this.hpMax += 25;

			UiLog.instance.print("[green]You reached level " + this.level + "!");
		}
	}
}