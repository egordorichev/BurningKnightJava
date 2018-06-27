package org.rexcellentgames.burningknight.entity.item;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.Log;
import org.rexcellentgames.burningknight.util.MathUtils;
import org.rexcellentgames.burningknight.util.Random;
import org.rexcellentgames.burningknight.util.Tween;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;
import java.lang.reflect.Constructor;

public class ItemHolder extends SaveableEntity {
	private Body body;
	private Item item;

	public boolean falling;
	private int hx;
	private int hy;
	public int hw;
	public int hh;
	public boolean auto;
	private float z = 0;

	public Body createSimpleBody(int x, int y, int w, int h, BodyDef.BodyType type, boolean sensor) {
		this.hx = x;
		this.hy = y;
		this.hw = w;
		this.hh = h;

		return World.createSimpleBody(this, x, y, w, h, type, sensor);
	}

	public Body getBody() {
		return body;
	}

	public void randomVel() {
		double a = Random.newFloat((float) (Math.PI * 2));

		this.vel.x = (float) (Math.cos(a) * 100f);
		this.vel.y = (float) (Math.sin(a) * 100f);
	}

	public void velToMouse() {
		float dx = Input.instance.worldMouse.x - this.x;
		float dy = Input.instance.worldMouse.y - this.y;

		float a = (float) Math.atan2(dy, dx);

		this.vel.x = (float) (Math.cos(a) * 100f);
		this.vel.y = (float) (Math.sin(a) * 100f);
	}

	public float last;

	@Override
	public void update(float dt) {
		if (this.done) {
			return;
		}

		this.t += dt;
		this.last += dt;

		if (this.last > 0.2f) {
			this.last = 0;
			Spark.randomOn(this.x, this.y, this.hw, this.hh);
		}

		super.update(dt);

		if (this.body == null) {
			Log.error("Null body with " + (this.item == null ? "null" : this.item.getClass().getSimpleName()));
		} else {
			this.x = this.body.getPosition().x;
			this.y = this.body.getPosition().y - this.z;
		}

		this.vel.mul(0.9f);

		this.sz = Math.max(1, this.sz - this.sz * dt);

		if (this.vel.len() <= 0.1f) {
			this.vel.mul(0);
			this.x = Math.round(this.x);
			this.y = Math.round(this.y);

			this.z += Math.cos(this.t * 1.7f) / 5f * (this.sz / 2) * dt * 60f;

			this.z = MathUtils.clamp(0, 5f, this.z);

			this.body.setTransform(this.x, this.y + this.z, 0);
		}

		if (this.item instanceof Lamp) {
			Dungeon.level.addLightInRadius(this.x + this.w / 2, this.y + this.h / 2, 0, 0, 0, 2f, 3f, false);

			// Camera.follow(this, false);
		}

		this.body.setLinearVelocity(this.vel);
	}

	protected void onTouch(short t, int x, int y) {

	}

	@Override
	public void init() {
		super.init();

		this.t = Random.newFloat(32f);

		if (this.body != null) {
			this.body.setTransform(this.x, this.y, 0);
		}
	}

	@Override
	public void destroy() {
		if (this.item instanceof Gold) {
			Gold.all.remove(this);
		}

		super.destroy();

		if (this.body != null) {
			this.body = World.removeBody(this.body);
		}
	}

	@Override
	public void render() {
		TextureRegion sprite = this.item.getSprite();

		int w = sprite.getRegionWidth();
		int h = sprite.getRegionHeight();

		float a = (float) Math.cos(this.t * 3f) * 8f * sz;
		float sy = (float) (1f + Math.sin(this.t * 2f) / 10f);

		if (this.item instanceof WeaponBase) {
			((WeaponBase) this.item).renderAt(this.x + w / 2, this.y + this.z + h / 2, a,
				w / 2, h / 2, false, false, 1f, sy);
		} else {
			Graphics.render(sprite, this.x + w / 2, this.y + this.z + h / 2, a,
				w / 2, h / 2, false, false, 1f, sy);
		}
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x, this.y, this.hw, this.hh, this.z);
	}

	private float sz = 1f;

	@Override
	public void onCollision(Entity entity) {
		super.onCollision(entity);

		if (entity instanceof Creature) {
			Tween.to(new Tween.Task(4f, 0.3f) {
				@Override
				public float getValue() {
					return sz;
				}

				@Override
				public void setValue(float value) {
					sz = value;
				}
			});
		}
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

		String type = reader.readString();

		try {
			Class<?> clazz = Class.forName(type);
			Constructor<?> constructor = clazz.getConstructor();
			Object object = constructor.newInstance();
			Item item = (Item) object;

			item.load(reader);
			this.setItem(item);
		} catch (Exception e) {
			Dungeon.reportException(e);
		}

		this.body.setTransform(this.x, this.y, 0);
	}

	public ItemHolder setItem(Item item) {
		this.item = item;

		if (this.body != null) {
			this.body = World.removeBody(this.body);
		}

		if (this.item == null) {
			Log.error("Warn: null item");
			return this;
		}

		if (this.item instanceof Gold) {
			Gold.all.add(this);
		}

		// This might be bad!
		this.body = this.createSimpleBody(0, 0, item.getSprite().getRegionWidth(), item.getSprite().getRegionHeight(),
			BodyDef.BodyType.DynamicBody, false);

		return this;
	}

	public Item getItem() {
		return this.item;
	}
}