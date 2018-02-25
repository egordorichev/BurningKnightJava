package org.rexellentgames.dungeon.entity.item;

import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;

public class Item {
	protected short sprite = 0;
	protected String name = "Missing Item Name";
	protected boolean stackable = false;
	protected int count = 1;
	protected boolean autoPickup = false;
	protected boolean useable = true;
	protected float delay = 0;
	protected float useTime = 0.5f;

	public void render(float x, float y, boolean flipped) {
		Graphics.render(Graphics.items, this.sprite, x, y);
	}

	public void update(float dt) {
		this.delay = Math.max(0, this.delay - dt);
	}

	public void use() {
		this.delay = this.useTime;
	}

	public void secondUse() {
		this.delay = this.useTime;
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
}