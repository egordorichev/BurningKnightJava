package org.rexellentgames.dungeon.entity.item;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;

public class Item extends Entity {
	protected short sprite = 0;
	protected String name = "Missing Item Name";
	protected String description = "";
	protected boolean stackable = false;
	protected int count = 1;
	protected boolean autoPickup = false;
	protected boolean useable = true;
	protected float delay = 0;
	protected float useTime = 0.5f;
	protected boolean identified;
	protected boolean cursed;
	protected Creature owner;

	public void setOwner(Creature owner) {
		this.owner = owner;
	}

	public void render(float x, float y, boolean flipped) {
		Graphics.render(Graphics.items, this.sprite, x, y);
	}

	@Override
	public void update(float dt) {
		this.delay = Math.max(0, this.delay - dt);
	}

	public void use() {
		this.delay = this.useTime;
	}

	public void secondUse() {
		this.delay = this.useTime;
	}

	public void endUse() {

	}

	public float getDelay() {
		return this.delay;
	}

	public void save(FileWriter writer) throws IOException {
		writer.writeInt32(this.count);
	}

	public void load(FileReader reader) throws IOException {
		this.count = reader.readInt32();
	}

	public short getSprite() {
		return this.sprite;
	}

	public boolean isUseable() {
		return this.useable;
	}

	public String getName() {
		return this.name;
	}

	public boolean isStackable() {
		return this.stackable;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return this.count;
	}

	public Item randomize() {
		return this;
	}

	public boolean hasAutoPickup() {
		return this.autoPickup;
	}

	public boolean isCursed() {
		return this.cursed;
	}

	public boolean isIdentified() {
		return this.identified;
	}

	public void identify() {
		this.identified = true;
	}

	public String getDescription() {
		return this.description;
	}

	public float getUseTime() {
		return this.useTime;
	}
}