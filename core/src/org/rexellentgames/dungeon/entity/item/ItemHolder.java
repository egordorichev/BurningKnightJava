package org.rexellentgames.dungeon.entity.item;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;
import java.lang.reflect.Constructor;

public class ItemHolder extends SaveableEntity {
	private Body body;
	private Item item;

	@Override
	public void init() {
		super.init();

		this.depth = -1;
	}

	@Override
	public void destroy() {
		super.destroy();

		if (this.body != null) {
			this.body.getWorld().destroyBody(this.body);
		}
	}

	@Override
	public void render() {
		Graphics.render(this.item.getSprite(), this.x, this.y);
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeString(this.item.getClass().getName());
		this.item.save(writer);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);
		this.body.setTransform(this.x, this.y, 0);

		String type = reader.readString();

		try {
			Class<?> clazz = Class.forName(type);
			Constructor<?> constructor = clazz.getConstructor();
			Object object = constructor.newInstance(new Object[]{});

			this.item = (Item) object;
			this.item.load(reader);
		} catch (Exception e) {
			Dungeon.reportException(e);
		}
	}

	public ItemHolder setItem(Item item) {
		this.item = item;

		if (this.body != null) {
			this.body.getWorld().destroyBody(this.body);
		}

		this.body = this.createBody(0, 0, item.getSprite().getRegionWidth(), item.getSprite().getRegionHeight(),
			BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y, 0);

		return this;
	}

	public Item getItem() {
		return this.item;
	}
}