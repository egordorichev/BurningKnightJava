package org.rexellentgames.dungeon.entity.creature.player;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.UiLog;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.ui.ExpFx;
import org.rexellentgames.dungeon.entity.creature.inventory.Inventory;
import org.rexellentgames.dungeon.entity.creature.inventory.UiInventory;
import org.rexellentgames.dungeon.entity.creature.player.fx.ItemPickedFx;
import org.rexellentgames.dungeon.entity.creature.player.fx.ItemPickupFx;
import org.rexellentgames.dungeon.entity.creature.player.fx.RunFx;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;

public class Player extends Creature {
	private static final int LIGHT_SIZE = 32;
	public static Player instance;
	private static Animation idle = new Animation(Graphics.sprites, 0.08f, 16, 0,  1, 2, 3, 4, 5, 6, 7);
	private static Animation run = new Animation(Graphics.sprites, 0.08f, 16, 8, 9, 10, 11, 12, 13, 14, 15);
	private static Animation hurt = new Animation(Graphics.sprites, 0.1f, 16, 16, 17);
	private static Animation killed = new Animation(Graphics.sprites, 1f, 16, 18);
	private PointLight light;
	private ItemPickupFx pickupFx;
	private Inventory inventory;
	private UiInventory ui;
	protected int mana;
	protected int manaMax;
	protected int experience;
	protected int experienceMax;
	protected int level;
	protected int forThisLevel;
	public float lightModifier;

	{
		hpMax = 100;
		manaMax = 100;
		level = 1;
		speed = 10;
		alwaysActive = true;
	}

	@Override
	public PointLight getLight() {
		return this.light;
	}

	public void setUi(UiInventory ui) {
		this.ui = ui;
	}

	@Override
	public void init() {
		super.init();
		instance = this;

		this.experienceMax = expNeeded(this.level);
		this.forThisLevel = expNeeded(this.level);
		this.mana = this.manaMax;
		this.inventory = new Inventory(this, 24);
		this.body = this.createBody(3, 1, 10, 10, BodyDef.BodyType.DynamicBody, false);

		if (Dungeon.light != null) {
			this.light = new PointLight(Dungeon.light, 128, new Color(1, 0.9f, 0.8f, 1f),
				LIGHT_SIZE, 300, 300);
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.vel.mul(0.8f);

		if (this.dead) {
			super.common();
			return;
		}

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

		if (this.light != null) {
			this.light.setPosition(this.x + 8, this.y + 8);
			this.light.setDistance((float) (LIGHT_SIZE + Math.cos(this.t * 3) * 8 + this.lightModifier));
		}

		float v = this.vel.len2();

		if (v > 9.9) {
			this.become("run");

			if (this.t % 0.2 <= 0.017) {
				this.area.add(new RunFx(this.x, this.y - 8));
			}
		} else {
			this.become("idle");

			this.vel.x = 0;
			this.vel.y = 0;
		}

		super.common();
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
	}

	@Override
	public void destroy() {
		this.light.remove(true);
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
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof ItemHolder) {
			ItemHolder item = (ItemHolder) entity;

			if (item.getItem().hasAutoPickup()) {
				this.tryToPickup(item);
			} else if (this.pickupFx == null) {
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
		UiLog.instance.print("[red]You died!");
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


	public static int expNeeded(int level) {
		if (level == 1) {
			return 0;
		}

		level -= 1;

		return level * 2;
	}
}