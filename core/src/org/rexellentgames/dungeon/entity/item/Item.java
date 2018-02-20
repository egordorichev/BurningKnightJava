package org.rexellentgames.dungeon.entity.item;

import org.rexellentgames.dungeon.entity.Entity;

public class Item extends Entity {
	protected short sprite = 0;
	protected String name = "Missing Item Name";
	protected boolean stackable = false;
	protected int count = 0;

	public short getSprite() {
		return this.sprite;
	}

	public String getName() {
		return this.name;
	}

	public boolean isStackable() {
		return this.stackable;
	}

	public int getCount() {
		return this.count;
	}

	public Item randomize() {
		return this;
	}
}