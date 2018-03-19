package org.rexellentgames.dungeon.entity.creature.player;

import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.UiLog;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.creature.fx.FireRectFx;
import org.rexellentgames.dungeon.entity.creature.inventory.Inventory;
import org.rexellentgames.dungeon.entity.creature.inventory.UiInventory;
import org.rexellentgames.dungeon.entity.creature.player.fx.FootFx;
import org.rexellentgames.dungeon.entity.creature.player.fx.ItemPickedFx;
import org.rexellentgames.dungeon.entity.creature.player.fx.ItemPickupFx;
import org.rexellentgames.dungeon.entity.creature.player.fx.RunFx;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.item.consumable.potion.HealingPotion;
import org.rexellentgames.dungeon.entity.item.consumable.potion.SpeedPotion;
import org.rexellentgames.dungeon.entity.item.consumable.potion.SunPotion;
import org.rexellentgames.dungeon.entity.item.consumable.seed.CabbageSeed;
import org.rexellentgames.dungeon.entity.item.consumable.spell.SpellOfDamage;
import org.rexellentgames.dungeon.entity.item.tool.Hoe;
import org.rexellentgames.dungeon.entity.item.weapon.Dagger;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.net.Network;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.MathUtils;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;
import java.util.ArrayList;

public class Player extends Creature {
	public static ArrayList<Player> all = new ArrayList<Player>();

	private static final float LIGHT_SIZE = 3f;
	public static String NAME;
	public static Player instance;
	public static boolean REGISTERED = false;
	private static Animation idle = Animation.make(Graphics.sprites, 0.08f, 16, 0, 1, 2, 3, 4, 5, 6, 7);
	private static Animation run = Animation.make(Graphics.sprites, 0.08f, 16, 8, 9, 10, 11, 12, 13, 14, 15);
	private static Animation hurt = Animation.make(Graphics.sprites, 0.1f, 16, 16, 17);
	private static Animation killed = Animation.make(Graphics.sprites, 1f, 16, 18);
	public float lightModifier;
	public int connectionId;
	public boolean main;
	protected int mana;
	protected int manaMax;
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

	{
		hpMax = 100;
		manaMax = 100;
		level = 1;
		hunger = 10;
		alwaysActive = true;
	}

	public Player() {
		this("player");
	}

	public Player(String name) {
		this.name = name;

		all.add(this);

		if (instance == null || Network.NONE) {
			instance = this;
			main = true;
			local = true;
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
		// this.inventory.add(new ItemHolder().setItem(new Hoe()));
		this.inventory.add(new ItemHolder().setItem(new CabbageSeed().setCount(100)));
		this.inventory.add(new ItemHolder().setItem(new SunPotion().setCount(100)));
		this.inventory.add(new ItemHolder().setItem(new HealingPotion().setCount(100)));
		this.inventory.add(new ItemHolder().setItem(new SpeedPotion().setCount(100)));
		this.inventory.add(new ItemHolder().setItem(new SpellOfDamage().setCount(100)));
	}

	public float getHunger() {
		return this.hunger;
	}

	public void setHunger(float hunger) {
		float old = this.hunger;
		this.hunger = MathUtils.clamp(0, 360, hunger);

		// todo
		/*
		if (old < 360 && this.hunger == 360) {
			this.addBuff(new StarvingBuff());
		} else if ((old < 260 || old == 360) && this.hunger >= 260) {
			this.addBuff(new HungryBuff());
		}

		if (this.hunger != 360) {
			this.removeBuff(StarvingBuff.class);
		}

		if (this.hunger < 260 || this.hunger == 360) {
			this.removeBuff(HungryBuff.class);
		}*/
	}

	public void setUi(UiInventory ui) {
		this.ui = ui;
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
		this.inventory = new Inventory(this, 24);
		this.body = this.createBody(3, 1, 10, 10, BodyDef.BodyType.DynamicBody, false);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (Dungeon.level != null) {
			Dungeon.level.addLightInRadius(this.x + 8, this.y + 8, 0, 0, 0, 0.5f, this.getLightSize(), false);
		}

		this.watery = Math.max(0, this.watery - dt);

		if (this.dead) {
			super.common();
			return;
		}

		this.setHunger(this.hunger + dt);

		if (Network.SERVER) {
			Input.set(this.getId());

			if (this.y >= 60 - 16) {
				Dungeon.goToLevel(1);
			}
		}

		if (Network.SERVER || this.main) {
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

		float v = this.vel.len2();

		if (v > 9.9) {
			this.become("run");

			if (this.t % 0.2 <= 0.017 && !Network.SERVER) {
				this.area.add(new RunFx(this.x, this.y - 8));
			}

			if (this.t % 0.3 <= 0.017 && !Network.SERVER && this.watery > 0) {
				this.area.add(new FootFx(this.x + 8, this.y - 8, (float) Math.atan2(this.vel.y, this.vel.x), this.watery / 5f));
			}
		} else {
			this.become("idle");

			this.vel.x = 0;
			this.vel.y = 0;
		}

		super.common();

		float dx = this.x + this.w / 2 - Input.instance.worldMouse.x - 8;

		this.flipped = dx >= 0;
	}

	@Override
	protected void onTouch(short t, int x, int y) {
		super.onTouch(t, x, y);

		if (Dungeon.level.isWater(x, y, false)) {
			this.watery = 5f;
		}
	}

	@Override
	public void render() {
		Graphics.batch.setColor(1, 1, 1, this.a);

		Animation animation;

		if (this.dead) {
			animation = killed;
		} else if (this.invt > 0) {
			animation = hurt;
		} else if (this.state.equals("run")) {
			animation = run;
		} else {
			animation = idle;
		}

		animation.render(this.x, this.y, this.t, this.flipped);
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
		this.manaMax = reader.readInt32();
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

		writer.writeInt32(this.mana);
		writer.writeInt32(this.manaMax);
		writer.writeInt32(this.experience);
		writer.writeInt32(this.experienceMax);
		writer.writeInt32(this.level);

		writer.writeInt16((short) this.hunger);
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof ItemHolder) {
			ItemHolder item = (ItemHolder) entity;

			if (item.getItem().hasAutoPickup()) {
				this.tryToPickup(item);
			} else if (this.pickupFx == null && !Network.SERVER) {
				this.pickupFx = new ItemPickupFx(item, this);
				this.area.add(this.pickupFx);
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

	public int getExperience() {
		return this.experience;
	}

	public int getExperienceMax() {
		return this.experienceMax;
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

	public int getMana() {
		return this.mana;
	}

	public int getManaMax() {
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

			UiLog.instance.print("[green]You reached level " + this.level + "!");
		}
	}
}