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
import org.rexellentgames.dungeon.entity.item.weapon.gun.bullet.Part;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.physics.World;
import org.rexellentgames.dungeon.util.AnimationData;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;
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
		this.body = World.createSimpleBody(this, 5, 0, 16, 11, BodyDef.BodyType.DynamicBody, true);
		this.body.setTransform(this.x, this.y, 0);
	}

	public Item generate() {
		return null;
	}

	public void setItem(Item item) {
		this.item = item;
		this.item.generate();
	}

	@Override
	public void onCollision(Entity entity) {
		if (!this.open && entity instanceof Player) {
			this.open = true;
			this.data = this.getOpenAnim();
			this.create = true;

			for (int i = 0; i < 20; i++) {
				Part part = new Part();

				part.x = this.x + Random.newFloat(this.w);
				part.y = this.y + Random.newFloat(this.h);

				Dungeon.area.add(part);
			}
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		this.open = reader.readBoolean();
		this.body.setTransform(this.x, this.y, 0);

		if (!this.open) {
			String name = reader.readString();

			try {
				Class<?> clazz = Class.forName(name);
				Constructor<?> constructor = clazz.getConstructor();
				Object object = constructor.newInstance(new Object[]{});

				Item item = (Item) object;
				item.load(reader);

				this.item = item;
			} catch (Exception e) {
				Log.error(name);
				Dungeon.reportException(e);
			}
		}

		if (this.item == null && !this.open) {
			Log.error("Something wrong with chest");
		}

		if (this.open) {
			this.data = this.getOpenedAnim();
		}
	}

	@Override
	public void save(FileWriter writer) throws IOException {
		super.save(writer);

		writer.writeBoolean(this.open);

		if (!this.open && this.item == null) {
			Log.error("Something wrong when saving");
		}

		if (!this.open) {
			writer.writeString(this.item.getClass().getName());
			this.item.save(writer);
		}
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.t += dt;

		if (this.item != null && this.create) {
			for (int i = 0; i < 20; i++) {
				Part part = new Part();

				part.x = Random.newFloat(this.w) + this.x;
				part.y = Random.newFloat(this.h) + this.y;

				Dungeon.area.add(part);
			}

			ItemHolder holder = new ItemHolder();

			holder.x = this.x + 5 + (this.w - this.item.getSprite().getRegionWidth()) / 2;
			holder.y = this.y - 3;

			holder.setItem(this.item);

			Dungeon.area.add(holder);
			Dungeon.level.addSaveable(holder);

			/*if (this.item instanceof Gun || this.item instanceof Bow || this.item instanceof RocketLauncher) {
				Item item = (this.item instanceof Gun ? new BulletA().setCount(Random.newInt(100, 200)) :
					(this.item instanceof Bow ? new ArrowA().setCount(Random.newInt(100, 200)) : new RocketA().setCount(Random.newInt(30, 100))));
				holder = new ItemHolder();

				holder.x = this.x + 5 + (this.w - item.getSprite().getRegionWidth()) / 2;
				holder.y = this.y - 3;

				holder.setItem(item);

				Dungeon.area.add(holder);
				Dungeon.level.addSaveable(holder);
			}*/

			this.item = null;
			this.create = false;
		}

		if (this.data.update(dt)) {
			if (this.data == this.getOpenAnim()) {
				this.data = this.getOpenedAnim();
			}
		}
	}

	public static Chest random() {
		return new WoodenChest();
	}

	@Override
	public void render() {
		TextureRegion sprite = this.data.getCurrent().frame;

		int w = sprite.getRegionWidth();
		int h = sprite.getRegionHeight();

		float a = 0;
		float sx = 1f;
		float sy = (float) (1f + Math.sin(this.t * 3f) / 15f);

		Graphics.startShadows();
		Graphics.render(sprite, this.x + w / 2, this.y - h / 2 + 1.5f, a, w / 2, h / 2, false, false, sx, -sy);
		Graphics.endShadows();
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