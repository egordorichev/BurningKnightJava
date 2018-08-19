package org.rexcellentgames.burningknight.entity.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
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
import java.util.ArrayList;

public class ItemHolder extends SaveableEntity {
	private Body body;
	private Item item;

	public static ArrayList<ItemHolder> all = new ArrayList<>();

	public boolean falling;
	public int hw;
	public int hh;
	public boolean auto;
	private float z = 0;

	public Body createSimpleBody(int x, int y, int w, int h, BodyDef.BodyType type, boolean sensor) {
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
	private boolean added;
	public ItemPrice price;

	public void sale() {
		getItem().sale = true;
		getItem().price = (byte) Math.max(0, Math.floor(getItem().price / 2));

		added = false;

		if (price != null) {
			price.done = true;
		}
	}

	public void unsale() {
		getItem().sale = false;
		getItem().price *= 2;

		if (getItem().price % 2 == 0) {
			getItem().price ++;
		}

		added = false;

		if (price != null) {
			price.done = true;
		}
	}

	@Override
	public void update(float dt) {
		if (this.getItem().shop && !added) {
			added = true;
			ItemPrice price = new ItemPrice();

			price.x = this.x + this.w / 2;
			price.y = this.y - 6 - (16 - this.h) / 2;
			price.price = this.getItem().price;
			price.sale = this.getItem().sale;

			this.price = price;
			Dungeon.area.add(price);
		}

		if (this.done) {
			return;
		}

		this.t += dt;
		this.last += dt;

		if (this.last > 0.5f) {
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

		this.item.update(dt);
		this.body.setLinearVelocity(this.vel);
	}

	protected void onTouch(short t, int x, int y) {

	}

	@Override
	public void init() {
		super.init();

		this.t = Random.newFloat(32f);
		this.last = Random.newFloat(1);

		if (this.body != null) {
			this.body.setTransform(this.x, this.y, 0);
		}

		all.add(this);
	}

	@Override
	public void destroy() {
		if (this.item instanceof Gold) {
			Gold.all.remove(this);
		}

		super.destroy();

		if (price != null) {
			price.remove();
			price = null;
		}

		if (this.body != null) {
			this.body = World.removeBody(this.body);
		}

		all.remove(this);
	}

	private float al;

	@Override
	public void render() {
		TextureRegion sprite = this.item.getSprite();

		float a = (float) Math.cos(this.t * 3f) * 8f * sz;
		float sy = (float) (1f + Math.sin(this.t * 2f) / 10f);

		Graphics.batch.end();

		float dt = Gdx.graphics.getDeltaTime();
		this.al = MathUtils.clamp(0, 1, this.al + (((Player.instance.pickupFx != null && Player.instance.pickupFx.item == this) ? 1 : 0) - this.al) * dt * 10);

		if (this.al > 0) {
			Mob.shader.begin();
			Mob.shader.setUniformf("u_color", new Vector3(1, 1, 1));
			Mob.shader.setUniformf("u_a", this.al);
			Mob.shader.end();
			Graphics.batch.setShader(Mob.shader);
			Graphics.batch.begin();

			for (int xx = -1; xx < 2; xx++) {
				for (int yy = -1; yy < 2; yy++) {
					if (Math.abs(xx) + Math.abs(yy) == 1) {
						Graphics.render(sprite, this.x + w / 2 + xx, this.y + this.z + h / 2 + yy, a,
							w / 2, h / 2, false, false, 1f, sy);
					}
				}
			}

			Graphics.batch.end();
			Graphics.batch.setShader(null);
		}

		WeaponBase.shader.begin();
		WeaponBase.shader.setUniformf("a", 1);
		WeaponBase.shader.setUniformf("time", Dungeon.time + this.t);
		WeaponBase.shader.end();
		Graphics.batch.setShader(WeaponBase.shader);
		Graphics.batch.begin();

		Graphics.render(sprite, this.x + w / 2, this.y + this.z + h / 2, a,
			w / 2, h / 2, false, false, 1f, sy);

		Graphics.batch.end();
		Graphics.batch.setShader(null);
		Graphics.batch.begin();
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x, this.y, this.w, this.h, this.z);
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
		this.body = this.createSimpleBody(-2, -2, item.getSprite().getRegionWidth() + 4, item.getSprite().getRegionHeight() + 4,
			BodyDef.BodyType.DynamicBody, false);

		this.w = item.getSprite().getRegionWidth();
		this.h = item.getSprite().getRegionHeight();

		return this;
	}

	public Item getItem() {
		return this.item;
	}
}