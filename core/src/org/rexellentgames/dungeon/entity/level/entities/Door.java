package org.rexellentgames.dungeon.entity.level.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;

public class Door extends SaveableEntity {
	private boolean open = false;
	private boolean vertical;
	private Body body;

	public Door(int x, int y, boolean vertical) {
		this.x = x * 16;
		this.y = y * 16;
		this.vertical = vertical;
	}

	public Door() {}

	@Override
	public void init() {
		if (this.body == null) {
		 	this.body = this.createBody((int) this.x, (int) this.y, 16, this.vertical ? 24 : 16, BodyDef.BodyType.DynamicBody, true);
		}
	}

	@Override
	public void render() {
		Graphics.render(Graphics.tiles, this.vertical ? (this.open ? 5 : 37) : (this.open ? 37 : 5),
			this.x, this.y, 1, this.vertical ? 2 : 1);

		// Graphics.render(Graphics.tiles, 37, this.x, this.y, 1, 2);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.vertical = reader.readBoolean();

		this.body = this.createBody(0, 0, 16, this.vertical ? 24 : 16, BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeBoolean(this.vertical);
	}

	public void setOpen(boolean open) {
		this.open = open;
	}
}