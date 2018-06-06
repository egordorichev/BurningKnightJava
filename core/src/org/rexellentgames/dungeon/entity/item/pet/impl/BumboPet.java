package org.rexellentgames.dungeon.entity.item.pet.impl;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.item.Gold;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.level.entities.Door;
import org.rexellentgames.dungeon.physics.World;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;

public class BumboPet extends SimpleFollowPet {
	private byte progress;
	private Body body;

	{
		noAdd = true;
		dependOnDistance = true;
		maxDistance = 32f;
		buildPath = true;
	}

	@Override
	public void init() {
		super.init();

		this.w = this.region.getRegionWidth() - 2;
		this.h = this.region.getRegionHeight() - 2;

		this.body = World.createCircleBody(this, 0, 0, this.w / 2f,
			BodyDef.BodyType.DynamicBody, false);
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		progress = reader.readByte();
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeByte(progress);
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		this.x = this.body.getPosition().x;
		this.y = this.body.getPosition().y;
		this.body.setLinearVelocity(this.vel);

		if (this.target == this.owner) {
			float max = 64f;

			for (int i = Gold.all.size() - 1; i >= 0; i--) {
				ItemHolder gold = Gold.all.get(i);

				if (gold.done) {
					Gold.all.remove(i);
					continue;
				}

				float d = gold.getDistanceTo(this.x + this.w / 2, this.y + this.h / 2);

				if (d < max) {
					max = d;
					maxDistance = 8f;
					this.target = gold;
				}
			}
		}
	}

	@Override
	protected void tp() {
		super.tp();
		this.body.setTransform(this.x, this.y, 0);
	}

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity instanceof ItemHolder && (((ItemHolder) entity).getItem() instanceof Gold || entity == this.target)) {
			this.progress = (byte) Math.max(100, this.progress + ((ItemHolder) entity).getItem().getCount());
			this.target = this.owner;
			entity.done = true;
			maxDistance = 32f;
			Graphics.playSfx("coin");
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}
}