package org.rexellentgames.dungeon.entity.item;

import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;

public class Item {
	protected short sprite = 0;
	protected String name = "Missing Item Name";
	protected boolean stackable = false;
	protected int count = 0;
	protected boolean autoPickup = false;

	public void save(FileWriter writer) throws IOException {

	}

	public void load(FileReader reader) throws IOException {

	}

	public short getSprite() {
		return this.sprite;
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