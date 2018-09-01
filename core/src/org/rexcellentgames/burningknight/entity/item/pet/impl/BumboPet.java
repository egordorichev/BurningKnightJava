package org.rexcellentgames.burningknight.entity.item.pet.impl;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.assets.Audio;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.item.Gold;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;

public class BumboPet extends SimpleFollowPet {
	private byte progress;
	private Body body;

	{
		noAdd = true;
		dependOnDistance = true;
		maxDistance = 32f;
		buildPath = true;
		sprite = "item-bumbo";
	}

	@Override
	public void init() {
		super.init();

		this.w = this.region.getRegionWidth();
		this.h = this.region.getRegionHeight();

		this.body = World.createCircleBody(this, 0, 0, this.w / 2f, BodyDef.BodyType.DynamicBody, false);
		
		if (this.body != null) {
			World.checkLocked(this.body).setTransform(this.x, this.y, 0);
		}
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		progress = reader.readByte();
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
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
		this.body.setLinearVelocity(this.velocity);

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
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
		this.target = this.owner;
		this.next = null;
	}

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity instanceof ItemHolder && (((ItemHolder) entity).getItem() instanceof Gold || entity == this.target)) {
			this.progress = (byte) Math.max(100, this.progress + ((ItemHolder) entity).getItem().getCount());
			this.target = this.owner;
			entity.done = true;
			maxDistance = 32f;
			Audio.playSfx("coin");
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}
}