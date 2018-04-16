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
import org.rexellentgames.dungeon.entity.item.Lamp;
import org.rexellentgames.dungeon.entity.item.weapon.Dagger;
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

public class Player extends Creature {
	public static ArrayList<Player> all = new ArrayList<Player>();

	private static final float LIGHT_SIZE = 2f;
	public static String NAME;
	public static Player instance;
	public static boolean REGISTERED = false;
	private static Animation animations = Animation.make("actor-gobbo");
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
	private int gold;
	public Room currentRoom;

	private static Sound[] steps;
	private static Sound[] waterSteps;

	{
		hpMax = 100;
		manaMax = 100;
		level = 1;
		hunger = 10;
		alwaysActive = true;
		// unhittable = true; // todo: remove

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
		this.inventory.add(new ItemHolder().setItem(new Dagger()));
		// this.inventory.add(new ItemHolder().setItem(new Lamp()));

		/*this.inventory.add(new ItemHolder().setItem(new CabbageSeed().setCount(100)));
		this.inventory.add(new ItemHolder().setItem(new SunPotion().setCount(100)));
		this.inventory.add(new ItemHolder().setItem(new HealingPotion().setCount(100)));
		this.inventory.add(new ItemHolder().setItem(new SpeedPotion().setCount(100)));*/
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

		if (steps == null) {
			steps = new Sound[5];
			waterSteps = new Sound[5];

			for (int i = 1; i < 6; i++) {
				steps[i - 1] = Graphics.getSound("sfx/step_gobbo_normal_" + i + ".wav");
				waterSteps[i - 1] = Graphics.getSound("sfx/step_gobbo_water_" + i + ".wav");
			}
		}

		if (instance == null) {
			instance = this;
		}

		this.experienceMax = expNeeded(this.level);
		this.forThisLevel = expNeeded(this.level);
		this.mana = this.manaMax;
		this.inventory = new Inventory(this, 6);
		this.body = this.createBody(3, 1, 10, 10, BodyDef.BodyType.DynamicBody, false);
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
					this.modifyHp(-40);
					// todo: debuffs?
					Camera.instance.follow(this);

					break;
				}
			}
		}
	}

	@Override
	public void modifyHp(int amount) {


		super.modifyHp(amount);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

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
			Dungeon.level.addLightInRadius(this.x + 8, this.y + 8, 0, 0, 0, 0.8f, this.getLightSize(), false);
			Room room = Dungeon.level.findRoomFor(this.x, this.y);

			if (room != null) {
				this.currentRoom = room;
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

		if ((Network.SERVER || this.main) && (!(Dungeon.game.getState() instanceof ComicsState) || ComicsState.alpha[4] < 0.5f)){
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
			}
		}

		float v = this.vel.len2();

		if (v > 20) {
			this.become("run");

			if (this.t % 0.2 <= 0.017 && !Network.SERVER) {
				this.area.add(new RunFx(this.x, this.y - 8));
			}

			if (this.t % 0.3 <= 0.017 && !Network.SERVER) {
				if (this.watery > 0) {
					// this.area.add(new FootFx(this.x + 8, this.y - 8, (float) Math.atan2(this.vel.y, this.vel.x), this.watery / 5f));

					if (this.watery > 4.5f) {
						Sound sound = waterSteps[Random.newInt(5)];
						sound.setPitch(sound.play(), Random.newFloat(0.9f, 1.9f));
					} else {
						Sound sound = steps[Random.newInt(5)];
						sound.setPitch(sound.play(), Random.newFloat(0.9f, 1.9f));
					}
				} else {
					Sound sound = steps[Random.newInt(5)];
					sound.setPitch(sound.play(), Random.newFloat(0.9f, 1.9f));
				}
			}
		} else {
			this.become("idle");

			this.vel.x = 0;
			this.vel.y = 0;
		}

		super.common();

		if (this.animation != null) {
			this.animation.update(dt);
		}

		float dx = this.x + this.w / 2 - Input.instance.worldMouse.x - 8;
		this.flipped = dx >= 0;
	}

	@Override
	protected void onTouch(short t, int x, int y) {
		super.onTouch(t, x, y);

		if (t == Terrain.WATER) {
			this.watery = 5f;
		}
	}

	@Override
	public void render() {
		Graphics.batch.setColor(1, 1, 1, this.a);

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

		this.animation.render(this.x, this.y, this.flipped);
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
		this.gold = reader.readInt32();

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
		writer.writeInt32(this.gold);

		writer.writeInt16((short) this.hunger);
	}

	private ArrayList<ItemHolder> holders = new ArrayList<>();

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof ItemHolder) {
			ItemHolder item = (ItemHolder) entity;

			if (item.getItem().hasAutoPickup()) {
				this.tryToPickup(item);
				this.area.add(new ItemPickedFx(item));
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
			if (item.getItem() instanceof Gold) {
				item.done = true;
				this.gold += item.getItem().getCount();
				return true;
			} else {
				if (this.inventory.add(item)) {
					if (item.getItem().hasAutoPickup()) {
						this.area.add(new ItemPickedFx(item));
					}

					return true;
				}
			}
		}

		return false;
	}

	public int getGold() {
		return this.gold;
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