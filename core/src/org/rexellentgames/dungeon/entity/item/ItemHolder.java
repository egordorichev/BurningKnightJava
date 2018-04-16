package org.rexellentgames.dungeon.entity.item;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.rexellentgames.dungeon.Dungeon;
import org.rexellentgames.dungeon.assets.Graphics;
import org.rexellentgames.dungeon.entity.level.Level;
import org.rexellentgames.dungeon.entity.level.SaveableEntity;
import org.rexellentgames.dungeon.entity.level.Terrain;
import org.rexellentgames.dungeon.game.input.Input;
import org.rexellentgames.dungeon.game.state.LoadState;
import org.rexellentgames.dungeon.util.Log;
import org.rexellentgames.dungeon.util.Random;
import org.rexellentgames.dungeon.util.file.FileReader;
import org.rexellentgames.dungeon.util.file.FileWriter;

import java.io.IOException;
import java.lang.reflect.Constructor;

public class ItemHolder extends SaveableEntity {
	private Body body;
	private Item item;
	private boolean set;
	public boolean falling;
	private int hx;
	private int hy;
	public int hw;
	public int hh;

	@Override
	public Body createBody(int x, int y, int w, int h, BodyDef.BodyType type, boolean sensor) {
		this.hx = x;
		this.hy = y;
		this.hw = w;
		this.hh = h;

		return super.createBody(x, y, w, h, type, sensor);
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

	@Override
	public void update(float dt) {
		this.t += dt;

		if (!this.set) {
			this.set = true;
			this.body.setTransform(this.x, this.y, 0);
		}

		super.update(dt);
		this.x = this.body.getPosition().x;
		this.y = this.body.getPosition().y;

		this.vel.mul(0.9f);
		this.body.setLinearVelocity(this.vel);

		if (this.falling) {
			this.vel.mul(0);
		}

		if (this.vel.len() <= 0.1f) {
			this.vel.mul(0);
			this.x = Math.round(this.x);
			this.y = Math.round(this.y);
		}

		if (Dungeon.level != null && !this.falling) {
			boolean onGround = false;

			for (int x = (int) Math.floor((this.hx + this.x) / 16); x < Math.ceil((this.hx + this.x + this.hw) / 16); x++) {
				for (int y = (int) Math.floor((this.hy + this.y + 8) / 16); y < Math.ceil((this.hy + this.y + 8 + this.hh / 3) / 16); y++) {
					if (x < 0 || y < 0 || x >= Level.getWidth() || y >= Level.getHeight()) {
						continue;
					}

					short t = Dungeon.level.get(x, y);

					if (!Dungeon.level.checkFor(x, y, Terrain.HOLE)) {
						onGround = true;
					}

					this.onTouch(t, x, y);
				}
			}

			if (!(Dungeon.game.getState() instanceof LoadState) && !this.falling && !onGround) {
				this.falling = true;
				this.t = 0;
			}
		}

		if (this.item instanceof Lamp) {
			Dungeon.level.addLightInRadius(this.x + this.w / 2, this.y + this.h / 2, 0, 0, 0, 2f, 3f, false);
		}
	}

	protected void onTouch(short t, int x, int y) {

	}

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
		if (this.falling) {
			TextureRegion sprite = this.item.getSprite();

			float s = 1 - this.t / 2;

			if (s <= 0) {
				this.done = true;
				Dungeon.level.removePlayerSaveable(this);
				Dungeon.level.droppedToChasm.add(this.item);

				Log.info("Dropped item to the next floor");

				return;
			}

			Graphics.render(sprite, x + sprite.getRegionWidth() / 2, y + sprite.getRegionHeight() / 2 - this.t * 8f,
				this.t * 360, sprite.getRegionWidth() / 2, sprite.getRegionHeight() / 2,
				false, false, s, s);
			Graphics.batch.setColor(1, 1, 1, 1);
		} else {
			Graphics.render(this.item.getSprite(), this.x, this.y);
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
			Object object = constructor.newInstance(new Object[]{});
			Item item = (Item) object;

			item.load(reader);
			this.setItem(item);
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
			BodyDef.BodyType.DynamicBody, false);

		return this;
	}

	public Item getItem() {
		return this.item;
	}
}