package org.rexellentgames.dungeon.entity.level.entities.chest;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.Entity;
import org.rexellentgames.dungeon.entity.creature.player.Player;
import org.rexellentgames.dungeon.entity.item.Item;
import org.rexellentgames.dungeon.entity.item.ItemHolder;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;
import java.lang.reflect.Constructor;

public class Chest extends SaveableEntity {
	private AnimationData data;
	protected Body body;
	protected boolean open;
	protected Item item;
	protected boolean create;

	@Override
	public void init() {
		super.init();

		this.data = this.getClosedAnim();
		this.data.setAutoPause(true);
		this.body = this.createBody(5, 0, 16, 11, BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y, 0);
	}

	public Item generate() {
		return null;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	@Override
	public void onCollision(Entity entity) {
		if (!this.open && entity instanceof Player) {
			this.open = true;
			this.data = this.getOpenAnim();
			this.create = true;
		}
	}

	@Override
	public void destroy() {
		super.destroy();

		this.body.getWorld().destroyBody(this.body);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.open = reader.readBoolean();
		this.body.setTransform(this.x, this.y, 0);

		if (reader.readBoolean()) {
			String name = reader.readString();

			try {
				Class<?> clazz = Class.forName(name);
				Constructor<?> constructor = clazz.getConstructor();
				Object object = constructor.newInstance(new Object[]{});

				Item item = (Item) object;
				item.load(reader);
			} catch (Exception e) {
				Log.error(name);
				Dungeon.reportException(e);
			}
		} else {
			this.data = this.getOpenedAnim();
		}
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeBoolean(this.open);
		writer.writeBoolean(this.item != null);

		if (this.item != null) {
			writer.writeString(this.item.getClass().getName());
			this.item.save(writer);
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.t += dt;

		if (this.item != null && this.create) {
			ItemHolder holder = new ItemHolder();

			holder.x = this.x;
			holder.y = this.y + 8;

			holder.setItem(this.item);

			Dungeon.area.add(holder);
			Dungeon.level.addSaveable(holder);

			this.item = null;
			this.create = false;
		}

		if (this.data.update(dt)) {
			if (this.data == this.getOpenAnim()) {
				this.data = this.getOpenedAnim();
			}
		}
	}

	@Override
	public void render() {
		TextureRegion sprite = this.data.getCurrent().frame;

		int w = sprite.getRegionWidth();
		int h = sprite.getRegionHeight();

		float a = (float) Math.cos(this.t * 3f) * 2f;
		float sx = (float) (1f + Math.cos(this.t * 4f) / 13f);
		float sy = (float) (1f + Math.sin(this.t * 3f) / 15f);

		Graphics.batch.setColor(0, 0, 0, 0.5f);
		Graphics.render(sprite, this.x + w / 2, this.y - 3, a, w / 2, h / 2, false, false, sx, -sy / 2);
		Graphics.batch.setColor(1, 1, 1, 1);
		Graphics.render(sprite, this.x + w / 2, this.y + h / 2, a,
			w / 2, h / 2, false, false, sx, sy);
	}

	protected AnimationData getClosedAnim() {
		return null;
	}

	protected AnimationData getOpenedAnim() {
		return null;
	}

	protected AnimationData getOpenAnim() {
		return null;
	}
}