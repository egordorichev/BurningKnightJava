package org.rexellentgames.dungeon.entity.level.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;

public class Door extends SaveableEntity {
	private boolean open = false;
	private boolean vertical;
	private Body body;
	private int numCollisions;

	public Door(int x, int y, boolean vertical) {
		this.x = x * 16;
		this.y = y * 16;
		this.vertical = vertical;
	}

	public Door() {

	}

	@Override
	public void init() {
		if (this.body == null) {
		 	this.body = this.createBody((int) this.x, (int) this.y, 16, 16, BodyDef.BodyType.DynamicBody, true);
		}
	}

	@Override
	public void render() {
		int w = 1;
		int h = 1;
		int xm = 0;
		boolean open = true;

		if (this.vertical) {
			if (this.open) {
				xm = 4;
			} else {
				open = false;
			}
		} else {
			if (this.open) {
				open = false;
			}
		}

		Graphics.render(open ? Terrain.openDoor : Terrain.closedDoor, this.x + xm, this.y);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.vertical = reader.readBoolean();

		this.body = this.createBody(0, 0, 16, 16, BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeBoolean(this.vertical);
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Creature) {
			this.numCollisions += 1;
			this.open = true;
		}
	}

	@Override
	public void onCollisionEnd(Entity entity) {
		if (entity instanceof Creature) {
			this.numCollisions -= 1;

			if (this.numCollisions <= 0) {
				this.open = false;
				this.numCollisions = 0; // to make sure
			}
		}
	}
}