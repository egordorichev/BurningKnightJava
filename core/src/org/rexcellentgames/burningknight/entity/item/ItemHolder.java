package org.rexcellentgames.burningknight.entity.item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.item.weapon.WeaponBase;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.entity.level.Terrain;
import org.rexcellentgames.burningknight.entity.level.entities.chest.Chest;
import org.rexcellentgames.burningknight.entity.level.rooms.Room;
import org.rexcellentgames.burningknight.entity.level.rooms.shop.ShopRoom;
import org.rexcellentgames.burningknight.game.Ui;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.game.state.InGameState;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.*;
import org.rexcellentgames.burningknight.util.file.FileReader;
import org.rexcellentgames.burningknight.util.file.FileWriter;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class ItemHolder extends SaveableEntity {
	protected Body body;

	public ItemHolder(Item item) {
		this.setItem(item);
	}

	public ItemHolder() {

	}

	protected float startX = 0f;
	protected float startY = 0f;

	protected boolean fake = false;
	protected Item item;

	public void setItem(Item item) {
		this.item = item;

		this.body = World.removeBody(this.body);

		if (item == null) {
			return;
		}

		if (this.item.getSprite() == null) {
			return;
		}

		createBody = true;

		this.w = item.getSprite().getRegionWidth();
		this.h = item.getSprite().getRegionHeight();
	}

	public boolean createBody;
	public boolean falling;
	public boolean auto;

	protected float last;
	public ItemPrice price;

	private float z;
	private int hw;
	private int hh;
	private boolean added;

	public float al;
	private float sz = 1f;

	protected Body createSimpleBody(int x, int y, int w, int h, BodyDef.BodyType type, boolean sensor) {
		this.hw = w;
		this.hh = h;

		return World.createSimpleBody(this, x, y, w, h, type, sensor);
	}

	public void randomVelocity() {
		float a = Random.newFloat((float) (Math.PI * 2));
		float f = Random.newFloat(60f, 150f);

		this.velocity.x = (float) (Math.cos(a) * f);
		this.velocity.y = (float) (Math.sin(a) * f);
	}

	public void velocityToMouse() {
		float dx = Input.instance.worldMouse.x - this.x;
		float dy = Input.instance.worldMouse.y - this.y;

		float a = (float) Math.atan2(dy, dx);

		this.velocity.x = (float) (Math.cos(a) * 100f);
		this.velocity.y = (float) (Math.sin(a) * 100f);
	}

	public void sale() {
		item.sale = true;
		item.price = (int) Math.max(0.0, Math.floor(item.price / 2));

		added = false;

		if (price != null) {
			price.done = true;
		}
	}

	public boolean shouldCollide(Object entity, Contact contact, Fixture fixture) {
		if (entity instanceof Chest) {
			return false;
		}

		if (item instanceof Gold && !(entity instanceof Player)) {
			return false;
		}

		return super.shouldCollide(entity, contact, fixture);
	}

	@Override
	public void update(float dt) {
		if (falling) {
			fall -= dt;

			if (fall <= 0) {
				done = true;
			}
		}

		if (createBody) {
			if (!fake) {
				this.body = this.createSimpleBody(-2, -2, item.getSprite().getRegionWidth() + 4, item.getSprite().getRegionHeight() + 4, BodyDef.BodyType.DynamicBody, false);
				this.body.setTransform(this.x, this.y + this.z, 0f);
			}

			createBody = false;
		}

		if (this.item == null) {
			return;
		}

		if (this.item.shop && !added) {
			added = true;

			ItemPrice price = new ItemPrice();

			this.item.price = this.item.getPrice();
			this.item.price = Math.max(1, this.item.price + Random.newInt(-3, 3));

			price.x = this.x + this.w / 2;
			price.y = this.y - 6f - (16 - this.h) / 2;
			price.price = this.item.price;
			price.sale = this.item.sale;

			this.price = price;

			Dungeon.area.add(price);
		}

		if (!this.item.shop && !falling) {
			boolean found = false;
			int x = (int) (Math.floor(((this.x) / 16)) - 1);

			while (x < Math.ceil(((this.x + this.hw + 8) / 16))) {
				int y = (int) (Math.floor(((this.y) / 16)) - 1);

				while (y < Math.ceil(((this.y + 16f + this.hh) / 16))) {
					if (x < 0 || y < 0 || x >= Level.getWidth() || y >= Level.getHeight()) {
						y++;
						continue;
					}

					if (CollisionHelper.check(this.x, this.y, w, h, x * 16f, y * 16f - 8f, 32f, 32f)) {
						int i = Level.toIndex(x, y);
						int l = Dungeon.level.data[i];

						if (l < 1 || l == Terrain.FLOOR_A || l == Terrain.FLOOR_B || l == Terrain.FLOOR_C || l == Terrain.FLOOR_D) {
							found = true;
							break;
						}
					}

					if (found) {
						break;
					}

					y++;
				}

				if (found) {
					break;
				}

				x++;
			}

			if (!found) {
				Log.error("Fallling");
				falling = true;
			}
		}

		this.t += dt;
		this.last += dt;

		if (this.done) {
			return;
		}

		if (this.last > 0.5f) {
			this.last = 0f;
			Spark.randomOn(this.x, this.y, this.hw, this.hh);
		}

		super.update(dt);

		if (this.body != null) {
			if (this.item.shop) {
				World.checkLocked(this.body).setTransform(this.x, this.y + this.z, 0f);
			} else {
				this.x = this.body.getPosition().x;
				this.y = this.body.getPosition().y - this.z;
			}
		}

		this.velocity.mul(0.9f);

		if (!InGameState.dark && item instanceof Gold && item.autoPickup && !done) {
			Room room = Dungeon.level.findRoomFor(this.x + this.w / 2, this.y + this.h / 2);

			if (room != null && !(room instanceof ShopRoom) && room == Player.instance.room && !room.hidden) {
				float dx = Player.instance.x + Player.instance.w / 2 - this.x - this.w / 2;
				float dy = Player.instance.y + Player.instance.h / 2 - this.y - this.h / 2;
				float d = (float) Math.sqrt((dx * dx + dy * dy));
				float f = 20f;

				this.velocity.x += (dx / d) * f;
				this.velocity.y += (dy / d) * f;
			}
		}

		this.sz = Math.max(1f, this.sz - this.sz * dt);

		if (this.velocity.len() <= 0.1f) {
			this.velocity.mul(0f);
			this.x = Math.round(this.x);
			this.y = Math.round(this.y);

			this.z += (Math.cos((this.t * 1.7f)) / 5f * (this.sz / 2) * dt * 60.0);

			this.z = MathUtils.clamp(0f, 5f, this.z);

			if (this.body != null) {
				World.checkLocked(this.body).setTransform(this.x, this.y + this.z, 0f);
			}
		}

		this.item.update(dt);

		if (this.body != null) {
			this.body.setLinearVelocity(this.velocity);
		}
	}

	@Override
	public void init() {
		super.init();

		startX = x;
		startY = y;

		this.t = Random.newFloat(32f);
		this.last = Random.newFloat(1f);

		if (this.body != null) {
			World.checkLocked(this.body).setTransform(this.x, this.y, 0f);
		}

		all.add(this);
	}

	@Override
	public void destroy() {
		super.destroy();

		if (price != null) {
			price.remove();
			price = null;
		}

		this.body = World.removeBody(this.body);
		all.remove(this);
	}

	private float fall = 1f;

	@Override
	public void render() {
		if (this.item == null) {
			return;
		}

		TextureRegion sprite = this.item.getSprite();

		float a = (float) (Math.cos((this.t * 3f)) * 8f * sz);
		float sy = (float) ((1f + Math.sin((this.t * 2f)) / 10f) * fall);

		Graphics.batch.end();

		float dt = Gdx.graphics.getDeltaTime();
		this.al = MathUtils.clamp(0f, 1f, this.al + (((Player.instance.pickupFx != null && Player.instance.pickupFx.item == this) ? 1 : 0) - this.al) * dt * 10f);

		if (this.al > 0.05f && !Ui.hideUi) {
			Mob.shader.begin();
			Mob.shader.setUniformf("u_color", new Vector3(1f, 1f, 1f));
			Mob.shader.setUniformf("u_a", this.al);
			Mob.shader.end();
			Graphics.batch.setShader(Mob.shader);
			Graphics.batch.begin();

			for (int xx = -1; xx < 2; xx++) {
				for (int yy = -1; yy < 2; yy++) {
					if (Math.abs(xx) + Math.abs(yy) == 1) {
						Graphics.render(sprite, this.x + w / 2 + xx, this.y + this.z + h / 2 + yy, a, w / 2, h / 2, false, false, fall, sy);
					}
				}
			}

			Graphics.batch.end();
			Graphics.batch.setShader(null);
		}

		WeaponBase.shader.begin();
		WeaponBase.shader.setUniformf("a", 1f);
		WeaponBase.shader.setUniformf("gray", 1f);
		WeaponBase.shader.setUniformf("time", Dungeon.time + this.t);
		WeaponBase.shader.end();
		Graphics.batch.setShader(WeaponBase.shader);
		Graphics.batch.begin();

		Graphics.render(sprite, this.x + w / 2, this.y + this.z + h / 2, a, w / 2, h / 2, false, false, fall, sy);

		Graphics.batch.end();
		Graphics.batch.setShader(null);
		Graphics.batch.begin();
	}

	@Override
	public void renderShadow() {
		Graphics.shadow(this.x, this.y, this.w * fall, this.h * fall, this.z);
	}

	public Item getItem() {
		return item;
	}

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

		writer.writeBoolean(this.item != null);

		if (this.item != null) {
			writer.writeString(this.item.getClass().getName());
			this.item.save(writer);
		}
	}

	@Override
	public void load(FileReader reader) throws IOException {
		super.load(reader);

		if (reader.readBoolean()) {
			String type = reader.readString();

			try {
				Class clazz = Class.forName(type);
				Constructor constructor = clazz.getConstructor();
				Object object = constructor.newInstance();
				Item item = (Item) object;

				item.load(reader);
				this.item = item;
			} catch (Exception e) {
				Dungeon.reportException(e);
			}
		}

		fake = false;

		if (this.body != null) {
			this.body.setTransform(this.x, this.y, 0f);
		}
	}

	public static ArrayList<ItemHolder> all = new ArrayList<>();
}