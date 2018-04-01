package org.rexellentgames.dungeon.entity.level.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.Creature;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.util.Animation;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;

public class Door extends SaveableEntity {
	private boolean vertical;
	private Body body;
	private int numCollisions;
	private static Animation vertAnimation = Animation.make("actor-door-vertical");
	private static Animation horizAnimation = Animation.make("actor-door-horizontal");
	private AnimationData animation;

	public Door(int x, int y, boolean vertical) {
		this.x = x * 16;
		this.y = y * 16;
		this.vertical = vertical;

		if (!this.vertical) {
			this.animation = vertAnimation.get("idle");
			this.y -= 8;
		} else {
			this.animation = horizAnimation.get("idle");
			this.x += 4;
		}

		this.animation.setPaused(true);
		this.animation.setAutoPause(true);
	}

	public Door() {

	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.animation.update(dt)) {

		}
	}

	@Override
	public void onCollision(Entity entity) {
		if (entity instanceof Creature) {
			this.numCollisions += 1;

			this.animation.setFrame(0);
			this.animation.setBack(false);
			this.animation.setPaused(false);
		}
	}

	@Override
	public void onCollisionEnd(Entity entity) {
		if (entity instanceof Creature) {
			this.numCollisions -= 1;

			if (this.numCollisions <= 0) {
				this.numCollisions = 0; // to make sure

				this.animation.setFrame(2);
				this.animation.setBack(true);
				this.animation.setPaused(false);
			}
		}
	}

	@Override
	public void init() {
		if (this.body == null) {
		 	this.body = this.createBody((int) this.x, (int) this.y, 16, 16, BodyDef.BodyType.DynamicBody, true);
		}
	}

	@Override
	public void render() {
		this.animation.render(this.x, this.y, false);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.vertical = reader.readBoolean();

		this.body = this.createBody(0, 0, 16, 16, BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y, 0);

		if (!this.vertical) {
			this.animation = vertAnimation.get("idle");
		} else {
			this.animation = horizAnimation.get("idle");
		}

		this.animation.setPaused(true);
		this.animation.setAutoPause(true);
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeBoolean(this.vertical);
	}
}