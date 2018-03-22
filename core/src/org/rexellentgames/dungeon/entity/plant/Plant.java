package org.rexellentgames.dungeon.entity.plant;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.item.weapon.Weapon;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;
import java.util.ArrayList;

public class Plant extends SaveableEntity {
	protected String sprite;
	protected float growSpeed = 1f;
	protected float growProgress;
	protected Body body;
	protected TextureRegion region;

	@Override
	public void init() {
		super.init();

		this.body = this.createBody(3, 3, 10, 10, BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y - 4, 0);
		this.region = Graphics.getTexture(this.sprite);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.growProgress < 1) {
			float a = Dungeon.level.getLight((int) this.x / 16, (int) this.y / 16);
			this.growProgress += this.growSpeed * a * dt * 0.05f;

			if (this.growProgress > 1) {
				this.growProgress = 1;
			}
		}
	}

	@Override
	public void render() {
		// todo: grow states
		// old code: sprite + (int) Math.floor(this.growProgress * 2)
		Graphics.render(this.region, this.x, this.y - 4);
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);
		writer.writeFloat(this.growProgress);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.body.setTransform(this.x, this.y - 4, 0);
		this.growProgress = reader.readFloat();
	}

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity instanceof Weapon && this.growProgress == 1f) {
			this.done = true;
			Dungeon.level.set((int) this.x / 16, (int) this.y / 16, Terrain.DIRT);
		}
	}

	public ArrayList<Item> getDrops() {
		return new ArrayList<Item>();
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body.getWorld().destroyBody(this.body);

		if (this.growProgress == 1f) {
			ArrayList<Item> drops = this.getDrops();

			for (Item item : drops) {
				ItemHolder holder = new ItemHolder();

				holder.setItem(item);
				holder.x = this.x + Random.newInt(-8, 8);;
				holder.y = this.y + Random.newInt(-8, 8);

				Dungeon.area.add(holder);
				Dungeon.level.addSaveable(holder);
			}
		}
	}
}