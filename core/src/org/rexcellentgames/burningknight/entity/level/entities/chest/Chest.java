package org.rexcellentgames.burningknight.entity.level.entities.chest;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.MassData;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.ItemHolder;
import org.rexcellentgames.burningknight.entity.item.key.KeyC;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Animation;
import org.rexcellentgames.burningknight.util.AnimationData;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class Chest extends SaveableEntity {
	private AnimationData data;
	protected Body body;
	protected Body sensor;
	protected boolean open;
	protected Item item;
	protected boolean create;
	public static ArrayList<Chest> all = new ArrayList<>();
	public boolean weapon;
	public boolean locked = true;

	public static Animation lockAnimations = Animation.make("door-lock", "-gold");

	private AnimationData unlock = lockAnimations.get("open");
	private TextureRegion idleLock = lockAnimations.getFrames("idle").get(0).frame;

	{
		h = 13;
	}

	@Override
	public void init() {
		super.init();

		this.data = this.getClosedAnim();

		this.data.setAutoPause(true);
		this.body = World.createSimpleBody(this, 4, 8, 8, 1, BodyDef.BodyType.DynamicBody, false);
		this.sensor = World.createSimpleBody(this, 0, 0, 16, 12, BodyDef.BodyType.DynamicBody, true);

		this.body.setTransform(this.x, this.y, 0);
		this.sensor.setTransform(this.x, this.y, 0);

		MassData data = new MassData();
		data.mass = 1000000000000000f;

		this.body.setMassData(data);

		all.add(this);
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
			if (this.locked) {
				this.colliding = true;
			} else {
				this.locked = false;

				this.open = true;
				this.data = this.getOpenAnim();

				this.data.setListener(new AnimationData.Listener() {
					@Override
					public void onEnd() {
						create = true;
					}
				});
			}
		}
	}

	@Override
	public void onCollisionEnd(Entity entity) {
		super.onCollisionEnd(entity);

		if (entity instanceof Player) {
			this.colliding = false;
		}
	}

	private float al;
	private boolean colliding;

	@Override
	public void destroy() {
		super.destroy();
		this.body = World.removeBody(this.body);
		this.sensor = World.removeBody(this.sensor);
		all.remove(this);
	}

	public void toMimic() {
		Mimic chest = new Mimic();

		chest.x = this.x;
		chest.y = this.y;
		chest.weapon = this.weapon;
		chest.locked = this.locked;

		Dungeon.area.add(chest);
		LevelSave.add(chest);

		this.done = true;
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

		this.locked = reader.readBoolean();
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

		writer.writeBoolean(this.locked);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		this.t += dt;

		if (this.item != null && this.create) {
			this.open();
		}

		if (this.data != null && this.data.update(dt)) {
			if (this.data == this.getOpenAnim()) {
				this.data = this.getOpenedAnim();
			}
		}

		this.sensor.setTransform(this.x, this.y, 0);
		this.body.setTransform(this.x, this.y, 0);

		this.al += ((this.colliding ? 1f : 0f) - this.al) * dt * 3;

		if (this.al >= 0.5f && Input.instance.wasPressed("interact")) {
			if (this.locked) {
				Item key = Player.instance.getInventory().findItem(KeyC.class);

				if (key == null) {
					this.colliding = true;
					Player.instance.playSfx("item_nocash");
					return;
				}

				drawOpenAnim = true;

				key.setCount(key.getCount() - 1);
				this.locked = false;

				this.open = true;
				this.data = this.getOpenAnim();

				this.data.setListener(new AnimationData.Listener() {
					@Override
					public void onEnd() {
						create = true;
					}
				});
			}
		}

		if (this.drawOpenAnim) {
			if (unlock.update(dt)) {
				this.drawOpenAnim = false;
			}
		}
	}

	private boolean drawOpenAnim;

	public void open() {
		ItemHolder holder = new ItemHolder();

		holder.x = this.x + (this.w - this.item.getSprite().getRegionWidth()) / 2;
		holder.y = this.y - 3;

		holder.setItem(this.item);

		Dungeon.area.add(holder);
		LevelSave.add(holder);

		this.item = null;
		this.create = false;

		this.data = this.getOpenedAnim();
		this.open = true;
	}

	public static Chest random() {
		float r = Random.newFloat();

		if (r < 0.5f) {
			WoodenChest chest = new WoodenChest();

			if (Random.chance(30)) {
				chest.locked = false;
			}

			return chest;
		} else if (r < 0.8f) {
			return new IronChest();
		}

		return new GoldenChest();
	}

	@Override
	public void render() {
		if (this.data != null) {
			TextureRegion sprite = this.data.getCurrent().frame;

			int w = sprite.getRegionWidth();

			float sx = 1f;
			float sy = 1f;//(float) (1f + Math.sin(this.t * 3f) / 15f);
			Graphics.render(sprite, this.x + w / 2, this.y, 0,
				w / 2, 0, false, false, sx, sy);
		}

		float x = this.x + (w - idleLock.getRegionWidth()) / 2;
		float y = this.y + (h - idleLock.getRegionHeight()) / 2 +
			(float) Math.sin(this.t) * 1.8f;

		if (this.locked) {
			if (al > 0) {
				Graphics.batch.end();
				Mob.shader.begin();
				Mob.shader.setUniformf("u_color", new Vector3(1, 1, 1));
				Mob.shader.setUniformf("u_a", al);
				Mob.shader.end();
				Graphics.batch.setShader(Mob.shader);
				Graphics.batch.begin();

				for (int yy = -1; yy < 2; yy++) {
					for (int xx = -1; xx < 2; xx++) {
						if (Math.abs(xx) + Math.abs(yy) == 1) {
							Graphics.render(this.idleLock, x + xx, y + yy);
						}
					}
				}

				Graphics.batch.end();
				Graphics.batch.setShader(null);
				Graphics.batch.begin();
			}

			Graphics.render(this.idleLock, x, y);
		} else if (drawOpenAnim) {
			unlock.render(x, y, false);
		}
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x, this.y, this.w, this.h);
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