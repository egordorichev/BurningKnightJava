package org.rexcellentgames.burningknight.entity.level.entities.chest;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.MassData;
import org.rexcellentgames.burningknight.Dungeon;
import org.rexcellentgames.burningknight.assets.Graphics;
import org.rexcellentgames.burningknight.entity.Entity;
import org.rexcellentgames.burningknight.entity.creature.Creature;
import org.rexcellentgames.burningknight.entity.creature.buff.BurningBuff;
import org.rexcellentgames.burningknight.entity.creature.fx.HeartFx;
import org.rexcellentgames.burningknight.entity.creature.mob.Mob;
import org.rexcellentgames.burningknight.entity.creature.player.Player;
import org.rexcellentgames.burningknight.entity.fx.TerrainFlameFx;
import org.rexcellentgames.burningknight.entity.item.Gold;
import org.rexcellentgames.burningknight.entity.item.Item;
import org.rexcellentgames.burningknight.entity.item.key.KeyC;
import org.rexcellentgames.burningknight.entity.item.weapon.Weapon;
import org.rexcellentgames.burningknight.entity.item.weapon.projectile.Projectile;
import org.rexcellentgames.burningknight.entity.level.Level;
import org.rexcellentgames.burningknight.entity.level.SaveableEntity;
import org.rexcellentgames.burningknight.entity.level.entities.fx.PoofFx;
import org.rexcellentgames.burningknight.entity.level.save.LevelSave;
import org.rexcellentgames.burningknight.game.input.Input;
import org.rexcellentgames.burningknight.physics.World;
import org.rexcellentgames.burningknight.util.*;
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
	public boolean burning;

	public static Animation lockAnimations = Animation.make("door-lock", "-gold");

	private AnimationData unlock = lockAnimations.get("open");
	public static TextureRegion idleLock = lockAnimations.getFrames("idle").get(0).frame;
	private TextureRegion halfBroken = getAnim().getFrames("break").get(0).frame;
	private TextureRegion broken = getAnim().getFrames("break").get(1).frame;

	private byte hp = 14;

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

		World.checkLocked(this.body).setTransform(this.x, this.y, 0);
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
		if (this.hp == 0) {
			return;
		}

		if (entity instanceof Creature) {
			if (((Creature) entity).hasBuff(BurningBuff.class)) {
				this.burning = true;
			} else if (this.burning) {
				((Creature) entity).addBuff(new BurningBuff());
			}
		}

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
		} else if (!this.open && (entity instanceof Projectile || entity instanceof Weapon)) {
			if (entity instanceof Projectile) {
				if (!(((Projectile) entity).owner instanceof Player)) {
					return;
				}
			} else if (entity instanceof Weapon) {
				if (!(((Weapon) entity).getOwner() instanceof Player)) {
					return;
				}
			}

			hit();
		}
	}

	private void hit() {
		if (this.hp == 0) {
			return;
		}

		this.hp -= 1;

		if (this.hp <= 0) {
			this.hp = 0;
			this.burning = false;

			for (int i = 0; i < 10; i++) {
				PoofFx fx = new PoofFx();

				fx.x = this.x + this.w / 2;
				fx.y = this.y + this.h / 2;

				Dungeon.area.add(fx);
			}

			this.locked = false;
			this.createLoot = true;

			this.body = World.removeBody(this.body);
			this.sensor = World.removeBody(this.sensor);
		}
	}

	private boolean createLoot;

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
		World.checkLocked(this.body).setTransform(this.x, this.y, 0);

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
		this.hp = reader.readByte();
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
		writer.writeByte(this.hp);
	}

	@Override
	public void update(float dt) {
		super.update(dt);

		if (this.hp == 0) {
			this.burning = false;
		}

		if (createLoot) {
			if (Random.chance(50)) {
				HeartFx fx = new HeartFx();

				fx.x = this.x + (this.w - fx.w) / 2;
				fx.y = this.y + (this.h - fx.h) / 2;

				Dungeon.area.add(fx);
			}

			if (Random.chance(30)) {
				ItemHolder fx = new ItemHolder(new KeyC());

				fx.x = this.x + (this.w - fx.w) / 2;
				fx.y = this.y + (this.h - fx.h) / 2;

				Dungeon.area.add(fx);
			}

			if (Random.chance(30)) {
				ItemHolder fx = new ItemHolder(new Gold());

				fx.getItem().generate();
				fx.x = this.x + (this.w - fx.w) / 2;
				fx.y = this.y + (this.h - fx.h) / 2;

				Dungeon.area.add(fx);
			}

			createLoot = false;
		}

		this.t += dt;

		if (this.item != null && this.create) {
			this.open();
		}

		if (this.data != null && this.data.update(dt)) {
			if (this.data == this.getOpenAnim()) {
				this.data = this.getOpenedAnim();
			}
		}

		if (this.body != null) {
			this.sensor.setTransform(this.x, this.y, 0);
			World.checkLocked(this.body).setTransform(this.x, this.y, 0);
		}

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

		if (this.burning) {
			lastFlame += dt;

			if (this.lastFlame >= 0.05f) {
				this.lastFlame = 0;
				TerrainFlameFx fx = new TerrainFlameFx();
				fx.x = this.x + Random.newFloat(this.w);
				fx.y = this.y + Random.newFloat(this.h) - 4;
				fx.depth = 1;
				Dungeon.area.add(fx);
			}

			damage += dt * 2;

			if (damage >= 1f) {
				damage = 0f;
				hit();
			}
		} else {
			int i = Level.toIndex((int) Math.floor(this.x / 16), (int) Math.floor((this.y + 8) / 16));
			int info = Dungeon.level.getInfo(i);

			if (BitHelper.isBitSet(info, 0)) {
				// Burning
				this.damage = 0;
				this.burning = true;

				for (int j : PathFinder.NEIGHBOURS4) {
					Dungeon.level.setOnFire(i + j, true);
				}
			}
		}
	}

	private float damage;
	private float lastFlame;
	private boolean drawOpenAnim;

	public void open() {
		ItemHolder holder = new ItemHolder(this.item);

		holder.x = this.x + (this.w - this.item.getSprite().getRegionWidth()) / 2;
		holder.y = this.y - 3;

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
			TextureRegion sprite;

			if (this.open || this.hp > 6) {
				sprite = this.data.getCurrent().frame;
			} else if (this.hp == 0) {
				sprite = broken;
			} else {
				sprite = halfBroken;
			}

			int w = sprite.getRegionWidth();

			float sx = 1f;
			float sy = 1f;
			Graphics.render(sprite, this.x + w / 2 - 1, this.y, 0,
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

	protected Animation getAnim() {
		return null;
	}

	protected AnimationData getOpenedAnim() {
		return null;
	}

	protected AnimationData getOpenAnim() {
		return null;
	}
}