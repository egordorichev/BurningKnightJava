package org.rexcellentgames.burningknight.entity.level.entities.chest;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Part;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.weapon.gun.bullet.Part;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

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
		
		if (this.body != null) {
			this.body.setTransform(this.x, this.y, 0);
		}
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
				Object object = constructor.newInstance();

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
			LevelSave.add(holder);

			/*if (this.item instanceof Gun || this.item instanceof Bow || this.item instanceof RocketLauncher) {
				Item item = (this.item instanceof Gun ? new BulletA().setCount(Random.newInt(100, 200)) :
					(this.item instanceof Bow ? new ArrowA().setCount(Random.newInt(100, 200)) : new RocketA().setCount(Random.newInt(30, 100))));
				holder = new ItemHolder();

				holder.x = this.x + 5 + (this.w - item.getSprite().getRegionWidth()) / 2;
				holder.y = this.y - 3;

				holder.setItem(item);

				Dungeon.area.add(holder);
				LevelSave.add(holder);
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

		float sx = 1f;
		float sy = (float) (1f + Math.sin(this.t * 3f) / 15f);
		Graphics.render(sprite, this.x + w / 2, this.y, 0,
			w / 2, 0, false, false, sx, sy);
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x + this.data.getCurrent().frame.getRegionWidth() / 2, this.y, this.w, this.h);
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