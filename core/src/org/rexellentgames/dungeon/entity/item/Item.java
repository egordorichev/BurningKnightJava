package org.rexellentgames.dungeon.entity.item;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.file.FileReader;

import java.io.IOException;

public class Item extends SaveableEntity {
	protected short sprite = 0;
	protected String name = "Missing Item Name";
	protected boolean stackable = false;
	protected int count = 0;
	protected Body body;
	protected boolean autoPickup = false;

	@Override
	public void init() {
		super.init();

		this.body = this.createBody(0, 0, 16, 16, BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y, 0);
	}

	public short getSprite() {
		return this.sprite;
	}

	@Override
	public void render() {
		this.body.setTransform(this.x, this.y, 0);
		Graphics.render(Graphics.items, this.sprite, this.x, this.y);
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

	public boolean hasAutoPickup() {
		return this.autoPickup;
	}
}